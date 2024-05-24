/* USER CODE BEGIN Header */
/**
 ******************************************************************************
 * @file    adc.h
 * @brief   This file contains all the function prototypes for
 *          the adc.c file
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
#ifndef __ADC_H__
#define __ADC_H__

#ifdef __cplusplus
extern "C" {
#endif

/* Includes ------------------------------------------------------------------*/
#include "main.h"

/* USER CODE BEGIN Includes */
extern _Bool adc_ready;
/* USER CODE END Includes */

extern ADC_HandleTypeDef hadc1;
extern DMA_HandleTypeDef hdma_adc1;
extern uint16_t capture_buffer[20000];

/* USER CODE BEGIN Private defines */

/* USER CODE END Private defines */

void MX_ADC1_Init(void);

/**
 * @brief Start the ADC capture
 * @note This function starts the timer to trigger the ADC
 */
void ADC_Start_Capture();

/**
 * @brief Stop the ADC capture
 */
void ADC_Stop_Capture();

/**
 * @brief Get the captured data
 * @return uint16_t*, pointer to the captured data
 */
uint16_t *ADC_Get_Captured();

/**
 * @brief This task is responsible for capturing the ADC data
 * and processing it.
 * It is a oneshot task that waits for the ADC to be ready.
 */
void ADC_task();

/* USER CODE BEGIN Prototypes */

/* USER CODE END Prototypes */

#ifdef __cplusplus
}
#endif

#endif /* __ADC_H__ */
