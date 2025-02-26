/* USER CODE BEGIN Header */
/**
 ******************************************************************************
 * @file           : main.h
 * @brief          : Header for main.c file.
 *                   This file contains the common defines of the application.
 ******************************************************************************
 * @attention
 *
 * <h2><center>&copy; Copyright (c) 2021 STMicroelectronics.
 * All rights reserved.</center></h2>
 *
 * This software component is licensed by ST under BSD 3-Clause license,
 * the "License"; You may not use this file except in compliance with the
 * License. You may obtain a copy of the License at:
 *                        opensource.org/licenses/BSD-3-Clause
 *
 ******************************************************************************
 */
/* USER CODE END Header */

/* Define to prevent recursive inclusion -------------------------------------*/
#ifndef __MAIN_H
#define __MAIN_H

#ifdef __cplusplus
extern "C" {
#endif

/* Includes ------------------------------------------------------------------*/
// #include "stm32l4xx_hal.h"

/* Private includes ----------------------------------------------------------*/
/* USER CODE BEGIN Includes */

/* USER CODE END Includes */

/* Exported types ------------------------------------------------------------*/
/* USER CODE BEGIN ET */

/* USER CODE END ET */

/* Exported constants --------------------------------------------------------*/
/* USER CODE BEGIN EC */

/* USER CODE END EC */

/* Exported macro ------------------------------------------------------------*/
/* USER CODE BEGIN EM */
// Macros for loops
#define loop while (1) // Inspired by rust's loop keyword
#define MS_PASSED(ms)                                                          \
  (TIM_Loop_To(ms)) // if (repeats >= (10 * ms / PWM_PERIOD)) {
#define EVERY_MS(ms, body)                                                     \
  while (1) {                                                                  \
    if (MS_PASSED(ms)) {                                                       \
      body                                                                     \
    }                                                                          \
  }

#define EVERY_100MS(body) EVERY_MS(100, body)

// Termianl characters
#define CR(c) (c[*index - 1] == '\n')            // <CR>
#define EXIT(c) (to_lower(c[*index - 1]) == 'x') // 'X' or 'x'

// Servo states
#define LED_STATE (servo->led_state & 0b011)
#define STATUS_PAUSED (servo->status == status_paused)
#define STATUS_RUNNING (servo->status == status_running)
#define RECIPE_POSITION (servo->recipe_pos)
#define RECIPE_ERROR (servo->recipe_state == recipe_error)
#define RECIPE_RUNNING (servo->recipe_state == recipe_running)
#define RECIPE_READY (servo->recipe_state == recipe_ready)
#define RECIPE_WAITING (servo->recipe_state == recipe_waiting)
#define RECIPE_DONE (servo->recipe_state == recipe_complete)
#define MOTOR_MOVING (servo->motor_state == state_moving)
#define DELAY_REMAINING (servo->delay_rem)
#define LOOP_POSITION (servo->loop_pos)
#define LOOPS_REMAINING (servo->loop_rem != 0)
/* USER CODE END EM */

/* Exported functions prototypes ---------------------------------------------*/
void Error_Handler(void);

/* USER CODE BEGIN EFP */

/* USER CODE END EFP */

/* Private defines -----------------------------------------------------------*/
/* USER CODE BEGIN Private defines */
#define NOT !

#define LED1 GPIO_PIN_6
#define LED2 GPIO_PIN_7

// Configuration constants
#define PWM_PERIOD 200
#define PWM_PULSE 20 // 4 - 20
#define MIN_PWM 5
#define MAX_PWM 20
#define SERVO_DELAY 100
#define BUFFER_SIZE 4
#define OVERRIDE_PROMPT_CHAR "\r> " // <CR> and '>'

// Terminal Commands
#define BEGIN 'b'
#define PAUSE 'p'
#define CONT 'c'
#define MOV_R 'r'
#define MOV_L 'l'
#define NONE 'n'

// LED States
#define LED_RECIPE_RUNNING 0b01
#define LED_RECIPE_PAUSED 0b00
#define LED_COMMAND_ERROR 0b10
#define LED_NESTED_ERROR 0b11

// Recipe commands
#define MOV (0x20)
#define WAIT (0x40)
#define LOOP (0x80)
#define END_LOOP (0xA0)
#define RECIPE_END (0)
#define COMMAND_MSK (MOV | WAIT | LOOP | END_LOOP | RECIPE_END)
/* USER CODE END Private defines */

#ifdef __cplusplus
}
#endif

#endif /* __MAIN_H */

/************************ (C) COPYRIGHT STMicroelectronics *****END OF FILE****/
