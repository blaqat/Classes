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

#include "FreeRTOS.h"
#include "bank.h"
#include "cmsis_os2.h"
#include "display.h"
#include "task.h"
#include "utils.h"

#define NEW_TASK(name, func, arg, handler, error_handler)                      \
  if (xTaskCreate(func, name, 128, arg, osPriorityHigh, handler) != pdPASS) {  \
    error_handler;                                                             \
  }

TaskHandle_t bank_task_handler;
TaskHandle_t customer_queue_task_handler;
TaskHandle_t display_task_handler;

void bank_task(void *x);
void customer_task(void *x);
void teller_task(void *x);

void MX_FREERTOS_Init(void); /* (MISRA C 2004 rule 8.1) */

/**
 * @brief  FreeRTOS initialization
 * @param  None
 * @retval None
 */
void MX_FREERTOS_Init(void) {

  const xSemaphoreHandle bank_semaphore = xSemaphoreCreateMutex();

  bank *manager = bank_init();
  manager->bank_handler = bank_semaphore;

  NEW_TASK("BankTask", bank_task, manager, &bank_task_handler,
           error("Failed to create bank task"));

  NEW_TASK("QueueTask", customer_task, manager, &customer_queue_task_handler,
           error("Failed to create customer queue task"));

  NEW_TASK("DisplayTask", display_task, manager, &display_task_handler,
           error("Failed to create display task"))

  for (int i = 0; i < NUM_TELLERS; i++) {
    teller *tllr = manager->tellers[i];
    TaskHandle_t teller_task_handler;
    NEW_TASK(format("TellerTask%d", i + 1), teller_task, tllr,
             &teller_task_handler,
             error(format("Failed to create teller %d task", i + 1)));
  }
}
