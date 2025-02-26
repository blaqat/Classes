/*
 * adc_example.c
 *
 *  Created on: Apr 13, 2022
 *      Author: bruce
 */

#include "adc.h"
#include "dac.h"
#include "adc_demo.h"
#include "systick.h"
#include "tone.h"
#include "stm32l476xx.h"
#include "LED.h"
#include "printf.h"

uint8_t one_second_elapsed = FALSE;
uint32_t counter = 0;


typedef struct {
	float duration;
	char note;
	int octave;
} beep;

float beat_duration = 359 * 8; //167 bpm (8 beats per bar)
static int sequence_length = 14;
static int division = 4;
static int loudness = 3; //gain
static int repeat = 0;

static beep sequence[14] = { //its a me
		{1, 'E', 5}, {1, '-', 0},
		{1, 'E', 5}, {3, '-', 0},
		{1, 'E', 5}, {3, '-', 0},
		{1, 'C', 5}, {1, '-', 0},
		{1, 'E', 5}, {3, '-', 0},
		{1, 'G', 5}, {7, '-', 0},
		{1, 'G', 4}, {1+8*2*4, '-', 0}
};

static char note_names[] = {
		'C', 'd', 'D', 'e', 'E', 'F', 'g', 'G', 'a', 'A', 'b', 'B'
};

//Gets the note hertz from the note name

float get_note(char note_name, int octave){
	octave++;
	int note_index = 0;
	while(note_names[note_index] != note_name) note_index++;

	return get_note_hz(octave * 12 + note_index);
}


void Error_Handler () {
	// Add error handler here if desired
}

void run_adc_dac_demo()
{
	uint16_t counter = 0;

	init_systick();
	DAC_Init ();
	ADC_Init ();
	LED_Init(7);
	DAC_Start ();
	repeat++;

	while(repeat--){
		printf ("Start!\n\r"); //PLAY SONG YIPPEEE

		for(int i = 0; i < sequence_length; i++){
			beep current = sequence[i];
			float duration = beat_duration * (float)(current.duration / division);
			mili_counter_init(4, duration, 0); //initialize timer for note duration

			float note = get_note(current.note, current.octave);
			float freq = hertz_to_duration(note);
			mili_counter_init(3, freq, 1); //initialize timer for frequency


			counting_start(4);
			printf("Play Note: %c %d\n\r", current.note, current.octave);

			while(!counting_ended(4)){ //PLAY NOTE RAHH
				if(counting_ended(3)){
					DAC_Set_Value (0x100*counter*loudness);
					counter=!counter;
					counting_start(3);
				}
			}
		}
	}
	printf("Stopping!");
}

void class_solution(){
	init_systick();
	DAC_Init();
	DAC_Start ();
	repeat++;
	uint32_t* count = tick();
	while(repeat--){
		printf ("Start!\n\r"); //PLAY SONG YIPPEEE

		for(int i = 0; i < sequence_length; i++){
			beep current = sequence[i];

			float duration = beat_duration * (float)(current.duration / division);
			mili_counter_init(2, duration, 0); //initialize timer for note duration

			float note = get_note(current.note, current.octave);
			int freq = hertz_to_duration(note);
			int half = freq / 2;

			uint32_t start = *tick();
			printf("Play Note: %c %d\n\r", current.note, current.octave);

			while((*count - start) <= 10 * duration){ //PLAY NOTE RAHH
				if ((*count % freq) < half) {
					DAC_Set_Value (2048 * loudness);
				}
				else {
					DAC_Set_Value (0);
				}
			}
		}
	}
}
