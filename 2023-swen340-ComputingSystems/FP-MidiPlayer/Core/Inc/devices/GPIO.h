/*
 * gpio.h
 *
 *  Created on: Nov 4, 2021
 *      Author: Mitesh Parikh
 */

#ifndef INC_GPIO_H_
#define INC_GPIO_H_

#include "main.h"
#include <stdint.h>
#include "systick.h"

#define S1_Pin GPIO_PIN_9
#define S1_GPIO_Port GPIOA

typedef struct {
	uint8_t blue_down;
	uint8_t switch_down;
	uint32_t switch_count;
} GPIO_Buttons_Down;

void GPIO_Init(void);
void gpio_test (void);
void EXTI9_5_IRQHandler();
void EXTI15_10_IRQHandler();
void set_reading_buttons(uint8_t val);

GPIO_Buttons_Down get_buttons();

void reset_pressed();


#endif /* INC_GPIO_H_ */
