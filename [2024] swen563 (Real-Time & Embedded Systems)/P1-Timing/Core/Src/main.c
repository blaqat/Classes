#include "clock.h"
#include "timer.h"
#include "uart.h"
#include "utils.h"
#include <stdbool.h>
#include <stdlib.h>

const int DEF_LOWER_BOUND = 950;
const int MIN_LOWER_BOUND = 50;
const int MAX_LOWER_BOUND = 9950;
const int NUM_PULSES = 1000;

/**
 * @brief Perform a power on self test
 *
 * @return _Bool
 */
_Bool power_on_self_test(void) {
  println("Starting power on self test");
  _Bool passed = false;
  uint32_t max_count = convert(100, 'm', 'i');

  // Wait for the timer to capture a value in the first 100 milliseconds
  while (get_time() < max_count) {
    if (has_captured()) {
      info("Power on self test passed");
      return true;
    }
  }

  // If the test did not pass, ask the user if they want to try again
  // If the user enters 'n', then the program will exit
  if (!passed) {
    char *response = inputln(
        "Power on self test failed. Try again? (y)es / (n)o / (i)gnore");
    if (response[0] == 'y') {
      return passed;
    } else if (response[0] == 'i') {
      return true;
    } else {
      close();
    }
    free(response);
  }

  return passed;
}

/**
 * @brief Set the timer base
 *
 * @param lower
 * @param upper
 * @return _Bool
 */
_Bool set_timer_base(uint32_t *lower, uint32_t *upper) {
  _Bool valid = false;
  char *response = inputln("Please enter the new lower bound: ");
  int new_lower = parse_int(response);
  free(response);

  if (new_lower == -1) {
    error("Invalid input: Must be an integer.");
  } else if (new_lower < MIN_LOWER_BOUND || new_lower > MAX_LOWER_BOUND) {
    error(format("Invalid input: Must be between %d and %d", MIN_LOWER_BOUND,
                 MAX_LOWER_BOUND));
  } else {
    valid = true;
    *lower = new_lower;
    *upper = new_lower + 100;
    info(format("The new bounds are %d and %d", new_lower, *upper));
  }
  return valid;
}

/**
 * @brief Capture a value from the timer
 *
 * @return uint32_t
 */
uint32_t capture_value() {
  while (!has_captured())
    ;
  return get_captured();
}

/**
 * @brief Fill the buckets with the captured values
 *
 * @param pulses
 * @param outliers
 * @param buckets
 * @param lower
 * @param upper
 */
void fill_buckets(int *pulses, int *outliers, int *buckets, uint32_t lower,
                  uint32_t upper) {
  uint32_t time1 = capture_value();
  _Bool tested = false;
  while (*pulses < NUM_PULSES) {
    uint32_t time2 = capture_value();
    uint32_t dt = time2 - time1;

    if (lower <= dt && dt <= upper) {
      buckets[dt - lower]++;
    } else {
      *outliers += 1;
    }
    time1 = time2;
    *pulses += 1;
  }
}

/**
 * @brief Print the results of the buckets filling test
 *
 * @param buckets
 * @param lower
 * @param outliers
 */
void print_results(int *buckets, int lower, int upper, int outliers) {
  int sum = outliers;
  println("-------------------------------");
  for (int i = 0; i < 100; i++) {
    if (buckets[i] != 0) {
      fprintln("%d:\t\t%d", i + lower, buckets[i]);
      sum += buckets[i];
    }
  }
  println("-------------------------------");
  fprintln("Outliers:\t%d", outliers);
  println("-------------------------------");
  fprintln("Sum:\t%d\n\r", sum);
}

int main(void) {
  /* Initalize the peripherals: UART, clock, PA0, and TIM
   * USART for user input/output TUI
   * System clock for USART
   * PA0 for the input capture from the oscilloscope
   * TIM for the timer
   */
  USART2_Init(115200);
  clock_init();
  PA0_Init();
  TIM_Init();

  start_timer();
  // Perform a power on self test until passed or the user exits
  while (!power_on_self_test()) {
  }

  uint32_t lower = DEF_LOWER_BOUND;
  uint32_t upper = lower + 100;

  while (1) {
    // Ask the user if they want to change the timer base
    char *response =
        finputln("The current bounds are %d and %d. Would you like "
                 "to change the timer base? (y/n)",
                 lower, upper);

    if (response[0] == 'y')
      // User set the timer base
      while (set_timer_base(&lower, &upper) == 0)
        ;
    free(response);
    input("Press any key to begin");

    // Capture the values
    int buckets[101] = {0};
    int outliers = 0;
    int pulses = 0;
    fill_buckets(&pulses, &outliers, buckets, lower, upper);

    // Print the results
    // Test print buckets
    // fprintln("Bucket 1: %d\n\rBucket 2: %d\n\rDelta: %d", buckets[0],
    //          buckets[1], buckets[1] - buckets[0]);
    print_results(buckets, lower, upper, outliers);

    // Ask the user if they want to run another test
    response = inputln("Would you like to run another test? (y/n)");
    if (response[0] != 'y') {
      break;
    }
    free(response);
  }
  close();
}
