/*
 * tone.c
 *
 *  Created on: May 14, 2022
 *      Author: bruce
 */


#include "tone.h"
#include "interpreter.h"
#include "systick.h"
#include <stdlib.h>
#include <stdio.h>

#define TICK_SPEED (100000)
static float notes[] = {
/* Octave -1 */			   8.18,    8.66,    9.18,    9.73,   10.30,   10.92,   11.56,   12.25,   12.98,   13.75,   14.57,   13.44,
/* Octave  0 */           16.35,   17.32,   18.35,   19.45,   20.60,   21.83,   23.12,   24.50,   25.96,   27.50,   29.14,   30.87,
/* Octave  1 */  	      32.70,   34.65,   36.71,   38.89,   41.20,   43.65,   46.25,   49.00,   51.91,   55.00,   58.27,   61.74,
/* Octave  2 */			  65.41,   69.30,   73.42,   77.78,   82.41,   87.31,   92.50,   98.00,  103.83,  110.00,  116.54,  123.47,
/* Octave  3 */			 130.81,  138.59,  146.83,  155.56,  164.81,  174.61,  185.00,  196.00,  207.65,  220.00,  233.08,  246.94,
/* Octave  4 */			 261.63,  277.18,  293.66,  311.13,  329.63,  349.23,  369.99,  392.00,  415.30,  440.00,  466.16,  493.88,
/* Octave  5 */          523.25,  554.37,  587.33,  622.25,  659.25,  698.46,  739.99,  783.99,  830.61,  880.00,  932.33,  987.77,
/* Octave  6 */         1046.50, 1108.73, 1174.66, 1244.51, 1318.51, 1396.91, 1479.98, 1567.98, 1661.22, 1760.00, 1864.66, 1975.53,
/* Octave  7 */         2093.00, 2217.46, 2349.32, 2489.02, 2637.02, 2793.83, 2959.96, 3135.96, 3322.44, 3520.00, 3729.31, 3951.07,
/* Octave  8 */         4186.01, 4434.92, 4698.63, 4978.03, 5274.04, 5587.65, 5919.91, 6271.93, 6644.88, 7040.00, 7458.62, 7902.13,
/* Octave  9 */         8372.02, 8869.84, 9397.26, 9956.06,10548.08,11175.30,11839.82,12543.86};


float get_note_hz(int note_number){
	return notes[note_number];
}

// Convert from hertz to ticks
float hertz_to_duration (float hertz) {
	uint32_t tick_speed = TICK_SPEED; // This is a hack, you'll fix it later
	if (hertz == 0)
		return 0;
	float duration = 1 / hertz;
	return (uint32_t) (duration * tick_speed);
}

// Plays a tone for duration
void play_tone(TONE tone, float duration){
	uint32_t* count = tick();
	//printlf("\tPlaying: %d | Note: %s | Velocity: %d", tone.velocity!=0, get_note_name(tone.note), tone.velocity);
	int half = tone.freq / 2;

	uint32_t start = *tick();
	//printlf("%d %d %d", *count, start, (*count - start) <= duration);

	while((*count - start) <= duration / 2){ //PLAY NOTE RAHH
		if ((*count % tone.freq) < half) {
			DAC_Set_Value (10 * tone.velocity);
		}
		else {
			DAC_Set_Value (0);
		}
	}
}

// Plays a whole track of song index
void play_track(int index){
	TONE* tone = get_track(index);
	int i = 0;
	DAC_Init();
	DAC_Start ();
	//printlf("-----------\n");

	while(tone[i+1].note != end_tone.note){
		//printlf("%d", tone[i].duration);
		play_tone(tone[i], tone[i+1].duration);
		i++;
	}
}


/*
 * Returns a standardized (0-1) equation for a waveform (type)
 * when given a counter and frequency
 */
float get_velocity_mult(char type, uint32_t x, int f, float a){
	float c = (x % f);

	if(type == 's') //Square Wave
		return 2 * c < f;

	else if(type == 'p') //Pulse Wave
		return c / a < f;

	else if(type == 'w') //Saw Wave
		return c / f;

	else if(type == 't') //Triangle Wave
		return (2 * c < f? (2 * c)/f : 2 - (2 * c)/f);

	return 0;
}


/*
 * Plays a tone once
 * (NO INTERRUPTION)
 */
void play_tone_no_int(TONE tone, char wave){
	uint32_t* count = tick();

	float mult = get_velocity_mult(wave, *count, tone.freq, .25);

	DAC_Set_Value(mult * 10 * tone.velocity);
}


/*
 * Plays the next sound in a track
 * (NO INTERRUPTION)
 */
int play_track_no_int(int track_index, int* from_begining, char wave){
	static int i = 1;
	static uint32_t start = 0;

	if(!*from_begining){
		i = 1;
		start = 0;
		*from_begining = 1;
	}

	if(start == 0)
		start = *tick();

	uint32_t* count = tick();
	TONE* tone = get_track(track_index);

	if(tone[i].note != end_tone.note){
		float duration = tone[i].duration;

		if( (*count - start) <= (duration) )
			play_tone_no_int(tone[i-1], wave);
		else {
			i++;
			start = 0;
			return 0; // Note has ended
		}

	} else {
		i = 1;
		return 1; // Song has ended
	}

	return 2; // Do nothing
}

