// System include file
#include <stdlib.h>

// project include files
#include "clock.h"
#include "gpio.h"
#include "led.h"
#include "main.h"
#include "recipes.h"
#include "servo.h"
#include "timer.h"
#include "uart.h"
#include "utils.h"

/**
 * @brief Read a command from the user
 *
 * @param command
 * @param index
 * @return _Bool
 */
_Bool read_command(char *command, int *index) {
  read(command, index);
  if (EXIT(command)) {
    *index = 0;
    command = malloc(BUFFER_SIZE);
    return 0;
  }

  if (CR(command)) {
    *index = 0;
    command = malloc(BUFFER_SIZE);
    return 1;
  }

  return 0;
}

/**
 * @brief Process the user command
 *
 * @param command
 * @param servo
 * @return _Bool
 */
_Bool process_user_command(char command, servo *servo) {
  command = to_lower(command);
  switch (command) {
  case BEGIN:
    servo->event = user_restart;
    break;
  case PAUSE:
    servo->event = user_pause;
    break;
  case CONT:
    if (servo->status == status_paused || servo->status == status_command_error)
      servo->event = user_cont;
    break;
  case MOV_R:
    if (servo->status == status_paused)
      servo->event = user_entered_right;
    break;
  case MOV_L:
    if (servo->status == status_paused)
      servo->event = user_entered_left;
    break;
  case NONE:
    servo->event = next_step;
    break;
  default:
    return 0;
  }

  Servo_State_Handler(servo);
  return 1;
}

int main(void) {
  HAL_Init();
  SysClock_Init();
  USART2_Init(9600);
  MX_GPIO_Init();
  GPIO_AF_Init(); // GPIO alternate function PA0 PA1 for TIM2
  TIM_Init();     // Init TIM2

  TIM_Start();

  Servo_Init(); // Initalize servo structs
  LED_Off(LED2 | LED1);

  // Recipes:
  // 1. required (one detailed from the assignment with 9 second delay at
  // end)
  // 2. all_positions (all positions with 0 second delay)
  // 3. early_end (stops recipe with a mov at the end that doesnt happen since
  // recipe stopped)
  // 4. mov_err (recipie with a MOV 6 error)
  // 5. nested_err (nested loop recipe)
  // SERVO1.recipe = recipe_nested_err;
  // SERVO1.recipe = recipe_early_end;
  // SERVO1.recipe = recipe_required;
  SERVO2.recipe = recipe_required;
  SERVO1.recipe = recipe_rainbow;

  char *command = malloc(BUFFER_SIZE);
  int index = 0;
  print(OVERRIDE_PROMPT_CHAR);

  /*
   * Main loop (Every 100 ms)
   * 1. Read the user command
   * 1a. Process the user command
   * 2. Play the next step of the recipe based off servo state
   * 3. Handle the LED change (ONLY FOR SERVO1)
   */
  EVERY_100MS({
    if (read_command(command, &index)) {
      process_user_command(command[0], &SERVO1);
      process_user_command(command[1], &SERVO2);
      print(OVERRIDE_PROMPT_CHAR);
    }
    Recipe_Next(&SERVO1);
    Recipe_Next(&SERVO2);
    LED_handler(&SERVO1);
  })
}
