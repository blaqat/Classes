/**
 * @file commands.c
 * @brief   This file contains the implementation of the command processing
 ******************************************************************************
 *  The command processing module is responsible for receiving, parsing, and
 *  executing commands from the user. It also handles the display of the current
 *  command and its parameters.
 *
 *  Created on: Apr 8, 2024
 *      Author: Aiden Green
 */

#include "commands.h"
#include "adc.h"
#include "dac.h"
#include "utils.h"
#include "waveform.h"

uint8_t recieve_buffer[256];
uint8_t recieve_index = 0;
uint8_t command_buffer[256];
uint8_t command_index = 0;
_Bool command_recieved = 0;
command_t current_command = {0};
QueueHandle_t recieve_queue;

/**
 * @brief Initializes command processing.
 * Sets up the necessary structures and states for handling commands.
 */
void command_init() { recieve_queue = xQueueCreate(40, sizeof(command_t)); }

/**
 * @brief Parses the received command.
 * Extracts and interprets command parameters from the input buffer.
 * @param cmd Pointer to the command structure to be populated.
 * @return True if parsing successful, false otherwise.
 */
_Bool command_parser(command_t *cmd) {
  return
      // IS GEN COMMAND
      sscanf(command_buffer, GEN_ARG_TEMPLATE, cmd->name, &cmd->channel,
             &cmd->type, &cmd->freq, &cmd->min_volt, &cmd->max_volt,
             &cmd->noise) == 7 ||
      // IS CAP COMMAND
      sscanf(command_buffer, CAP_ARG_TEMPLATE, cmd->name) == 1;
}

/**
 * @brief Validates a parsed command.
 * Checks if the command and its parameters are valid and executable.
 * @param cmd Pointer to the command to validate.
 * @return True if the command is valid, false otherwise.
 */
_Bool command_validator(command_t *cmd) {
  if (STR_EQ(cmd->name, "gen")) {
    cmd->type = to_lower(cmd->type);
    if (!range(cmd->channel, 1, 2)) {
      error("Channel must be 1 or 2");
      return 0;
    }
    if (cmd->type != 's' && cmd->type != 'r' && cmd->type != 't' &&
        cmd->type != 'a' && cmd->type != 'c') {
      error("Invalid waveform type");
      return 0;
    }
    if (!RANGE(cmd->min_volt, MIN_VOLTAGE, MAX_VOLTAGE)) {
      error("Invalid min voltage");
      return 0;
    }

    if (!RANGE(cmd->max_volt, MIN_VOLTAGE, MAX_VOLTAGE)) {
      error("Invalid max voltage");
      return 0;
    }

    if (cmd->freq != 0 && cmd->min_volt > cmd->max_volt) {
      error("Min voltage must be less than max voltage");
      return 0;
    }

    if (!RANGE(cmd->freq, MIN_FREQ, MAX_FREQ)) {
      error("Invalid frequency");
      return 0;
    }

    if (!range(cmd->noise, MIN_NOISE, MAX_NOISE)) {
      error("Invalid noise");
      return 0;
    }

    return 1;
  } else if (STR_EQ(cmd->name, "cap")) {
    return 1;
  }
}

/**
 * @brief Interprets and validates the command. Then sends it to the command
 * queue.
 * @return True if command sent successfully, false otherwise.
 */
_Bool command_interpreter() {
  command_t cmd = {0};
  command_index = 0;

  if (!command_parser(&cmd)) {
    error("Invalid command");
    return 0;
  }

  if (!command_validator(&cmd)) {
    return 0;
  }

  command_recieved = 1;
  if (xQueueSendFromISR(recieve_queue, &cmd, 0) != pdPASS) {
    error("Failed to send command to queue");
    return 0;
  }

  return 1;
}

/**
 * @brief Clears the command buffer.
 * Resets the command buffer and related indices for new commands.
 */
void command_clear() { memset(command_buffer, 0, sizeof(command_buffer)); }

/**
 * @brief Processes incoming commands.
 * Coordinates the parsing, validation, and execution of commands.
 */
void command_process() {
  if (STR_EQ(current_command.name, "gen")) {
    switch (current_command.channel) {
    case 1:
      dac_ch1_ready = 1;
      break;
    case 2:
      dac_ch2_ready = 1;
      break;
    default:
      break;
    }
  } else if (STR_EQ(current_command.name, "cap")) {
    adc_ready = 1;
  }
}

/**
 * @brief Task routine for command processing.
 * Continuously processes commands received from the queue.
 * @param x Context parameter for the task, not used.
 */
void command_processing_task(void *x) {
  print("> ");
  readc(&recieve_buffer[recieve_index]);

  while (1) {
    if (command_recieved) {
      command_t cmd;

      xQueueReceive(recieve_queue, &cmd, 0);
      strcpy(current_command.name, cmd.name);
      current_command.channel = cmd.channel;
      current_command.type = cmd.type;
      current_command.freq = cmd.freq;
      current_command.min_volt = cmd.min_volt;
      current_command.max_volt = cmd.max_volt;
      current_command.noise = cmd.noise;

      command_process();

      command_recieved = 0;
    }

    vTaskDelay(1);
  }

  vTaskDelete(NULL);
}

/**
 * @brief Displays the current command.
 * Outputs the current command and its status to a display or console.
 */
void command_display(uint32_t length) {
  clear();

  fprintln("DAC Channel %d", current_command.channel);
  fprintln("Shape: %s", char_to_wf_string(current_command.type));
  fprintln("Raw frequency: %.2f Hz", current_command.freq);
  fprintln("Number of samples: %d", length);

  if (current_command.freq > .01) {
    fprintln("DAC frequency: %.2f", 312500.0f / current_command.freq);
    fprintln("Voltage Range: %.2f V - %.2f V", current_command.min_volt,
             current_command.max_volt);
    fprintln("DAC Voltage Range: %.2f V - %.2f V",
             VOLT_CONV(current_command.min_volt),
             VOLT_CONV(current_command.max_volt));
  } else {
    fprintln("DAC frequency: %.2f", 0);
    fprintln("DC Voltage: %.2f V", current_command.min_volt);
    fprintln("DAC Voltage: %.2f V", VOLT_CONV(current_command.min_volt));
  }
}

/**
 * @brief Handles received command characters.
 * Manages the characters inputted into the command buffer.
 *
 * @note This is run in HAL_UART_RxCpltCallback
 */
void command_recieve_handler() {
  char r = recieve_buffer[recieve_index];
  // Display typed character
  put(r);

  command_buffer[command_index] = r;
  command_index++;

  // Handle command on enter press
  if (r == '\n' || r == '\r') {
    print("> ");
    println(command_buffer);
    _Bool passed = command_interpreter();
    command_clear();
    if (passed) {
      print("\r\n");
    }
    print("> ");
  }
  // Handle backspace and delete press
  else if (r == '\b' || r == 127) {
    if (command_index > 0) {
      command_buffer[command_index] = '\0';
      command_index -= 2;
    }
    print("\b \b");
  }

  // Handle command buffer overflow
  if (recieve_index < 255) {
    recieve_index++;
  } else {
    recieve_index = 0;
  }

  // Read next character
  readc(&recieve_buffer[recieve_index]);
}
