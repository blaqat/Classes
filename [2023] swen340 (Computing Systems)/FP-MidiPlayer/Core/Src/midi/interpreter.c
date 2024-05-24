/*   interpreter.c   */
#include "interpreter.h"
#include "UART.h"
#include <stdarg.h>
#include <stdio.h>
#include <string.h>
#include "printf.h"
#include "little_endian.h"
#include <stdlib.h>

#include "adc.h"
#include "dac.h"
#include "systick.h"

//This makes the entire project work.
void nothing_function(){
	printlf("");
}

Header* midi_header (song s) {
	Header* song_header = (Header *)s.p_song;
	return convert_header(song_header);
}

SongInfo song_infos[5];// = {INIT_SONGINFO(X), INIT_SONGINFO(X), INIT_SONGINFO(X), INIT_SONGINFO(X)};
TONE song_tones[5][200];

TONE* get_track(int index){
	return song_tones[index];
}

SongInfo get_song_info (int index) {
	if(song_infos[index].finished == 1){
		return song_infos[index];
	}

	SongInfo * s_info =(SongInfo *)malloc(sizeof(SongInfo));
	song s = get_song(index);
	Header* h = midi_header(s);
	uint8_t* address = s.p_song + h->len + sizeof(h->chunk_type) + sizeof(h->len);

	//Parse through tracks
	for(int i = 0; i < h->tracks; i++){

		if(i > 1)
			continue;

		if(i > 0 && index>=2){// No extra credit soz
			song_tones[index][0] = end_tone;
			continue;
		}

		INIT_SONGINFO(t_info);
		TONE* track = song_tones[index];

		if(i > 0)
			t_info.tick_rate = s_info->tick_rate;

		address = parse_mtrk(address, &t_info, track); //Parse Track
		address = &address[4];

		if(i == 0){
			*s_info = t_info;
			s_info->tick_rate = s_info->tempo/h->division;
			s_info->finished = 1;
			song_infos[index] = *s_info; //Memoization for song info for quick retrieval
		} else {
			free(t_info.copyright);
			free(t_info.title);

		}
	}

	return *s_info;
}

/*
 * Parses Through Meta Events for Title/Track Name, Copyright, and Tempo
 */
uint8_t* parse_meta_event(uint8_t* buffer, SongInfo* s_info) {

	//Skip 00 Delta Time
	buffer++;
	uint8_t meta_event_type = buffer[1];
	uint32_t meta_event_length;
	buffer += 2;

	//Get Meta Event Length
	buffer += get_delay(buffer, &meta_event_length);

	// Sequence/Track Name
	if (meta_event_type == 0x03) {
	    char *title = (char *)malloc(meta_event_length + 1);
	    memcpy(title, buffer, meta_event_length);
		title[meta_event_length] = '\0';

		s_info->title = title;
	}

	// Copyright Notice
	else if (meta_event_type == 0x02) {
		char *cp = (char *)malloc(meta_event_length + 1);
        memcpy(cp, buffer, meta_event_length);
        cp[meta_event_length] = '\0';

		s_info->copyright = cp;
	}

	// Tempo
	else if (meta_event_type == 0x51) {
		uint24_t* tempo = (uint24_t*)convert_to_uint24(buffer);

		s_info->tempo = (unsigned int)tempo;
	}

	buffer += meta_event_length;

	return buffer;
}

/*
 * Parses through Tracks for note events
 */
uint8_t* parse_mtrk (uint8_t* buffer, SongInfo* s_info, TONE* track){
	int index = 0;

	//Get Track Length and End Point of track
	uint32_t mtrk_length = convert_to_uint32(buffer + 4);
	uint8_t * stop = buffer + mtrk_length;
	buffer += 8;

	while (buffer < stop) {
		uint32_t delta_time;
		uint8_t results[5] = {0};

		//Check if is a Meta-Event
		if (buffer[1] == 0xFF) {
			buffer = parse_meta_event(buffer, s_info);
		}

		//Else, Parse the midi event
		else {
			buffer = parse_midi_event(buffer, &delta_time, results);

		    uint8_t event_type = results[0];

		    //Check if Event Type is Note On or Note Off
			if(event_type == 0b1000 || event_type == 0b1001 ){

				uint8_t is_playing = results[2];
				uint8_t note = results[3];
				uint8_t velocity = results[4];

				int freq = hertz_to_duration(get_note_hz(note));

				TONE this = {
					note,
					velocity = is_playing * velocity, //If note is not playing, velocity = 0
					(float)(delta_time * s_info->tick_rate / 10),
					freq
				};

				//Save the note data into the next spot in the note sequence
				track[index] = this;
				index++;
			}

		    buffer++;
		}
	}

	//Mark the end of the song in the sequence
	track[index] = end_tone;

	return buffer;
}

/*
 * The function should place the calculated delay into the delay variable and
 * return the number of bytes that were used to calculate the delay.
 */

uint8_t get_delay (uint8_t* buffer, uint32_t* delay){
	*delay = *buffer & 0x7f; //mask off first bit

	uint8_t bytes = 1;

	while(*buffer >> 7){
		bytes++;
		buffer++;
		*delay = ( *delay << 8 | ( *buffer & 0x7f ) << 1 ) >> 1;
	}

	return bytes;
}

/*
 * Parses the midi event after a delay for the event type and channel effected
 */

uint8_t* get_midi_event(uint8_t* buffer, uint8_t bytes, uint8_t* event, uint8_t* channel){
	for(int i = 0; i < bytes; i++){
		buffer++;
	}

	*event = *buffer>>4;
	*channel = *buffer&0x0F;

	buffer++;

	return buffer;
}


/*
 * Parses midi event buffer for values of note and velocity
 */

uint8_t* parse_midi_event(uint8_t* buffer, uint32_t* delay, uint8_t results[]){
	uint8_t event_type = 0;
	uint8_t channel_no = 0;

	uint8_t* address = get_midi_event(buffer, get_delay(buffer, delay), &event_type, &channel_no);

	results[0] = event_type;
	results[1] = channel_no;
	if(event_type == 0b1000 || event_type == 0b1001 ){ // Note On/Off
		uint8_t note = *address & 0x7f;
		address++;
		uint8_t velocity = *address & 0x7f;
		results[2] = event_type == 0b1001; //if note is playing
		results[3] = note;
		results[4] = velocity;
		return address;
	} else {
		for(int i = 0; i < !(event_type == 0b1100 || event_type == 0b1101); i++){
			address++;
		}
		return address;
	}
	return 0;
}




