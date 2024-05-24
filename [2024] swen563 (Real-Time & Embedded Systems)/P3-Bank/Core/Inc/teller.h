/*
 * teller.h
 *
 *  Created on: Mar 16, 2024
 *      Author: Aiden Green
 */

#ifndef __TELLER_H__
#define __TELLER_H__

#include "FreeRTOS.h"
#include "semphr.h"

struct {
  uint32_t arrival_time;
  uint32_t service_time;
  uint32_t end_time;
} typedef customer;

struct {
  _Bool is_working;
  _Bool is_on_break;
  uint8_t customers_served_cnt;
  uint32_t idle_start_time;
  uint32_t break_start_time;
  uint32_t wait_duration;
  uint16_t GPIO_pin;
  customer *current_customer;
  SemaphoreHandle_t teller_handler;
} typedef teller;

/**
 * @brief Initialize a new teller with default values
 * @return teller* The initialized teller structure
 */
teller *teller_init(void);

/**
 * @brief Main task for the teller
 * @param x The teller structure passed as a void pointer
 */
void teller_task(void *x);

/**
 * @brief Check if the teller's button is pressed to indicate a break
 * @param t The teller structure
 * @return _Bool TRUE if the button is pressed, FALSE otherwise
 */
_Bool teller_on_break(teller *t);
#endif /*__ TELLER_H__ */
