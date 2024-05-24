/*
 * interpreter.h
 *
 *  Created on: Marsh 23, 2023
 *      Author: Aiden Green
 *
 */

#ifndef INC_INTERPRETER_H_
#define INC_INTERPRETER_H_

#include <stdint.h>
#include "song.h"
#include "tone.h"

typedef struct { unsigned int : 24; } uint24_t;

typedef struct header {
    char chunk_type[4];
    uint32_t len;
    uint16_t format;
    uint16_t tracks;
    uint16_t division;
} Header;

typedef struct  {
	char* title;
	char* copyright;
	unsigned int tempo;
	uint8_t finished;
	unsigned int tick_rate;
	TONE* track;
} SongInfo;
#define INIT_SONGINFO(X) SongInfo X = {.title = "none", .copyright ="none", .tempo = 0, .finished = 0}



Header* midi_header (song s);
SongInfo get_song_info (int index);
TONE* get_track(int index);
uint8_t get_delay (uint8_t* buffer, uint32_t*delay);
uint8_t* get_midi_event(uint8_t* buffer, uint8_t bytes, uint8_t* event, uint8_t* channel);
uint8_t* parse_midi_event(uint8_t* buffer, uint32_t* delay, uint8_t results[]);
uint8_t* parse_mtrk (uint8_t* buffer, SongInfo* s_info, TONE* track);
uint8_t* parse_meta_event(uint8_t* buffer, SongInfo* s_info);


#endif /* INC_INTERPRETER_H_ */
