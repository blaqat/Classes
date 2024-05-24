/*
 * project.c
 *
 *  Created on: Jan 24, 2023
 *      Author: Aiden Green
 */

#include "project.h"
#include <stdio.h>
#include "UART.h"
#include "printf.h"
#include "string.h"
#include <stdint.h>
#include "LED.h"
#include "demo.h"
#include "systick.h"
#include "commands.h"
#include "interpreter.h"
#include "song.h"
#include "GPIO.h"

int i = 0;
uint8_t paused = 0;
uint8_t ti_mode = 1;
uint8_t switching_mode = 0;
uint8_t held = 0;

void switch_mode(){ //Switch mode between Local and Remote
	if(ti_mode){
		printlf("\n\r***REMOTE LED CONTROL MENU***");
		run_command("HELP", &paused);
		printlf("\n\r***REMOTE MODE ACTIVE***");
		reset_sent_text();
		printf("\n\r>> ");
	} else {
		printlf("\n\r***MANUAL OVERRIDE MODE ACTIVE***");
	}
	set_reading(ti_mode);
	set_reading_buttons(ti_mode);
}

void run () {
	char* cmd = get_sent_text();
	LED_Init(PROJECT_LED);
	init_systick();
	init_songs();
	switch_mode(); //Prints initial mode
	DAC_Init();
	DAC_Start();
	GPIO_Buttons_Down buttons;

	while (1) {
		//Plays the songs on the buzzer
		run_song();

		buttons = get_buttons();

		//Check if Mode Is Being Switched
		if(buttons.blue_down)
			switching_mode = 1;
		else if(switching_mode){
			ti_mode = !ti_mode;
			switching_mode = 0;
			switch_mode();
		}

		//Timer that Runs Every Second
		if(get_second_elapsed()){
			//Resets second timer
			reset_second_elapsed();

			//Flashes LED if paused
			if(paused == 1) LED_Toggle(PROJECT_LED);

			//Local Mode Commands
			if(!ti_mode && !buttons.switch_down){
				reset_pressed();

				//LONG PRESS | STOP PLAYBACK
				if(held){
					held = 0;
					run_command("STOP", &paused);
				}

				//DOUBLE PRESS | NEXT SONG
				else if(buttons.switch_count >= 2) {
					run_command("NEXT", &paused);
				}

				//SINGLE PRESS | TOGGLE PLAY
				else if(buttons.switch_count > 0) {
					run_command("TOGGLE", &paused);
				}

				reset_duration();
			}

		}

		//Remote Mode Commands
		if(ti_mode && get_has_text()){
			uint8_t keep = ti_mode;
			run_command(cmd, &paused);
			reset_sent_text();
			printf(">> ");
			ti_mode = keep;
		}

		// Local Mode Constant Handler
		else if (!ti_mode){
			//Starts counting how long button down
			if(buttons.switch_down) {
				start_duration();
			}

			//Checks if button is released over a debouncer
			else if(!buttons.switch_down && counting_ended(2)){
				uint32_t held_duration = get_duration();

				//If button is held over 1 second,
				if(held_duration >= 100000){
					held = 1;
				} else if (held_duration > 0) {
					held = 0;
				}

				//Resets the held button counter
				reset_duration();
			}
		}

	}
}

