/* USER CODE BEGIN Header */
/**
 ******************************************************************************
 * File Name          : freertos.c
 * Description        : Code for freertos applications
 ******************************************************************************
 * @attention
 *
 * Copyright (c) 2024 STMicroelectronics.
 * All rights reserved.
 *
 * This software is licensed under terms that can be found in the LICENSE file
 * in the root directory of this software component.
 * If no LICENSE file comes with this software, it is provided AS-IS.
 *
 ******************************************************************************
 */
/* USER CODE END Header */

#include "adc.h"
#include "cmsis_os2.h"
#include "commands.h"
#include "dac.h"
#include "queue.h"
#include "utils.h"

// Creates a new task with the given name, function, handler, priority, and
// error handler
#define NEW_TASK(name, func, handler, priority, error_handler)                 \
  if (xTaskCreate(func, name, 256 * 4, NULL, priority, handler) != pdPASS) {   \
    error_handler;                                                             \
  }

TaskHandle_t recieve_task_handler;
TaskHandle_t command_processing_task_handler;
TaskHandle_t dac_chn1_task_handler;
TaskHandle_t dac_chn2_task_handler;
TaskHandle_t adc_task_handler;

void command_processing_task(void *x);
void DAC_ch1_task(void *x);
void DAC_ch2_task(void *x);
void ADC_task(void *x);

void MX_FREERTOS_Init(void); /* (MISRA C 2004 rule 8.1) */

/**
 * @brief  FreeRTOS initialization
 * @param  None
 * @retval None
 */
void MX_FREERTOS_Init(void) {
  command_init();
  NEW_TASK("CommandProcessingTask", command_processing_task,
           &command_processing_task_handler, osPriorityNormal,
           error("Failed to create command processing task"));

  NEW_TASK("DACChannel1Task", DAC_ch1_task, &dac_chn1_task_handler,
           osPriorityLow, error("Failed to create DAC channel 1 task"));

  NEW_TASK("DACChannel2Task", DAC_ch2_task, &dac_chn2_task_handler,
           osPriorityLow, error("Failed to create DAC channel 2 task"));

  NEW_TASK("ADCTask", ADC_task, &adc_task_handler, osPriorityLow,
           error("Failed to create ADC task"));
}
