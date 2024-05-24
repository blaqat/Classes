/* USER CODE BEGIN Header */
/**
 ******************************************************************************
 * @file    usart.c
 * @brief   This file provides code for the configuration
 *          of the USART instances.
 ******************************************************************************
 * @attention
 *
 * Copyright (c) 2024 STMicroelectronics.
 * All rights reserved.
 *
 * This software is licensed under terms that can be found in the LICENSE file
 * in the root directory of this software component.
 * If no LICENSE file comes with this software, it is provided AS-IS.
 *
 ******************************************************************************
 */
/* USER CODE END Header */
/* Includes ------------------------------------------------------------------*/
#include "usart.h"

/* USER CODE BEGIN 0 */

/* USER CODE END 0 */

UART_HandleTypeDef huart2;

/* USART2 init function */

void MX_USART2_UART_Init(void) {

  /* USER CODE BEGIN USART2_Init 0 */

  /* USER CODE END USART2_Init 0 */

  /* USER CODE BEGIN USART2_Init 1 */

  /* USER CODE END USART2_Init 1 */
  huart2.Instance = USART2;
  huart2.Init.BaudRate = 115200;
  huart2.Init.WordLength = UART_WORDLENGTH_8B;
  huart2.Init.StopBits = UART_STOPBITS_1;
  huart2.Init.Parity = UART_PARITY_NONE;
  huart2.Init.Mode = UART_MODE_TX_RX;
  huart2.Init.HwFlowCtl = UART_HWCONTROL_NONE;
  huart2.Init.OverSampling = UART_OVERSAMPLING_16;
  huart2.Init.OneBitSampling = UART_ONE_BIT_SAMPLE_DISABLE;
  huart2.AdvancedInit.AdvFeatureInit = UART_ADVFEATURE_NO_INIT;
  if (HAL_UART_Init(&huart2) != HAL_OK) {
    Error_Handler();
  }
  /* USER CODE BEGIN USART2_Init 2 */

  /* USER CODE END USART2_Init 2 */
}

void HAL_UART_MspInit(UART_HandleTypeDef *uartHandle) {

  GPIO_InitTypeDef GPIO_InitStruct = {0};
  RCC_PeriphCLKInitTypeDef PeriphClkInit = {0};
  if (uartHandle->Instance == USART2) {
    /* USER CODE BEGIN USART2_MspInit 0 */

    /* USER CODE END USART2_MspInit 0 */

    /** Initializes the peripherals clock
     */
    PeriphClkInit.PeriphClockSelection = RCC_PERIPHCLK_USART2;
    PeriphClkInit.Usart2ClockSelection = RCC_USART2CLKSOURCE_PCLK1;
    if (HAL_RCCEx_PeriphCLKConfig(&PeriphClkInit) != HAL_OK) {
      Error_Handler();
    }

    /* USART2 clock enable */
    __HAL_RCC_USART2_CLK_ENABLE();

    __HAL_RCC_GPIOA_CLK_ENABLE();
    /**USART2 GPIO Configuration
    PA2     ------> USART2_TX
    PA3     ------> USART2_RX
    */
    GPIO_InitStruct.Pin = USART_TX_Pin | USART_RX_Pin;
    GPIO_InitStruct.Mode = GPIO_MODE_AF_PP;
    GPIO_InitStruct.Pull = GPIO_NOPULL;
    GPIO_InitStruct.Speed = GPIO_SPEED_FREQ_VERY_HIGH;
    GPIO_InitStruct.Alternate = GPIO_AF7_USART2;
    HAL_GPIO_Init(GPIOA, &GPIO_InitStruct);

    /* USER CODE BEGIN USART2_MspInit 1 */

    /* USER CODE END USART2_MspInit 1 */
  }
}

void HAL_UART_MspDeInit(UART_HandleTypeDef *uartHandle) {

  if (uartHandle->Instance == USART2) {
    /* USER CODE BEGIN USART2_MspDeInit 0 */

    /* USER CODE END USART2_MspDeInit 0 */
    /* Peripheral clock disable */
    __HAL_RCC_USART2_CLK_DISABLE();

    /**USART2 GPIO Configuration
    PA2     ------> USART2_TX
    PA3     ------> USART2_RX
    */
    HAL_GPIO_DeInit(GPIOA, USART_TX_Pin | USART_RX_Pin);

    /* USER CODE BEGIN USART2_MspDeInit 1 */

    /* USER CODE END USART2_MspDeInit 1 */
  }
}

/* USER CODE BEGIN 1 */

uint8_t USART_Read(USART_TypeDef *USARTx) {
  // SR_RXNE (Read data register not empty) bit is set by hardware
  while (!(USARTx->ISR & USART_ISR_RXNE))
    ; // Wait until RXNE (RX not empty) bit is set
  // USART resets the RXNE flag automatically after reading DR
  return ((uint8_t)(USARTx->RDR & 0xFF));
  // Reading USART_DR automatically clears the RXNE flag
}

/**
 * @brief Read a byte from the USART without blocking
 * @param USARTx
 * @return uint8_t
 */
uint8_t USART_Read_Async(USART_TypeDef *USARTx) {
  if (USARTx->ISR & USART_ISR_RXNE) {
    return ((uint8_t)(USARTx->RDR & 0xFF));
  }
  return '\0';
}

void USART_Write(USART_TypeDef *USARTx, uint8_t *buffer, uint32_t nBytes) {
  uint32_t i;
  // A byte to be transmitted is written to the TDR (transmit data register),
  // and the TXE (transmit empty) bit is cleared. The TDR is copied to an output
  // shift register for serialization when that register is empty, and the TXE
  // bit is set.
  for (i = 0; i < nBytes; i++) {
    while (!(USARTx->ISR & USART_ISR_TXE))
      ; // wait until TXE (TX empty) bit is set
    USARTx->TDR =
        buffer[i] & 0xFF; // writing USART_TDR automatically clears the TXE flag
  }
  while (!(USARTx->ISR & USART_ISR_TC))
    ; // wait until TC bit is set
  USARTx->ISR &= ~USART_ISR_TC;
}

void USART_IRQHandler(USART_TypeDef *USARTx, uint8_t *buffer,
                      uint32_t *pRx_counter) {
  if (USARTx->ISR & USART_ISR_RXNE) { // Received data
    buffer[*pRx_counter] =
        USARTx->RDR; // Reading USART_DR automatically clears the RXNE flag
    (*pRx_counter)++;
    if ((*pRx_counter) >= BufferSize) {
      (*pRx_counter) = 0;
    }
  }
}
void USART_Delay(uint32_t us) {
  uint32_t time = 100 * us / 7;
  while (--time)
    ;
}
/* USER CODE END 1 */
