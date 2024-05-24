/*
 * timer.c
 *
 *  Created on: Feb 26, 2024
 *      Author: Aiden Green
 */
#include "timer.h"
#include "utils.h"

volatile uint32_t repeats = 0;

/**
 * @brief Initialize GPIO PA0 and PA1 for Timer2
 */
void GPIO_AF_Init(void) {
  GPIO_InitTypeDef hpa0;

  __HAL_RCC_GPIOA_CLK_ENABLE();

  // Configure GPIO pin : PA0
  hpa0.Pin = GPIO_PIN_0;
  hpa0.Mode = GPIO_MODE_AF_PP;
  hpa0.Pull = GPIO_NOPULL;
  hpa0.Speed = GPIO_SPEED_FREQ_LOW;
  hpa0.Alternate =
      GPIO_AF1_TIM2; // Set PA0 to Alternate Function 1 for TIM2_CH1

  HAL_GPIO_Init(GPIOA, &hpa0);

  // Initialize pin for PA1
  hpa0.Pin = GPIO_PIN_1;
  HAL_GPIO_Init(GPIOA, &hpa0);
}

TIM_HandleTypeDef htim2;
TIM_OC_InitTypeDef sConfigOC;

/**
 * @brief Initialize Timer2
 */
void TIM_Init(void) {
  __HAL_RCC_TIM2_CLK_ENABLE(); // Enable clock for TIM2

  htim2.Instance = TIM2;
  htim2.Init.Prescaler = 100 * 80 - 1;
  HAL_TIM_GenerateEvent(&htim2, TIM_EVENTSOURCE_UPDATE);
  htim2.Init.CounterMode = TIM_COUNTERMODE_UP;
  htim2.Init.Period = PWM_PERIOD; // Set period to 20 milliseconds (ARR Value)
  htim2.Init.ClockDivision = TIM_CLOCKDIVISION_DIV1;
  htim2.Init.AutoReloadPreload = TIM_AUTORELOAD_PRELOAD_ENABLE;
  HAL_TIM_Base_Init(&htim2);

  // Timer PWM Configuration
  sConfigOC.OCMode = TIM_OCMODE_PWM1; // Set output mode to PWM
  sConfigOC.Pulse = 5;                // Set pulse width (CRR1 Value) [4-20]

  // Enable channel output for TIM2 for C1 and C2
  HAL_TIM_PWM_ConfigChannel(&htim2, &sConfigOC, TIM_CHANNEL_1);
  HAL_TIM_PWM_ConfigChannel(&htim2, &sConfigOC, TIM_CHANNEL_2);

  HAL_TIM_PWM_Start(&htim2, TIM_CHANNEL_1);
  HAL_TIM_PWM_Start(&htim2, TIM_CHANNEL_2);

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

/**
 * @brief Stop the PWM
 * @param channel
 */
void TIM_PWM_Stop(uint32_t channel) {
  switch (channel) {
  case 1:
    HAL_TIM_PWM_Stop(&htim2, TIM_CHANNEL_1);
    break;
  case 2:
    HAL_TIM_PWM_Stop(&htim2, TIM_CHANNEL_2);
    break;
  }
}

/**
 * @brief Get the PWM value
 * @param channel
 * @return uint32_t
 */
uint32_t TIM_PWM_Get(uint32_t channel) {
  switch (channel) {
  case 1:
    return TIM2->CCR1;
  case 2:
    return TIM2->CCR2;
  }

  return 0;
}

/**
 * @brief Set the PWM value
 * @param pwm
 * @param channel
 */
void TIM_PWM_Set(uint32_t pwm, uint32_t channel) {
  switch (channel) {
  case 1:
    TIM2->CCR1 = pwm;
    break;
  case 2:
    TIM2->CCR2 = pwm;
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
  if (repeats >= (10 * time_ms / PWM_PERIOD)) {
    repeats = 0;
    return 1;
  }

  return 0;
}
