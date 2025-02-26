/**
 * @file customer.c
 * @brief This file contains the implementation of the customer and related
 * functions.
 ******************************************************************************
 * The customer is generated randomly and added to the queue. Each customer has
 * a random service time and an arrival time. The customer task generates new
 * customers at random intervals and adds them to the queue. It also updates the
 * maximum queue depth metric.
 *
 *  Created on: Mar 16, 2024
 *      Author: Aiden Green
 */

#include "bank.h"
#include "display.h"
#include "main.h"
#include "timer.h"
#include "utils.h"

/**
 * @brief Initialize a new customer with a random service time
 * @return customer* The initialized customer structure
 */
customer *customer_init(void) {
  customer *c = INIT_STRUCT(customer);
  c->arrival_time = TIM_Elapsed();
  c->service_time = gen_random(MIN_CUST_SERVICE_TIME, MAX_CUST_SERVICE_TIME);

  return c;
}

/**
 * @brief Get the next customer from the queue
 * @return customer* The next customer, or NULL if the queue is empty
 */
customer *get_next_customer() {
  customer *c;
  if (xQueueReceive(customer_queue, &c, 0) == pdTRUE) {
    return c;
  } else {
    return NULL;
  }
}

/**
 * @brief Update metrics after serving a customer and free the customer
 * @param t The teller structure
 */
void process_current_customer(teller *t) {
  customer *customer = t->current_customer;
  // If service is done, update metrics and free customer
  EDIT_METRIC({
    t->customers_served_cnt++;
    uint32_t queue_time =
        customer->end_time - customer->service_time - customer->arrival_time;
    int num_customers = GLOBAL_METRICS.customers_served_cnt;

    GLOBAL_METRICS.avg_queue_time =
        UPDATE_AVG(num_customers, GLOBAL_METRICS.avg_queue_time, queue_time);

    GLOBAL_METRICS.max_queue_time =
        MAX(GLOBAL_METRICS.max_queue_time, queue_time);

    GLOBAL_METRICS.avg_serve_time = UPDATE_AVG(
        num_customers, GLOBAL_METRICS.avg_serve_time, customer->service_time);

    GLOBAL_METRICS.max_serve_time =
        MAX(GLOBAL_METRICS.max_serve_time, customer->service_time);

    GLOBAL_METRICS.avg_wait_time = UPDATE_AVG(
        num_customers, GLOBAL_METRICS.avg_wait_time, t->wait_duration);

    GLOBAL_METRICS.max_wait_time =
        MAX(GLOBAL_METRICS.max_wait_time, t->wait_duration);

    GLOBAL_METRICS.customers_served_cnt++;
    free(customer);
    t->current_customer = NULL;
  })
}

/**
 * @brief Main task for generating customers
 * @param x The bank structure passed as a void pointer
 */
void customer_task(void *x) {
  bank *manager = (bank *)x;

  int customer_wait_time =
      gen_random(MIN_CUST_ARRIVAL_TIME, MAX_CUST_ARRIVAL_TIME);

  while (manager->status != BANK_CLOSING) {
    // If bank is closed empty the queue, then quit
    // if (manager->status == BANK_CLOSED) {
    //   customer *c;
    //   while (xQueueReceive(customer_queue, &c, 5)) {
    //     free(c);
    //   }
    //   break;
    // }

    // If time passed for new customer arrival, add customer to queue
    // if (manager->last_customer_arrival + customer_wait_time > TIM_Elapsed())
    //   continue;
    vTaskDelay(customer_wait_time);
    customer *c = customer_init();

    if (c == NULL) {
      error("Failed to initialize customer");
    } else {
      if (xQueueSend(customer_queue, &c, 0) != pdTRUE) {
        error("Error to add customer to queue");
        free(c);
        continue;
      }
    }

    // Update max queue depth
    EDIT_METRIC({
      uint16_t queue_depth = (uint16_t)uxQueueMessagesWaiting(customer_queue);
      GLOBAL_METRICS.max_queue_depth =
          MAX(GLOBAL_METRICS.max_queue_depth, queue_depth);
    })

    manager->last_customer_arrival = c->arrival_time;

    // Generate new random wait time for next customer
    customer_wait_time =
        gen_random(MIN_CUST_ARRIVAL_TIME, MAX_CUST_ARRIVAL_TIME);
    taskYIELD();
  }

  vTaskDelete(NULL);
}
