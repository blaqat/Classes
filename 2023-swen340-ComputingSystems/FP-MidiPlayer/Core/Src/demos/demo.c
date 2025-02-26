/*
 * demo.c
 *
 *  Created on: Feb 14, 2021
 *      Author: larry kiser
 *  Updated on: Sept 6, 2021
 *      Author: Mitesh Parikh
 */

#include <stdio.h>
#include <string.h>

// Custom Include files
#include "LED.h"
#include "UART.h"
#include "demo.h"
#include "printf.h"
#include "stm32l4xx.h"
#include "systick.h"
#include "song.h"
#include "interpreter.h"
#include "little_endian.h"
#include "GPIO.h"
#include "tone.h"

// This function is to create a delay by consuming CPU cycle on counter
static void delay_loop( int value )
{
	// spin loop consuming CPU to spend time.
	for (int i = 0; i < value; i++)
		;
}

// This function is to print counter on UART port and toggle LED
static void demo_of_UART_print(int counter){
	int n ;
	uint8_t buffer[BUFFER_SIZE];

	n = sprintf((char *)buffer, "counter = %d\r\n", counter);
	USART_Write(USART2, buffer, n);

	delay_loop( 8000000 ) ;	// comment this out when you are ready to test delay_systick
	// delay_systick() ;	// enable this when you are ready to test

	// Toggle LED
	LED_Toggle(LED_PIN);
}

void timer_demo () {
	printlf ("Timer Demo");
    // Initialize the SysTick timer
    init_systick();
    printlf ("SysTick Initialized");

    int current_time = 0;
    uint8_t hours;
    uint8_t minutes;
    uint8_t seconds;
    uint32_t clock;

    while (1) {
        // Count for one second
        clock = (uint32_t)tick();

        if(clock == 10){
        	current_time++;
        	// Print the current time (clock format)
			seconds = current_time % 60;
			minutes = (current_time / 60) % 60;
			hours = current_time / 3600;
			printlf ("%03d:%02d:%02d", hours, minutes, seconds);
        }
    }
}

void header_demo () {
	printlf ("Header Demo");
	song song = get_song(0);
	Header* song_header = midi_header(song);

	printlf("Chunk Type: %s", (song_header->chunk_type));
	printlf("Len: %d", (song_header->len));
	printlf("Format: %d", (song_header->format));
	printlf("Tracks: %d", (song_header->tracks));
	printlf("Division: %d", (song_header->division));

}


void delay_demo() {
	printlf ("Delay Demo From Activity 17");
	unsigned char act17data[] = {
			0x3B, 0x80, 0x27, 0x00,  //59
			0x83, 0x18, 0x91, 0x54, 0x64, //408
			0xC5, 0x92, 0x74, 0xE3, 0x74, 0x12, //1132916
			0x90, 0x82, 0x93, 0x64, 0xD3, 0x89 //33589732
	};

	uint8_t bytes = 0;
	uint32_t delay = 0;

	bytes = get_delay(&act17data[0], &delay);
	printlf("1: 3B 80 27 00\n\r\tExpected: %d\n\r\tActual: %d\n\r\tBytes: %d",  59, delay,bytes);

	bytes = get_delay(&act17data[4], &delay);
	printlf("2: 83 18 91 54 64\n\r\tExpected: %d\n\r\tActual: %d\n\r\tBytes: %d", 408, delay, bytes);

	bytes= get_delay(&act17data[9], &delay);
	printlf("3: C5 92 74 E3 74 12\n\r\tExpected: %d\n\r\tActual: %d\n\r\tBytes: %d", 1132916, delay, bytes);

	bytes= get_delay(&act17data[15], &delay);
	printlf("4: 90 82 93 64 D3 89\r\n\tExpected: %d\r\n\tActual: %d\n\r\tBytes: %d", 33589732, delay, bytes);
}

void act7_parsing() {
	printlf ("Activity 17");
	unsigned char act17data[] = {
			0x3B, 0x80, 0x27, 0x00,  //59
			0x83, 0x18, 0x91, 0x54, 0x64, //408
			0xC5, 0x92, 0x74, 0xE3, 0x74, 0x12, //1132916
			0x90, 0x82, 0x93, 0x64, 0xD3, 0x89, //33589732
			0xff,0x2f,0x00 // End of Track
	};

	uint8_t * address = &act17data[0];
	uint8_t results[5] = {};
	uint32_t delay = 0;

	while(convert_to_uint24(address) != 0xFF2F00){

		address = (uint8_t*)parse_midi_event(address, &delay, results);

		printlf("Delay: %d | Event: %d", delay, results[0]-7);

		if(results[0] == 0b1000 || results[0] == 0b1001 )
			printlf("\tPlaying: %d | Note: %d | Velocity: %d", results[2], results[3], results[4]);

		address++;
	}

}

void play_demo(){
	init_systick();
	init_songs();


	play_track(0);
}

void play_demo_no_int(){
	init_systick();
	init_songs();
	int is_playing = 1;
	int state = 0;
	int song = 1;

	while(1){

		//printlf("%d %d", last, state);

		if(state == 1){
			is_playing = 0;
		}
		if(is_playing){
			state = play_track_no_int(song, 0, 's');
		}
	}

}

void run_demo(){
	
	int counter = 0;
	
	// Run a loop to print counter value on UART port
	while (1)
	{
		//demo_of_printf_scanf();
		demo_of_UART_print(counter);
		counter++;
	}
}

