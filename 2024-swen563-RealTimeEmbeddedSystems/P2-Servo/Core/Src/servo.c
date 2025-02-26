/*
 * servo.c
 *
 *  Created on: Feb 26, 2024
 *      Author: Aiden Green
 */
#include "servo.h"
#include "recipes.h"
#include "timer.h"
#include "utils.h"
#include <stdlib.h>

servo servos[2];
/**
 * @brief Reset all servo states to default values
 *
 * @param servo
 */
void Servo_Reset(servo *servo) {
  servo->motor_state = state_idle;
  servo->status = status_paused;
  servo->recipe_state = recipe_paused;
  servo->event = none;
  servo->position = 0;
  servo->recipe_pos = 0;
  servo->loop_rem = 0;
  servo->loop_pos = 0;
  servo->led_state = 0b000;
}

/**
 * @brief Initalize 2 servo structs with default values
 */
void Servo_Init(void) {
  for (int i = 0; i < 2; i++) {
    servos[i] = (servo){
        .channel = i + 1,
    };
    Servo_Reset(&servos[i]);
  }
}

/**
 * @brief Maps a servo position to its corresponding PWM value
 * Positions = 0, 1, 02, 03, 04, 05
 * PWM POS   = 4, 7, 10, 14, 17, 20
 *
 * @param pos
 * @return uint32_t
 */
uint32_t map_pos_pwm(int pos) {
  if (pos == 0)
    return MIN_PWM;
  if (pos == 5)
    return MAX_PWM;
  if (pos >= 3)
    return (3 * pos + MIN_PWM + 1);

  return (3 * pos + MIN_PWM);
}

/**
 * @brief Move the servo to a new position
 *
 * @param servo
 * @param position
 */
void Servo_Move(servo *servo, uint8_t position) {
  servo->motor_state = state_moving;
  servo->position = position;
  servo->recipe_state = recipe_waiting;

  // If the position is out of range, set it to the nearest limit
  if (NOT range(position, 0, 5)) {
    if (position > 5)
      position = 5;
    if (position < 0)
      position = 0;
  }

  // Convert the position to a PWM value
  uint32_t pwm = map_pos_pwm(position);
  servo->delay_rem = SERVO_DELAY * map_pos_pwm(abs(servo->position - position));

  TIM_PWM_Set(pwm, servo->channel);
  servo->event = next_step;
}

/*
 * @brief Process the user command commands and other events
 * @param servo
 */
void Event_Handler(servo *servo) {
  switch (servo->event) {
  case user_restart: // Reset the servo struct
    Servo_Reset(servo);
    servo->recipe_state = recipe_ready;
    servo->status = status_running;
    break;

  case user_entered_left: // Move the servo to the left if possible
  {
    uint8_t new_pos = servo->position - 1;
    if (range(new_pos, 0, 5)) { // If the new position is in range
      Servo_Move(servo, new_pos);
    }
  } break;

  case user_entered_right: // Move the servo to the right if possible
  {
    uint8_t new_pos = servo->position + 1;
    if (range(new_pos, 0, 5)) { // If the new position is in range
      Servo_Move(servo, new_pos);
    }
  } break;

  case recipe_ended: // Set servo to completed state
    servo->status = status_unknown;
    servo->motor_state = state_recipe_ended;
    servo->recipe_pos = 0;
    break;

  case user_pause: // Set servo and recipe to paused state
    if (servo->recipe_state != recipe_complete &&
        servo->recipe_state != recipe_error) {
      servo->status = status_paused;
      servo->motor_state = state_idle;
    }
    break;

  case next_step: // Move the recipe to the next step
    if (servo->recipe_state == recipe_error)
      return;
    Recipe_Next(servo);
    break;

  case user_cont: // Unpause the servo and recipe
    if (servo->recipe_state != recipe_complete &&
        servo->recipe_state != recipe_error)
      servo->status = status_running;
    break;

  case none: // Ignore
  default:
    break;
  }
}

/*
 * @brief Process the servo state
 * @param servo
 */
void Servo_State_Handler(servo *servo) {
  switch (servo->motor_state) {
  case state_idle:   // Ready for next event
  case state_moving: // Moving to a new position
    Event_Handler(servo);
    break;
  case state_recipe_ended: // Recipe has ended
    servo->status = status_unknown;
    servo->recipe_state = recipe_unknown;
    break;
  case state_unknown:
    break;
  }
}
