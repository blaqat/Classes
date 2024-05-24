/*
 * systick.c
 *
 *  Created on: Feb 16, 2023
 *      Author: Aiden
 */

#include "systick.h"
#include <stdint.h>

static systick* SYST = (systick*)0xe000e010;
static uint8_t size = 3;
static uint32_t counters[3];
static uint8_t counters_on[3];
static float counters_max[3];
uint32_t clock = 0;

void init_systick()
{

	// Use the SysTick global structure pointer to do the following in this
	// exact order with separate lines for each step:
	//
	// Disable SysTick by clearing the CTRL (CSR) register.
	SYST->CSR &= ~1;

	// Set the LOAD (RVR) to 8 million to give us a 100 milliseconds timer.

	//SYST->RVR = 10000;
	SYST->RVR = 800;

	// Set the clock source bit in the CTRL (CSR) to the internal clock.
	SYST->CSR |= 0x01 << 0x02;

	// Set the enable bit in the CTRL (CSR) to start the timer.
	SYST->CSR |= 0x01;

	// Set the interrupt bit;
	SYST->CSR |= 0x01 << 0x01;

	counter_init(0, reset_value, 1); //init one second counter
	counter_init(1, 0, 0); //init button down counter
}

void delay_systick()
{
	// Using the SysTick global structure pointer do the following:
	// Create a for loop that loops 10 times
	// Inside that for loop check the COUNTFLAG bit in the CTRL (CSR)
	// register in a loop. When that bit is set exit this inner loop
	// to do another pass in the outer loop of 10.
}


systick* get_systick()
{
	return SYST;
}

uint8_t get_flag()
{
	return (SYST->CSR & (1 << 16)) >> 16;
}

void SysTick_Handler() {
	for(int i = 0; i < size; i++){
		if(counters_on[i]) //Increment all playing counters
			counters[i]++;

		//If counter has reached its max point, reset and set the counter flag to end
		//count >= 80000000 * 1 / SYST->RVR = 1 second
		//count >= 8000000 * 10 / SYST->RVR = 1 second
		//count >= 80000 * 1000 / SYST->RVR = 1 second

		// 1 second = 1000 milli seconds
		// 8000000 (8mil) = 100 millisecond timer = .1 second timer
		// 80000 (80th) = 1 millisecond timer = .001 second timer
		if(counters_max[i] != 0 && counters[i] >= (float)(8000 * counters_max[i] / SYST->RVR)){
			counters_on[i] = 2;
			counter_reset(i);
		}
	}
	clock++;
}

uint32_t* tick() {
	return &clock;
}

uint8_t get_second_elapsed() {
	return counting_ended(0);
}

void reset_second_elapsed() {
	counting_start(0);
}

//get system clock elapsed time
uint32_t  get_duration() {
	return counter_get(1);
}

//reset elapsed time
void reset_duration() {
	counter_reset(1);
	counting_stop(1);
}

//start system clock
void start_duration() {
	counting_start(1);
}

uint32_t counter_get(int id){
	return counters[id];
}

void counter_reset(int id){
	counters[id] = 0;
}

//get if counter is counting
uint8_t counting_get(int id){
	return counters_on[id];
}

//start counter counting
void counting_start(int id){
	counters_on[id] = 1;
}

//stop counter counting
void counting_stop(uint8_t id){
	counters_on[id] = 0;
}

//get if counter has hit max time duration
int counting_ended(uint8_t id){
	return counters_on[id] == 2;
}

//create new counter
void counter_init(uint8_t id, float max, uint8_t start_immediately){
	mili_counter_init(id, max*1000, start_immediately);
}

void mili_counter_init(uint8_t id, float max, uint8_t start_immediately){
	counters[id] = 0;
	counters_max[id] = max;
	counters_on[id] = start_immediately;
}


