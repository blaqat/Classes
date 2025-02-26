/*
 * systick.h
 *
 *  Created on: Jan 8, 2023
 *      Author: bruce
 */

#ifndef INC_SYSTICK_H_
#define INC_SYSTICK_H_
#include <stdint.h>

typedef struct systick {

	uint32_t CSR; //Control and Status Register
	uint32_t RVR; //Reload Value Register
	uint32_t CVR; //Current Value Register
	uint32_t CALIB; //Calibration Value Register

} systick;

static const uint8_t reset_value = 10;

// This function is to Initialize SysTick registers
void init_systick();

// This function is to create delay using SysTick timer counter
void delay_systick();

systick* get_systick();

uint8_t get_flag();

uint32_t* tick();

uint8_t get_second_elapsed() ;

void reset_second_elapsed() ;

uint32_t  get_duration() ;

void reset_duration() ;

void start_duration() ;

uint32_t counter_get(int id);

void counter_reset(int id);

uint8_t counting_get(int id);

void counting_start(int id);

void counting_stop(uint8_t id);

int counting_ended(uint8_t id);
void counter_init(uint8_t id, float max, uint8_t start_immediately);
void mili_counter_init(uint8_t id, float max, uint8_t start_immediately);

#endif /* INC_SYSTICK_H_ */
