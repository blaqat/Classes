/*
 * clock.h
 *
 *  Created on: Feb 26, 2024
 *      Author: Aiden Green
 */

#ifndef INC_CLOCK_H_
#define INC_CLOCK_H_

#include "stm32l4xx_hal.h"

// Default clock settings generated by STM32CubeMX
void clock_init(void);

// Custom clock settings
void SysClock_Init(void);

#endif /* INC_CLOCK_H_ */