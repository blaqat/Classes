/*
 * timer.c
 *
 *  Created on: Feb 26, 2024
 *      Author: Aiden Green
 */
#include "timer.h"
#include "bank.h"
#include "utils.h"

volatile uint32_t repeats = 0;
volatile uint32_t total_time = 0;

TIM_HandleTypeDef htim2;
TIM_OC_InitTypeDef sConfigOC;

#define PERIOD 500

/**
 * @brief Initialize Timer2
 */
void TIM2_Init(void) {
  __HAL_RCC_TIM2_CLK_ENABLE(); // Enable clock for TIM2

  htim2.Instance = TIM2;
  htim2.Init.Prescaler = 100 * sec_to_MHz("mi") - 1; // Set prescaler
  HAL_TIM_GenerateEvent(&htim2, TIM_EVENTSOURCE_UPDATE);
  htim2.Init.CounterMode = TIM_COUNTERMODE_UP;
  htim2.Init.Period = PERIOD; // Set period to 100 milliseconds (ARR Value)
  htim2.Init.ClockDivision = TIM_CLOCKDIVISION_DIV1;
  htim2.Init.AutoReloadPreload = TIM_AUTORELOAD_PRELOAD_ENABLE;
  HAL_TIM_Base_Init(&htim2);

  // Enable channel output for TIM2 for C1
  HAL_TIM_PWM_ConfigChannel(&htim2, &sConfigOC, TIM_CHANNEL_1);
  HAL_TIM_PWM_Start(&htim2, TIM_CHANNEL_1);

  // Enable the TIM2 global interrupt
  HAL_NVIC_SetPriority(TIM2_IRQn, 0, 0);
  HAL_NVIC_EnableIRQ(TIM2_IRQn);

  HAL_TIM_Base_Start_IT(&htim2);
}

/*
 * @brief Timer2 interrupt handler
 */
void TIM2_IRQHandler(void) {
  // Check if the update interrupt flag is set
  if (__HAL_TIM_GET_FLAG(&htim2, TIM_FLAG_UPDATE) != RESET) {
    __HAL_TIM_CLEAR_IT(&htim2, TIM_IT_UPDATE);
    repeats++; // Increment the repeat counter
    total_time += PERIOD / 10;
  }
}

/**
 * @brief Start the timer
 */
void TIM_Start() { TIM2->CR1 |= TIM_CR1_CEN_Msk; }

/**
 * @brief Stop the timer
 */
void TIM_Stop() { TIM2->CR1 &= ~TIM_CR1_CEN_Msk; }

/**
 * @brief Get the current count of the timer
 * @return uint32_t
 */
uint32_t TIM_Count() { return TIM2->CNT; }

uint32_t TIM_Elapsed() { return total_time; }

/**
 * @brief Start the PWM signal
 * @param channel
 */
void TIM_PWM_Start(uint32_t channel) {
  switch (channel) {
  case 1:
    HAL_TIM_PWM_Start(&htim2, TIM_CHANNEL_1);
    break;
  case 2:
    HAL_TIM_PWM_Start(&htim2, TIM_CHANNEL_2);
    break;
  }
}

/*
 * @brief Get the number of loops occured since the last reset
 * @return uint32_t
 */
uint32_t TIM_Loops() { return repeats; }

/*
 * @brief Reset the loop counter after set amount of time
 * @param time_ms
 *
 * @return _Bool
 */
_Bool TIM_Loop_To(uint32_t time_ms) {
  if (repeats >= (10 * time_ms / PERIOD)) {
    repeats = 0;
    return 1;
  }

  return 0;
}
