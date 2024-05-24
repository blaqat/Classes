/*
 * bank.h
 *
 *  Created on: Mar 16, 2024
 *      Author: Aiden Green
 */
#ifndef __BANK_H__
#define __BANK_H__

#include "FreeRTOS.h"
#include "semphr.h"
#include "teller.h"

extern SemaphoreHandle_t metrics_handler;
extern SemaphoreHandle_t completion_handler;
extern QueueHandle_t customer_queue;

#define MAX_CUSTOMERS 420
#define NUM_TELLERS 3

#define MIN_CUST_SERVICE_TIME (TIME_IN_MIN / 2)
#define MAX_CUST_SERVICE_TIME (8 * TIME_IN_MIN)

#define MIN_CUST_ARRIVAL_TIME (1 * TIME_IN_MIN)
#define MAX_CUST_ARRIVAL_TIME (4 * TIME_IN_MIN)

#define TIME_IN_MIN 100 // 60000 for real time
#define TIME_IN_HOUR (60 * TIME_IN_MIN)
#define TIME_IN_SEC (TIME_IN_MIN / 60)

#define MS_TO_TIME(x)                                                          \
  (time) {                                                                     \
    x / TIME_IN_HOUR, (x % TIME_IN_HOUR) / TIME_IN_MIN,                        \
        (x % TIME_IN_HOUR) % TIME_IN_MIN                                       \
  }
#define TIME_TO_STR(t) time_to_str(time_from_ticks(t))
#define PRINT_TIME(t) println(TIME_TO_STR(t))
#define TIME_STR_TEMP "%02d:%02d:%02d"

#define BANK_OPEN_HOURS 7
#define BANK_OPEN_TIME (9 * TIME_IN_HOUR)   // 9:00 AM
#define BANK_CLOSE_TIME (16 * TIME_IN_HOUR) // 4:00 PM
#define TIME_IN_HOURS_OPEN (BANK_OPEN_HOURS * TIME_IN_HOUR)
#define GET_CLOSING_TIME(x) (x + BANK_OPEN_HOURS * TIME_IN_HOUR)

enum bank_status {
  BANK_CLOSED = 0,
  BANK_OPEN = 1,
  BANK_UNOPENED = 2,
  BANK_CLOSING = 3,
} typedef bank_status;

struct {
  uint8_t customers_served_cnt;
  uint32_t avg_queue_time;
  uint32_t avg_serve_time;
  uint32_t avg_wait_time;
  uint32_t max_queue_time;
  uint32_t max_serve_time;
  uint32_t max_wait_time;
  uint16_t max_queue_depth;
  uint32_t idle_hook_cnt;
} typedef metrics;

struct {
  uint32_t open_time;
  uint32_t close_time;
  bank_status status;
  uint32_t last_customer_arrival;
  teller *tellers[NUM_TELLERS];
  SemaphoreHandle_t bank_handler;
} typedef bank;

extern metrics GLOBAL_METRICS;

/**
 * @brief Initialize global metrics to zero
 * @return metrics The initialized metrics structure
 */
metrics metrics_init(void);

/**
 * @brief Initialize the bank manager and tellers
 * @return bank* The initialized bank structure
 */
bank *bank_init(void);

/**
 * @brief Main task for the bank
 * @param x The bank structure passed as a void pointer
 */
void bank_task(void *x);

#endif /*__ BANK_H__ */
