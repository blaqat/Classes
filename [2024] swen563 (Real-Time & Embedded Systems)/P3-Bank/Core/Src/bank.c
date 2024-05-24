/**
 * @file bank.c
 * @brief This file contains the implementation of the bank manager and related
 * functions.
 ******************************************************************************
 * The bank manager is responsible for opening and closing the bank, managing
 * tellers, and updating the display with the current queue size. It also keeps
 * track of global metrics such as the average queue time, average service time,
 * and maximum queue depth.
 *
 *  Created on: Mar 16, 2024
 *      Author: Aiden Green
 */

#include "bank.h"
#include "display.h"
#include "main.h"
#include "timer.h"
#include "utils.h"

metrics GLOBAL_METRICS = {0};
SemaphoreHandle_t metrics_handler;
SemaphoreHandle_t completion_handler;
QueueHandle_t customer_queue;

/**
 * @brief Initialize global metrics to zero
 * @return metrics The initialized metrics structure
 */
metrics metrics_init(void) {
  metrics *m = INIT_STRUCT(metrics);
  m->avg_queue_time = 0;
  m->avg_serve_time = 0;
  m->avg_wait_time = 0;
  m->customers_served_cnt = 0;
  m->idle_hook_cnt = 0;
  m->max_queue_depth = 0;
  m->max_queue_time = 0;
  m->max_serve_time = 0;
  m->max_wait_time = 0;
  return *m;
}

/**
 * @brief Initialize the bank manager and tellers
 * @return bank* The initialized bank structure
 */
bank *bank_init(void) {
  GLOBAL_METRICS = metrics_init();
  metrics_handler = xSemaphoreCreateMutex();
  completion_handler = xSemaphoreCreateMutex();

  bank *manager = INIT_STRUCT(bank);
  customer_queue = xQueueCreate(MAX_CUSTOMERS, sizeof(customer *));
  manager->status = BANK_UNOPENED;
  manager->last_customer_arrival = 0;
  manager->open_time = 0;
  manager->close_time = 0;

  for (int i = 0; i < NUM_TELLERS; i++) {
    teller *t = teller_init();
    t->is_working = TRUE;
    manager->tellers[i] = t;
  }

  manager->tellers[0]->GPIO_pin = Teller1_Pin;
  manager->tellers[1]->GPIO_pin = Teller2_Pin;
  manager->tellers[2]->GPIO_pin = Teller3_Pin;

  return manager;
}

/**
 * @brief Main task for the bank
 * @param x The bank structure passed as a void pointer
 */
void bank_task(void *x) {

  bank *manager = (bank *)x;
  xSemaphoreTake(manager->bank_handler, 0);
  manager->open_time = TIM_Elapsed();
  manager->close_time = manager->open_time + TIME_IN_HOURS_OPEN;
  manager->status = BANK_OPEN;

  print("Bank is open: ");

  while (manager->status == BANK_OPEN) {
    // Update digital display with queue size
    // fprintln("Queue size: %d",
    // uxQueueMessagesWaiting(manager->customer_queue));
    update_display(manager);

    // Check if bank is closed
    if (manager->close_time > TIM_Elapsed()) {
      vTaskDelay(1);
      continue;
    }

    manager->status = BANK_CLOSING;

    // When bank is closing, have tellers finish current task, then quit.
    for (int i = 0; i < NUM_TELLERS; i++) {
      teller *t = manager->tellers[i];
      t->is_working = FALSE;
      // Wait for teller to finish current task
      xSemaphoreTake(t->teller_handler, 0);
    }

    // When all tellers are finished, close bank
    manager->status = BANK_CLOSED;
  }

  xSemaphoreTake(metrics_handler, 0);
  manager->close_time = BANK_OPEN_TIME + TIM_Elapsed();
  xSemaphoreTake(completion_handler, 0);
  fprintln("Closing Time: %s", TIME_TO_STR(manager->close_time));

  print_metrics(manager);

  xSemaphoreGive(manager->bank_handler);
  vTaskDelete(NULL);
}

/**
 * @brief Increment the idle hook count in the global metrics
 */
void vApplicationIdleHook(void) { EDIT_METRIC(GLOBAL_METRICS.idle_hook_cnt++) }
