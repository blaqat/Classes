/* USER CODE BEGIN Header */
/**
 ******************************************************************************
 * @file    dac.c
 * @brief   This file provides code for the configuration
 *          of the DAC instances.
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
#include "dac.h"
#include "adc.h"
#include "commands.h"
#include "stm32l4xx_hal_dac_ex.h"
#include "tim.h"
#include "waveform.h"
#include <math.h>
#include <stdint.h>

/* USER CODE BEGIN 0 */

/* USER CODE END 0 */

DAC_HandleTypeDef hdac1;
_Bool dac_ch1_ready = 0;
_Bool dac_ch2_ready = 0;
uint16_t waveform_1[WF_LEN];
uint16_t waveform_2[WF_LEN];

/* DAC1 init function */
void MX_DAC1_Init(void) {

  /* USER CODE BEGIN DAC1_Init 0 */

  /* USER CODE END DAC1_Init 0 */

  DAC_ChannelConfTypeDef sConfig = {0};

  /* USER CODE BEGIN DAC1_Init 1 */

  /* USER CODE END DAC1_Init 1 */

  /** DAC Initialization
   */
  hdac1.Instance = DAC1;
  if (HAL_DAC_Init(&hdac1) != HAL_OK) {
    Error_Handler();
  }

  /** DAC channel OUT1 config
   */
  // sConfig.DAC_SampleAndHold = DAC_SAMPLEANDHOLD_DISABLE;
  sConfig.DAC_Trigger = DAC_TRIGGER_T2_TRGO;
  sConfig.DAC_OutputBuffer = DAC_OUTPUTBUFFER_ENABLE;
  // sConfig.DAC_ConnectOnChipPeripheral = DAC_CHIPCONNECT_DISABLE;
  // sConfig.DAC_UserTrimming = DAC_TRIMMING_FACTORY;
  if (HAL_DAC_ConfigChannel(&hdac1, &sConfig, DAC_CHANNEL_1) != HAL_OK) {
    Error_Handler();
  }

  /** DAC channel OUT2 config
   */
  sConfig.DAC_Trigger = DAC_TRIGGER_T5_TRGO;
  if (HAL_DAC_ConfigChannel(&hdac1, &sConfig, DAC_CHANNEL_2) != HAL_OK) {
    Error_Handler();
  }
  /* USER CODE BEGIN DAC1_Init 2 */

  /* USER CODE END DAC1_Init 2 */
}

/* USER CODE BEGIN 1 */

/**
 * @brief  Start DAC DMA
 * @param  channel: DAC channel
 * @param  waveform: waveform to be played
 * @param  length: length of the waveform
 * @retval None
 */
void DAC_Start_DMA(uint32_t channel, uint16_t *waveform, uint32_t length) {
  if (HAL_DAC_Start_DMA(&hdac1, channel, waveform, length, DAC_ALIGN_12B_R) !=
      HAL_OK) {
    Error_Handler();
  }
}

/**
 * @brief  Stop DAC DMA
 * @param  channel: DAC channel
 * @retval None
 */
void DAC_Stop_DMA(uint32_t channel) {
  if (HAL_DAC_Stop_DMA(&hdac1, channel) != HAL_OK) {
    Error_Handler();
  }
}

/**
 * @brief  Generate Noise waveform
 * @param  channel: DAC channel
 * @param  amplitude: amplitude of the noise
 * @retval None
 */
void DAC_Gen_Noise(uint32_t channel, uint32_t amplitude) {
  if (HAL_DACEx_NoiseWaveGenerate(&hdac1, channel, amplitude) != HAL_OK) {
    Error_Handler();
  }
}

uint32_t conv_noise_to_DACLFS(uint16_t noise) {
  switch (noise) {
  case 1:
    return DAC_LFSRUNMASK_BITS1_0;
  case 2:
    return DAC_LFSRUNMASK_BITS2_0;
  case 3:
    return DAC_LFSRUNMASK_BITS3_0;
  case 4:
    return DAC_LFSRUNMASK_BITS4_0;
  case 5:
    return DAC_LFSRUNMASK_BITS5_0;
  case 6:
    return DAC_LFSRUNMASK_BITS6_0;
  case 7:
    return DAC_LFSRUNMASK_BITS7_0;
  case 8:
    return DAC_LFSRUNMASK_BITS8_0;
  case 9:
    return DAC_LFSRUNMASK_BITS9_0;
  case 10:
    return DAC_LFSRUNMASK_BITS10_0;
  case 11:
    return DAC_LFSRUNMASK_BITS11_0;
  default:
    return DAC_LFSRUNMASK_BIT0;
  }
}

void dac_task(uint32_t channel, uint16_t *waveform, _Bool *ready,
              TIM_TypeDef *timer) {
  *ready = 0;
  uint32_t length;

  while (1) {
    if (!*ready) {
      vTaskDelay(1);
      continue;
    }

    DAC_Stop_DMA(channel);

    // Handle Captured waveform playback
    if (current_command.type == WF_CAP) {
      TIM_Set_AutoReload(timer,
                         (uint32_t)round(((float)312500 / 10000 * 2.56)));

      // waveform_copy_into(waveform, ADC_Get_Captured(), WF_CAP_LEN);
      length = WF_CAP_LEN;
      uint16_t *captured = ADC_Get_Captured();
      DAC_Start_DMA(channel, captured, length / 2);
    } else {
      if (current_command.freq < 0.001) {
        // Handle Direct Current waveform
        current_command.type = WF_DC;
        current_command.freq = 0;
        TIM_Set_AutoReload(timer, 0);
      } else {
        TIM_Set_AutoReload(timer,
                           (uint32_t)round(312500 / current_command.freq * 2));
      }

      uint32_t amp = conv_noise_to_DACLFS(current_command.noise);
      DAC_Gen_Noise(channel, amp);
      waveform_generate(waveform, current_command.type,
                        current_command.min_volt, current_command.max_volt);
      length = WF_LEN;
      DAC_Start_DMA(channel, waveform, length / 2);
    }

    command_display(length);

    *ready = 0;
    vTaskDelay(1);
  }
}

/** DAC Channel 1 task
 * @param x: Task handle
 * @note This is a one shot task that plays the waveform on DAC channel 1
 */
void DAC_ch1_task(void *x) {
  uint32_t channel = DAC1_CHANNEL_1;
  uint16_t *waveform = waveform_1;
  _Bool *ready = &dac_ch1_ready;

  dac_task(channel, waveform, ready, TIM2);
}

void DAC_ch2_task(void *x) {
  uint32_t channel = DAC1_CHANNEL_2;
  uint16_t *waveform = waveform_2;
  _Bool *ready = &dac_ch2_ready;

  dac_task(channel, waveform, ready, TIM5);
}

/* USER CODE END 1 */
