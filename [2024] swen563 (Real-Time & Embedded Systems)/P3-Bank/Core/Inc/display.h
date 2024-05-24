/*
 * display.h
 *
 *  Created on: Mar 16, 2024
 *      Author: Aiden Green
 */
#ifndef __DISPLAY_H__
#define __DISPLAY_H__

#include "bank.h"
#include "utils.h"

#define EDIT_METRIC(y)                                                         \
  loop {                                                                       \
    if (xSemaphoreTake(metrics_handler, 1) == pdTRUE) {                        \
      y;                                                                       \
      xSemaphoreGive(metrics_handler);                                         \
      break;                                                                   \
    } else {                                                                   \
      vTaskDelay(1);                                                           \
    }                                                                          \
  }
#define SAFE_PRINT(s, b) EDIT_METRIC(fprintln(s, b))

struct {
  uint32_t seconds;
  uint32_t minutes;
  uint32_t hours;
} typedef time;

/**
 * @brief Display task for updating the monitor
 * @param x The bank structure passed as a void pointer
 */
void display_task(void *x);

/**
 * @brief Convert a time in microseconds to a string representation
 * @param t The time structure
 * @return char* The string representation of the time
 */
char *time_to_str(time *t);

/**
 * @brief Convert a time in microseconds to a struct with hours, minutes, and
 * seconds
 * @param microseconds The time in microseconds
 * @return time* The time structure
 */
time *time_from_ticks(uint32_t microseconds);

/**
 * @brief Update the display with the current queue size
 * @param manager The bank structure
 */
void update_display(bank *manager);

/**
 * @brief Update the monitor with the simulated time, queue size, and teller
 * statuses
 * @param manager The bank structure
 */
void update_monitor(bank *manager);

/**
 * @brief Print the final metrics after the bank is closed
 * @param m The bank structure
 */
void print_metrics(bank *m);

#endif /*__ DISPLAY_H__ */
