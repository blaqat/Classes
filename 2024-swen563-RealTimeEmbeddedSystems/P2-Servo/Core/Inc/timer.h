/*
 * timer.h
 *
 *  Created on: Feb 26, 2024
 *      Author: Aiden Green
 */
#ifndef INC_TIMER_H_
#define INC_TIMER_H_
#include "main.h"
#include "stm32l4xx_hal.h"

// Initialization functions
void GPIO_AF_Init();
void TIM_Init();

// Operational functions
void TIM_Start();
void TIM_Stop();
void TIM_PWM_Start(uint32_t channel);
void TIM_PWM_Stop(uint32_t channel);
uint32_t TIM_PWM_Get(uint32_t channel);
void TIM_PWM_Set(uint32_t pwm, uint32_t channel);
_Bool TIM_Loop_To(uint32_t time_ms);
uint32_t TIM_Loops();
#endif /* INC_TIMER_H_ */
