
/*
 * waveform.h
 *
 *  Created on: Apr 7, 2024
 *      Author: Aiden Green
 */
#ifndef __WAVEFORM_H__
#define __WAVEFORM_H__

#include "utils.h"
#include <stdint.h>

// Definitions
#define DAC_RESOLUTION 4095
#define WF_LEN 256
#define WF_CAP_LEN 20000
#define MAX_VOLTAGE (float)3.3
#define MIN_VOLTAGE 0
#define MAX_NOISE 12
#define MIN_NOISE 0
#define MAX_FREQ 10000
#define MIN_FREQ -5

// Waveform types
#define WF_SINE 's'
#define WF_TRI 't'
#define WF_EKG 'a'
#define WF_SQ 'r'
#define WF_DC 'd'
#define WF_CAP 'c'

#define DIG_CONV DAC_RESOLUTION / MAX_VOLTAGE
#define VOLT_CONV(x) (((float)x) * (DIG_CONV))
#define VOLT_CONV_TR(x) (uint16_t)((float)x * DIG_CONV)
#define ADC_CONV(x) ((((float)x) * MAX_VOLTAGE) / DAC_RESOLUTION)

/**
 * @brief Generates a waveform of a specified type.
 * Creates sine, square, triangular, or EKG-like waveforms based on parameters.
 * @param buffer Pointer to the buffer to store waveform values.
 * @param type Character indicating the type of waveform to generate.
 * @param min_volt Minimum voltage of the waveform.
 * @param max_volt Maximum voltage of the waveform.
 */
void waveform_generate(uint16_t *buffer, char type, float min_volt,
                       float max_volt);

/**
 * @brief Copies one waveform buffer to another.
 * Copies the contents of one buffer to another.
 * @param buffer Pointer to the buffer to copy from.
 *  @param copy Pointer to the buffer to copy to.
 */
void waveform_copy_into(uint16_t *buffer, uint16_t *copy, uint32_t len);

/**
 * @brief Converts a character to a waveform string.
 * Interprets a character and returns its corresponding waveform string.
 * @param chr Character to be converted.
 * @return String representing the waveform.
 */
string char_to_wf_string(char chr);

#endif /*__WAVEFORM_H__ */
