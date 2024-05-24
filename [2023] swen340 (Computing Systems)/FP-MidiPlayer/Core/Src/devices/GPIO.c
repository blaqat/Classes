/*
 * gpio.c
 *
 *  Created on: Nov 4, 2021
 *      Author: Mitesh Parikh
 */


/* Includes ------------------------------------------------------------------*/
#include "GPIO.h"
#include <stdbool.h>
#include "stm32l4xx.h"
#include "printf.h"
#include "LED.h"

// External Global Variables that we will need access to


/*----------------------------------------------------------------------------*/
/* Configure GPIO                                                             */
/*----------------------------------------------------------------------------*/
 static uint8_t switch_down = 0;
 static uint8_t blue_down = 0;
 static uint32_t switch_count = 0;
 static uint8_t is_reading_buttons = 0;
void GPIO_Init(void)
{
	GPIO_InitTypeDef GPIO_InitStruct = {0};

	/* GPIO Ports Clock Enable */
	__HAL_RCC_GPIOC_CLK_ENABLE();
	__HAL_RCC_GPIOH_CLK_ENABLE();
	__HAL_RCC_GPIOA_CLK_ENABLE();
	__HAL_RCC_GPIOB_CLK_ENABLE();

	/*Configure GPIO pin : PtPin */
	GPIO_InitStruct.Pin = B1_Pin;
	GPIO_InitStruct.Mode = GPIO_MODE_IT_RISING_FALLING;
	GPIO_InitStruct.Pull = GPIO_PULLDOWN;
	HAL_GPIO_Init(B1_GPIO_Port, &GPIO_InitStruct);

	GPIO_InitStruct.Pin = S1_Pin;
	GPIO_InitStruct.Mode = GPIO_MODE_IT_RISING_FALLING;
	GPIO_InitStruct.Pull = GPIO_PULLDOWN;
	HAL_GPIO_Init(S1_GPIO_Port, &GPIO_InitStruct);

	/* EXTI15_10_IRQn interrupt init*/
	// Note you will have to add EXTI15_10_IRQn Interrupt handler function as well
	// This is the interrupt handler for the blue button
	HAL_NVIC_EnableIRQ(EXTI15_10_IRQn);
	//HAL_GPIO_EXTI_IRQHandler(B1_Pin);


	/* EXTI9_15_IRQn interrupt init*/
	// Note you will have to add EXTI9_15_IRQn Interrupt handler function as well
	// This is the interrupt handler for the external buttons (S1)
	HAL_NVIC_EnableIRQ(EXTI9_5_IRQn);
	//HAL_GPIO_EXTI_IRQHandler(S1_Pin);

	counter_init(2, 1, 0);
}

// Switch
void EXTI9_5_IRQHandler(){
	if(is_reading_buttons){ //Make sure buttons should be read
		if(HAL_GPIO_ReadPin(S1_GPIO_Port, S1_Pin) && counting_get(2) != 1){ //If has not been down in 1/10 seconds
			counter_reset(2);
			counting_start(2);
			if(!switch_down)
				switch_count++; //Increment button pressed count
			switch_down = 1;

		} else {
			switch_down = 0;
		}
	}

	HAL_GPIO_EXTI_IRQHandler(S1_Pin);
}

//Blue Button
void EXTI15_10_IRQHandler(){
	if(HAL_GPIO_ReadPin(B1_GPIO_Port, B1_Pin)!=1){
		blue_down = 1;
	} else {
		blue_down = 0;
	}
	HAL_GPIO_EXTI_IRQHandler(B1_Pin);
}

GPIO_Buttons_Down get_buttons(){ //get snapshot of the button state
	GPIO_Buttons_Down down = {blue_down, switch_down, switch_count};
	return down;
}


void reset_pressed(){ //reset switch button down pressed count
	switch_count = 0;
}


void gpio_test(){
	//INITIALIZE PROJECT LED
	int PROJECT_LED = 7;
	LED_Init(PROJECT_LED);
	//INITIALIZE GPIO
	GPIO_Init();
	GPIO_Buttons_Down buttons;

	while(1){
		buttons = get_buttons();
		//TURN ON LED WHEN PRESSED
		if(buttons.blue_down || buttons.switch_down){
			LED_On(PROJECT_LED);
		} else {
			LED_Off(PROJECT_LED);
		}
	}
}

void set_reading_buttons(uint8_t val){
	is_reading_buttons = !val;
};
