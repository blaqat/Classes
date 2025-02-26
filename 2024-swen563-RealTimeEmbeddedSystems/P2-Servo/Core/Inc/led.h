#ifndef __STM32L476G_DISCOVERY_LED_H
#define __STM32L476G_DISCOVERY_LED_H

#include "main.h"
#include "servo.h"
#include "stm32l4xx_hal.h"

void LED_Init(uint32_t pin);
void LED_On(uint16_t pin);
void LED_Off(uint16_t pin);
void LED_Toggle(uint32_t pin);
void LED_Set(uint16_t pin, uint32_t state);
void LED_handler(servo *servo);

#endif /* __STM32L476G_DISCOVERY_LED_H */
