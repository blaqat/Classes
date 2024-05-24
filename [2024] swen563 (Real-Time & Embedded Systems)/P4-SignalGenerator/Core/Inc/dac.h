/* USER CODE BEGIN Header */
/**
 ******************************************************************************
 * @file    dac.h
 * @brief   This file contains all the function prototypes for
 *          the dac.c file
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
/* Define to prevent recursive inclusion -------------------------------------*/
#ifndef __DAC_H__
#define __DAC_H__

#ifdef __cplusplus
extern "C" {
#endif

/* Includes ------------------------------------------------------------------*/
#include "main.h"

/* USER CODE BEGIN Includes */

/* USER CODE END Includes */
extern DAC_HandleTypeDef hdac1;
extern _Bool dac_ch1_ready;
extern _Bool dac_ch2_ready;

/* USER CODE BEGIN Private defines */

#define DAC_BUFFER_SIZE 256

/* USER CODE END Private defines */

void MX_DAC1_Init(void);

/* USER CODE BEGIN Prototypes */
/**
 * @brief  Start DAC DMA
 * @param  channel: DAC channel
 * @param  waveform: waveform to be played
 * @param  length: length of the waveform
 * @retval None
 */
void DAC_Start_DMA(uint32_t channel, uint16_t *waveform, uint32_t length);

/**
 * @brief  Stop DAC DMA
 * @param  channel: DAC channel
 * @retval None
 */
void DAC_Stop_DMA(uint32_t channel);

/** DAC Channel 1 task
 * @param x: Task handle
 * @note This is a one shot task that plays the waveform on DAC channel 1
 */
void DAC_ch1_task(void *x);

/** DAC Channel 2 task
 * @param x: Task handle
 * @note This is a one shot task that plays the waveform on DAC channel 2
 */
void DAC_ch2_task(void *x);

/* USER CODE END Prototypes */

#ifdef __cplusplus
}
#endif

#endif /* __DAC_H__ */
