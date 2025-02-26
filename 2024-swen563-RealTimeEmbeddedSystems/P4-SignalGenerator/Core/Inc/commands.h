/*
 * commands.h
 *
 *  Created on: Apr 7, 2024
 *      Author: Aiden Green
 */
#ifndef __COMMANDS_H__
#define __COMMANDS_H__

#include "FreeRTOS.h"
#include "queue.h"
#define GEN_ARG_TEMPLATE "%s %d %c %lf %f %f %hu"
#define CAP_ARG_TEMPLATE "%s"

// Command structure
typedef struct {
  char name[20];
  int channel;
  char type;
  double freq;
  float min_volt;
  float max_volt;
  uint16_t noise;
} command_t;

extern QueueHandle_t recieve_queue;
extern command_t current_command;

/**
 * @brief Task routine for command processing.
 * Continuously processes commands received from the queue.
 * @param x Context parameter for the task, not used.
 */
void command_processing_task(void *x);

/**
 * @brief Displays the current command.
 * Outputs the current command and its status to a display or console.
 */
void command_display(uint32_t length);

/**
 * @brief Handles received command characters.
 * Manages the characters inputted into the command buffer.
 *
 * @note This is run in HAL_UART_RxCpltCallback
 */
void command_recieve_handler();

/**
 * @brief Initializes command processing.
 * Sets up the necessary structures and states for handling commands.
 */
void command_init();

#endif /*__ COMMANDS_H__ */
