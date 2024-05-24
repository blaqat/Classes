/*
 * shield.h
 *
 *  Created on: Mar 14, 2024
 *      Author: Aiden Green
 */

#ifndef INC_BUTTON_H_
#define INC_BUTTON_H_

#include "main.h"
#include "stm32l4xx_hal.h"

/**
 * @brief Initialize the buttons on the shield
 */
void SHLD_Button_Init(GPIO_TypeDef *GPIO_port, uint32_t GPIO_pins);

/**
 * @brief Check if a button is pressed
 */
_Bool SHLD_Button_IsPressed(GPIO_TypeDef *GPIO_port, uint16_t GPIO_pin);

/**
 * @brief Get the state of a button
 */
GPIO_PinState SHLD_Button_GetState(GPIO_TypeDef *GPIO_port, uint16_t GPIO_pin);

/**
 * @brief Display an integer on the 7-segment display
 */
void SHLD_Display_Int(uint16_t val);

#endif /* INC_BUTTON_H_ */
