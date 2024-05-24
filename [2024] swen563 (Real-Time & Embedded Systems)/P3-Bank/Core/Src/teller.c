/**
 * @file teller.c
 * @brief This file contains the implementation of the teller and related
 * functions.
 ******************************************************************************
 * The teller is responsible for serving customers and processing their
 * transactions. It waits for customers in the queue and serves them one by one.
 * The teller can also go on break by pressing a button. The teller keeps track
 * of its own metrics such as the number of customers served and the wait
 * duration between customers.
 *
 *  Created on: Mar 16, 2024
 *      Author: Aiden Green
 */

#include "customer.h"
#include "display.h"
#include "shield.h"
#include "timer.h"

/**
 * @brief Initialize a new teller with default values
 * @return teller* The initialized teller structure
 */
teller *teller_init(void) {
  teller *t = INIT_STRUCT(teller);
  t->current_customer = NULL;
  t->teller_handler = xSemaphoreCreateMutex();
  t->is_working = FALSE;
  t->is_on_break = FALSE;
  t->customers_served_cnt = 0;
  return t;
}

/**
 * @brief Main task for the teller
 * @param x The teller structure passed as a void pointer
 */
void teller_task(void *x) {
  teller *t = (teller *)x;
  SemaphoreHandle_t teller_mutex = t->teller_handler;
  xSemaphoreTake(teller_mutex, 0);
  t->idle_start_time = TIM_Elapsed();

  // If bank is closed, break out of loop
  while (t->is_working) {

    // If teller is on break, skip to next iteration
    while (teller_on_break(t)) {
      vTaskDelay(100);
    }

    if (t->is_on_break) {
      t->is_on_break = FALSE;
      t->idle_start_time = TIM_Elapsed();
    }

    // If teller is not on break and has no customer, wait for next customer
    if (t->current_customer == NULL) {
      customer *next_customer = get_next_customer();
      if (next_customer == NULL) {
        vTaskDelay(1);
        continue;
      }
      uint32_t current_time = TIM_Elapsed();
      if (current_time > t->idle_start_time)
        t->wait_duration = current_time - t->idle_start_time;
      else
        t->wait_duration = 0;

      next_customer->end_time = current_time + next_customer->service_time;

      t->idle_start_time = next_customer->end_time;
      free(t->current_customer);
      t->current_customer = next_customer;
      vTaskDelay(1);
      continue;
    }

    // If teller is not on break and has a customer, check if service is done
    // if (customer->end_time > TIM_Elapsed())
    //   continue;
    vTaskDelay(t->current_customer->service_time);
    process_current_customer(t);
    vTaskDelay(1);
  }

  SAFE_PRINT("Teller %d is heading out...", t->GPIO_pin)
  xSemaphoreGive(teller_mutex);
}

/**
 * @brief Check if the teller's button is pressed to indicate a break
 * @param t The teller structure
 * @return _Bool TRUE if the button is pressed, FALSE otherwise
 */
_Bool teller_on_break(teller *t) {
  uint16_t pin = t->GPIO_pin;
  if (SHLD_Button_IsPressed(pin == Teller3_Pin ? GPIOB : GPIOA, pin)) {
    if (!t->is_on_break) {
      t->is_on_break = TRUE;
      t->break_start_time = TIM_Elapsed();
    }
    return TRUE;
  }

  return FALSE;
}
