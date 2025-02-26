/*
 * timer.h
 *
 *  Created on: Feb 26, 2024
 *      Author: Aiden Green
 */
#ifndef INC_TIMER_H_
#define INC_TIMER_H_
#include "stm32l4xx_hal.h"

// Initialization functions
void TIM2_Init();

// Operational functions
void TIM_Start();
void TIM_Stop();
_Bool TIM_Loop_To(uint32_t time_ms);
uint32_t TIM_Loops();
uint32_t TIM_Count();
uint32_t TIM_Elapsed();
#endif /* INC_TIMER_H_ */
