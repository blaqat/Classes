#include "timer.h"
#include "stm32l476xx.h"
#include "utils.h"

// Initialize the GPIO for PA0
void PA0_Init(void) {
  RCC->AHB2ENR |= RCC_AHB2ENR_GPIOAEN_Msk; // Enable clock for A group of GPIO
  GPIOA->MODER &= ~GPIO_MODER_MODE0; // Reset PA0 Mode (clear bits 0 and 1)
  // Since Bits are cleared 00, PA0 is in input mode
  GPIOA->MODER |= GPIO_MODER_MODE0_1;  // Enabled Alternate Function for PA0
  GPIOA->AFR[0] |= GPIO_AFRL_AFSEL0_0; // Set PA0 to Alternate Function 1
}

/* TODO: Create TIM init functions in clock.c
 * TIM2->ARR = ~0; // Set the auto reload register to max value
 */
// Initialize the timer
void TIM_Init(void) {
  // Timer Configuration for TIM2
  RCC->APB1ENR1 |= RCC_APB1ENR1_TIM2EN; // Enable clock for TIM2
  // TIM2->PSC = sec_to_MHz("mi") - 1;     // Set prescaler to 1MHz
  TIM2->PSC = 16 - 1;
  TIM2->EGR |= TIM_EGR_UG; // Force reload of prescaler with update event

  // Timer Capture Configuration
  TIM2->CCER &= ~TIM_CCER_CC1E_Msk;   // Turn off capture output enable
  TIM2->CCMR1 &= ~TIM_CCMR1_IC1F_Msk; // Clear input capture filter
  TIM2->CCMR1 |= TIM_CCMR1_CC1S_0;    // Set Input Capture to Channel 1
  TIM2->CCER |= TIM_CCER_CC1E_Msk;    // Turn on capture output enable
}

// Start the timer / input capture
void start_timer() { TIM2->CR1 |= TIM_CR1_CEN_Msk; }

// Stop the timer / input capture
void stop_timer() { TIM2->CR1 &= ~TIM_CR1_CEN_Msk; }

// Get the current status from the timer
uint32_t get_status() { return TIM2->SR; }

// Get the current count from the timer
uint32_t get_time() { return TIM2->CNT; }

// Check if capture event has occured
_Bool has_captured() { return get_status() & TIM_SR_CC1IF_Msk; }

// Return the captured value
uint32_t get_captured() { return TIM2->CCR1; }
