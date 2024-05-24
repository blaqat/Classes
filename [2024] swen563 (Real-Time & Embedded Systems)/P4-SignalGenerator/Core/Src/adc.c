/* USER CODE BEGIN Header */
/**
 ******************************************************************************
 * @file    adc.c
 * @brief   This file provides code for the configuration
 *          of the ADC instances.
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
#include "adc.h"
#include "commands.h"
#include "tim.h"
#include "utils.h"
#include "waveform.h"

/* USER CODE BEGIN 0 */

/* USER CODE END 0 */

_Bool adc_ready = 0;
uint16_t capture_buffer[WF_CAP_LEN];
ADC_HandleTypeDef hadc1;
DMA_HandleTypeDef hdma_adc1;

/**
 * @brief ADC1 Initialization Function
 * @param None
 * @retval None
 */
void MX_ADC1_Init(void) {

  /* USER CODE BEGIN ADC1_Init 0 */

  /* USER CODE END ADC1_Init 0 */

  ADC_MultiModeTypeDef multimode = {0};
  ADC_ChannelConfTypeDef sConfig = {0};

  /* USER CODE BEGIN ADC1_Init 1 */

  /* USER CODE END ADC1_Init 1 */

  /** Common config
   */
  hadc1.Instance = ADC1;
  hadc1.Init.ClockPrescaler = ADC_CLOCK_ASYNC_DIV1;
  hadc1.Init.Resolution = ADC_RESOLUTION_12B;
  hadc1.Init.DataAlign = ADC_DATAALIGN_RIGHT;
  hadc1.Init.ScanConvMode = ADC_SCAN_DISABLE;
  hadc1.Init.EOCSelection = ADC_EOC_SINGLE_CONV;
  hadc1.Init.LowPowerAutoWait = DISABLE;
  hadc1.Init.ContinuousConvMode = ENABLE;
  hadc1.Init.NbrOfConversion = 1;
  hadc1.Init.DiscontinuousConvMode = DISABLE;
  hadc1.Init.ExternalTrigConv = ADC_EXTERNALTRIG_T4_TRGO;
  hadc1.Init.ExternalTrigConvEdge = ADC_EXTERNALTRIGCONVEDGE_RISING;
  hadc1.Init.DMAContinuousRequests = ENABLE;
  hadc1.Init.Overrun = ADC_OVR_DATA_PRESERVED;
  hadc1.Init.OversamplingMode = DISABLE;
  if (HAL_ADC_Init(&hadc1) != HAL_OK) {
    Error_Handler();
  }

  /** Configure the ADC multi-mode
   */
  multimode.Mode = ADC_MODE_INDEPENDENT;
  if (HAL_ADCEx_MultiModeConfigChannel(&hadc1, &multimode) != HAL_OK) {
    Error_Handler();
  }

  /** Configure Regular Channel
   */
  sConfig.Channel = ADC_CHANNEL_5;
  sConfig.Rank = ADC_REGULAR_RANK_1;
  sConfig.SamplingTime = ADC_SAMPLETIME_2CYCLES_5;
  sConfig.SingleDiff = ADC_SINGLE_ENDED;
  sConfig.OffsetNumber = ADC_OFFSET_NONE;
  sConfig.Offset = 0;
  if (HAL_ADC_ConfigChannel(&hadc1, &sConfig) != HAL_OK) {
    Error_Handler();
  }
  /* USER CODE BEGIN ADC1_Init 2 */

  /* USER CODE END ADC1_Init 2 */
}

/**
 * @brief Start the ADC capture
 * @note This function starts the timer to trigger the ADC
 */
void ADC_Start_Capture() {
  TIM_Start(4);
  if (HAL_ADC_Start_DMA(&hadc1, (uint32_t *)capture_buffer, WF_CAP_LEN) !=
      HAL_OK) {
    Error_Handler();
  }
}

/**
 * @brief Stop the ADC capture
 */
void ADC_Stop_Capture() {
  if (HAL_ADC_Stop_DMA(&hadc1) != HAL_OK) {
    Error_Handler();
  }
  TIM_Stop(4);
}

/**
 * @brief Display the captured data
 * @param min_volt, minimum voltage
 * @param max_volt, maximum voltage
 */
void display_captured_data(uint32_t min_volt, uint32_t max_volt) {
  fprintln("DAC Voltage Range: %d V - %d V", min_volt, max_volt);
  fprintln("Voltage Range (V): %.2f V - %.2f V", ADC_CONV(min_volt),
           ADC_CONV(max_volt));
}

/**
 * @brief Get the captured data
 * @return uint16_t*, pointer to the captured data
 */
uint16_t *ADC_Get_Captured() { return capture_buffer; }

/* USER CODE BEGIN 1 */
/** ADC Task
 *
 * This task is responsible for capturing the ADC data
 * and processing it.
 *
 * It is a oneshot task that waits for the ADC to be ready.
 */
void ADC_task() {
  uint32_t min_volt, max_volt;

  while (1) {
    if (!adc_ready) {
      vTaskDelay(1);
      continue;
    }

    // // Start the ADC
    ADC_Start_Capture();
    // //
    // // Wait for the ADC to finish
    vTaskDelay(2000);
    //
    // // Stop the ADC
    ADC_Stop_Capture();

    // Process the ADC data
    min_volt = 0xFFFF;
    max_volt = 0;

    for (int i = 100; i < WF_CAP_LEN - 100; i++) {
      uint16_t sample = capture_buffer[i];
      if (sample < min_volt) {
        min_volt = sample;
      }
      if (sample > max_volt) {
        max_volt = sample;
      }
    }

    // Display the captured data
    display_captured_data(min_volt, max_volt);

    adc_ready = 0;
  }
}
/* USER CODE END 1 */
