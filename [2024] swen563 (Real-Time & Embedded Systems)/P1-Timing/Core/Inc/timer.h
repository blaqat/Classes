#ifndef INC_TIMER_H_
#define INC_TIMER_H_
#include <stdint.h>

// Initialization functions
void PA0_Init();
void TIM_Init();

// Operational functions
void start_timer();
void stop_timer();
_Bool has_captured();
uint32_t get_captured();
uint32_t get_status();
uint32_t get_time();

#endif /* INC_TIMER_H_ */
