#ifndef __NUCLEO476_LED_H
#define __NUCLEO476_LED_H

#include "stm32l476xx.h"

#define LED_PIN 5

void LED_Init(uint16_t led_pin);

void LED_Off(uint16_t led_pin);
void LED_On(uint16_t led_pin);
void LED_Toggle(uint16_t led_pin);

#endif /* __NUCLEO476_LED_H */
