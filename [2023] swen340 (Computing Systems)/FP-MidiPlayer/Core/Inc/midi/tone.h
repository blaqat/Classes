/*
 * tone.h
 *
 *  Created on: May 14, 2022
 *      Author: bruce
 */

#ifndef INC_TONE_H_
#define INC_TONE_H_

#include "dac.h"

typedef struct {
	uint32_t duration;
	uint16_t power;
} tone_info;

typedef struct {
	uint8_t note;
	uint8_t velocity;
	float duration;
	int freq;
} TONE;

static const TONE end_tone = {0, 0, 0, 0};

/**
 * Utility function that converts a frequency into a duration.
 */
float hertz_to_duration (float hertz);

float get_note_hz(int note_number);
void play_track(int index);
int play_track_no_int(int track_index, int* from_begining, char wave);

#endif /* INC_TONE_H_ */
