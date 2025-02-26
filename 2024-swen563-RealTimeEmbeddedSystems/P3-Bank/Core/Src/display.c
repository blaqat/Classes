
/**
 * @file display.c
 * @brief This file contains the implementation of the display and related
 * functions.
 ******************************************************************************
 * The display is responsible for updating the monitor with the current
 * simulated time, queue size, and teller statuses. It also updates the digital
 * display with the current queue size. t also provides utility functions for
 * converting time to string representation and printing the final metrics after
 * the bank is closed.
 */

#include "display.h"
#include "main.h"
#include "shield.h"
#include "timer.h"
#include "utils.h"

/**
 * @brief Display task for updating the monitor
 * @param x The bank structure passed as a void pointer
 */
void display_task(void *x) {
  bank *manager = (bank *)x;
  while (manager->status != BANK_CLOSED) {
    xSemaphoreTake(completion_handler, 0);
    update_monitor(manager);
    xSemaphoreGive(completion_handler);
    vTaskDelay(500);
  }
  vTaskDelete(NULL);
}

/**
 * @brief Convert a time in microseconds to a string representation
 * @param t The time structure
 * @return char* The string representation of the time
 */
char *time_to_str(time *t) {
  static char buffer[32];
  sprintf(buffer, TIME_STR_TEMP, t->hours, t->minutes, t->seconds);
  return buffer;
}

/**
 * @brief Convert a time in microseconds to a struct with hours, minutes, and
 * seconds
 * @param microseconds The time in microseconds
 * @return time* The time structure
 */
time *time_from_ticks(uint32_t microseconds) {
  time *t = INIT_STRUCT(time);
  t->minutes = microseconds / TIME_IN_MIN % 60;
  t->seconds = microseconds / (TIME_IN_MIN / 60) % 60;
  t->hours = microseconds / TIME_IN_MIN / 60;
  if (t->hours > 12)
    t->hours %= 12;
  return t;
}

/**
 * @brief Update the display with the current queue size
 * @param manager The bank structure
 */
void update_display(bank *manager) {
  if (manager->status == BANK_CLOSED)
    return;

  int queue_size = uxQueueMessagesWaiting(customer_queue);
  SHLD_Display_Int((uint16_t)queue_size);
}

/**
 * @brief Update the monitor with the simulated time, queue size, and teller
 * statuses
 * @param manager The bank structure
 */
void update_monitor(bank *manager) {
  // Display the simulated time
  clear();
  uint32_t elapsed_time = BANK_OPEN_TIME + TIM_Elapsed();
  fprintln("Simulated Time: %s", TIME_TO_STR(elapsed_time));

  // Display the number of customers waiting in the queue
  int queue_size = uxQueueMessagesWaiting(customer_queue);
  fprintln("Customers Waiting in Queue: %d", queue_size);

  // Display the status of each teller and the number of customers they have
  // served
  fprintln("Teller Status:");
  for (int i = 0; i < NUM_TELLERS; i++) {
    teller *t = manager->tellers[i];
    const char *status = (!t->is_working)                ? "Off Duty"
                         : (t->is_on_break)              ? "On Break"
                         : (t->current_customer != NULL) ? "Busy"
                                                         : "Idle";
    fprintln("Teller %d: %s | Customers Served: %d", i + 1, status,
             t->customers_served_cnt);
  }

  // Print a separator line
  println("----------------------------------");
}

/**
 * @brief Print the final metrics after the bank is closed
 * @param m The bank structure
 */
void print_metrics(bank *m) {
  println("Metrics:");
  fprintln("Total Customers Served: %d", GLOBAL_METRICS.customers_served_cnt);
  for (int i = 0; i < NUM_TELLERS; i++) {
    teller *t = m->tellers[i];
    fprintln("Teller %d's Customers Served: %d", i + 1,
             t->customers_served_cnt);
  }

  fprintln("Average Queue Time: %s",
           TIME_TO_STR(GLOBAL_METRICS.avg_queue_time));
  fprintln("Average Service Time: %s",
           TIME_TO_STR(GLOBAL_METRICS.avg_serve_time));
  fprintln("Average Teller Wait Time: %s",
           TIME_TO_STR(GLOBAL_METRICS.avg_wait_time));

  fprintln("Max Queue Time: %s", TIME_TO_STR(GLOBAL_METRICS.max_queue_time));
  fprintln("Max Service Time: %s", TIME_TO_STR(GLOBAL_METRICS.max_serve_time));
  fprintln("Max Teller Wait Time: %s",
           TIME_TO_STR(GLOBAL_METRICS.max_wait_time));

  fprintln("Max Queue Depth: %d", GLOBAL_METRICS.max_queue_depth);
  fprintln("Idle Hook Count: %d", GLOBAL_METRICS.idle_hook_cnt);
}
