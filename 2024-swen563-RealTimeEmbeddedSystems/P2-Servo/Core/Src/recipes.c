/*
 * recipes.c
 *
 *  Created on: Feb 26, 2024
 *      Author: Aiden Green
 */
#include "recipes.h"
#include "main.h"
#include "servo.h"
#include "utils.h"

/*
 * All of the defined recipies
 */
unsigned char recipe1[] = {MOV + 3, MOV | 5, RECIPE_END};
unsigned char recipe2[] = {MOV | 5, MOV | 2, RECIPE_END};

// Required recuipe from instructions
unsigned char recipe_required[] = {
    MOV | 0, // There must NOT be intervening instructions in this group to
    MOV | 5, // allow verification of default time delay.
    MOV | 0,   MOV | 3,
    LOOP | 0, // Test the default loop behavior.
    MOV | 1,   MOV | 4,   END_LOOP, MOV | 0,   MOV | 2, WAIT | 0,
    MOV | 3, // Move to an adjacent position to verify
    WAIT | 0,  MOV | 2,
    MOV | 3,   // Measure the timing precision of the 9.3 second delay with an
    WAIT | 31, // external timer
    WAIT | 31, WAIT | 31, MOV | 4,  RECIPE_END};

// Sets the servo to all positions
unsigned char recipe_all_positions[] = {
    MOV | 0, WAIT | 0, MOV | 1, WAIT | 0, MOV | 2, WAIT | 0,
    MOV | 3, WAIT | 0, MOV | 4, WAIT | 0, MOV | 5,
};

// Recipe that ends early and tries to move
unsigned char recipe_early_end[] = {
    MOV | 3,
    RECIPE_END,
    MOV | 1,
};

// Recipe that tries to move to an invalid position
unsigned char recipe_mov_err[] = {
    MOV | 5,
    MOV | 6,
    MOV | 1,
};

// Recipe with a nested loop
unsigned char recipe_nested_err[] = {
    MOV | 3,  LOOP | 3, MOV | 1, LOOP | 3, MOV | 1,  MOV | 5,
    LOOP | 2, MOV | 3,  MOV | 5, END_LOOP, END_LOOP, RECIPE_END,
};

// Recipe that loops going all the way to the right and left 1 move at a time
// twice
unsigned char recipe_rainbow[] = {
    MOV | 0, LOOP | 4, MOV | 1, MOV | 2, MOV | 3, MOV | 4,  MOV | 5,
    MOV | 4, MOV | 3,  MOV | 2, MOV | 1, MOV | 0, END_LOOP, RECIPE_END,
};

unsigned char *recipes[] = {
    recipe1,          recipe2,        recipe_required,   recipe_all_positions,
    recipe_early_end, recipe_mov_err, recipe_nested_err, NULL};

/*
 * @brief Parses the next command in the recipe
 * @param servo
 */
void Recipe_Parser(servo *servo) {
  unsigned char *recipe = servo->recipe; // Get the recipe
  uint8_t index = servo->recipe_pos;     // Position in the recipe
  char command = recipe[index];    // Get the command at the current position
  char arg = command & 0b00011111; // Get the argument of the command
  command = command & COMMAND_MSK; // Get just the command

  switch (command) {
  case MOV: // move to a position
    if (range(arg, 0, 5) && RECIPE_READY) {
      Servo_Move(servo, arg);
    } else {
      servo->recipe_state = recipe_error;
      servo->status = status_command_error;
    }
    break;

  case WAIT: // wait for a period of time
    servo->delay_rem = SERVO_DELAY * (arg + 1);
    servo->recipe_state = recipe_waiting;
    break;

  case LOOP: // start a loop
    // If the loop position isnt the current position then there is a nested
    // loop
    if (LOOP_POSITION != index && LOOPS_REMAINING) {
      servo->status = status_nested_error;
      servo->recipe_state = recipe_error;
      break;
    }
    // If the loop count argument is greater than 31, set the recipe state to
    // recipe_error and the servo state to state_command_error
    if (arg > 0b11111) {
      servo->status = status_command_error;
      servo->recipe_state = recipe_error;
      break;
    }
    // If the loop count argument is zero, set the loop counter to the passed in
    // value
    if (NOT LOOPS_REMAINING) {
      servo->loop_rem = arg;
      servo->loop_pos = index;
    }
    break;

  case END_LOOP: // end a loop
    // If the loop count is greater than 0, decrement the loop counter and set
    // the recipe position to the loop position
    if (LOOPS_REMAINING) {
      servo->loop_rem--;
      servo->recipe_pos = LOOP_POSITION;
    }
    break;

  case RECIPE_END: // end of the recipe
    servo->recipe_state = recipe_complete;
    servo->status = status_unknown;
    servo->event = recipe_ended;
    break;
  }
}

/**
 * @brief Moves the instructions along, and the recipe to the next command
 * @param servo
 */
void Recipe_Next(servo *servo) {

  // Check for necciary LED changes
  // Running
  if ((RECIPE_RUNNING || RECIPE_READY) && LED_STATE != LED_RECIPE_RUNNING) {
    servo->led_state = 0b100 | LED_RECIPE_RUNNING;
  }

  // Paused
  else if ((RECIPE_DONE || STATUS_PAUSED) && LED_STATE != LED_RECIPE_PAUSED) {
    servo->led_state = 0b100 | LED_RECIPE_PAUSED;
  }

  // Command Error
  else if (servo->status == status_command_error &&
           LED_STATE != LED_COMMAND_ERROR) {
    servo->led_state = 0b100 | LED_COMMAND_ERROR;
  }

  // Nested Loop Error
  else if (servo->status == status_nested_error &&
           LED_STATE != LED_NESTED_ERROR) {
    servo->led_state = 0b100 | LED_NESTED_ERROR;
  }

  // Handle incrementing recipe position when ready to move on
  if (NOT STATUS_PAUSED && (RECIPE_RUNNING || RECIPE_READY)) {
    Recipe_Parser(servo);
    servo->recipe_pos++;
    return;
  }

  if (NOT RECIPE_WAITING || (STATUS_PAUSED && NOT MOTOR_MOVING)) {
    return;
  }

  // Handle decrementing the remaining delay if recipe is waiting
  if (NOT DELAY_REMAINING) {
    servo->recipe_state = recipe_ready;
    if (MOTOR_MOVING) {
      servo->motor_state = state_idle;
    }
  }

  if (DELAY_REMAINING > SERVO_DELAY) {
    servo->delay_rem -= SERVO_DELAY;
  } else {
    servo->delay_rem = 0;
  }
}
