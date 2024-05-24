/*
 * servo.h
 *
 *      Author: Aiden Green
 */

#ifndef INC_SERVO_H_
#define INC_SERVO_H_

#include "main.h"
#include <stdint.h>

// This is a good way to define the status of the display.
// This should be in a header (.h) file.
enum status {
  status_running,
  status_paused,
  status_command_error,
  status_nested_error,
  status_unknown
};

enum recipe_states {
  recipe_paused,
  recipe_running,
  recipe_complete,
  recipe_error,
  recipe_unknown,
  recipe_waiting,
  recipe_ready,
};

// This is a good way to define the state of a servo motor.
// This should be in a header (.h) file.
enum servo_states {
  state_idle, // use a separate integer to record the current position (0
              // through 5) for each servo.
  state_unknown,
  state_moving,
  state_recipe_ended
};

enum events {
  user_restart,
  user_entered_left,
  recipe_ended,
  user_entered_right,
  user_pause,
  next_step,
  user_cont,
  none,
};

typedef struct {
  enum status status;
  enum servo_states motor_state;
  enum recipe_states recipe_state;
  enum events event;
  uint8_t position;
  unsigned char *recipe;
  uint8_t recipe_pos;
  uint8_t loop_rem;
  uint8_t loop_pos;
  uint32_t delay_rem;
  uint32_t channel;
  uint8_t led_state;
} servo;

extern servo servos[2];

#define SERVO1 servos[0]
#define SERVO2 servos[1]

void Servo_Init(void);
void Servo_Move(servo *servo, uint8_t position);
void Servo_State_Handler(servo *servo);

#endif /* INC_SERVO_H_ */
