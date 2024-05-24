/*
 * customer.h
 *
 *  Created on: Mar 16, 2024
 *      Author: Aiden Green
 */

#ifndef __CUSTOMER_H__
#define __CUSTOMER_H__

#include "teller.h"

/**
 * @brief Initialize a new customer with a random service time
 * @return customer* The initialized customer structure
 */
customer *customer_init(void);

/**
 * @brief Get the next customer from the queue
 * @return customer* The next customer, or NULL if the queue is empty
 */
customer *get_next_customer();

/**
 * @brief Update metrics after serving a customer and free the customer
 * @param t The teller structure
 */
void process_current_customer(teller *t);

/**
 * @brief Main task for generating customers
 * @param x The bank structure passed as a void pointer
 */
void customer_task(void *x);

#endif /*__ CUSTOMER_H__ */
