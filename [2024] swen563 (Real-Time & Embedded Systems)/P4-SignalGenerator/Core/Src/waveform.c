/**
 * @file waveform.c
 * @brief This file contains the implementation of the waveform generator and
 * related functions.
 ******************************************************************************
 * The waveform generator module is responsible for generating different types
 * of waveforms, including sine, square, triangular, and EKG-like waveforms. It
 * also provides functions to generate waveforms based on user input.
 *
 *  Created on: Apr 8, 2024
 *      Author: Aiden Green
 */
#include "waveform.h"
#include <math.h>

#define BITS_PS WF_LEN

// Provided waveform table
const uint16_t wf_ekg_table[] = {
    1690, 1680, 1680, 1669, 1648, 1648, 1648, 1680, 1680, 1690, 1680, 1680,
    1680, 1680, 1680, 1658, 1690, 1690, 1712, 1690, 1690, 1680, 1669, 1669,
    1680, 1690, 1690, 1680, 1669, 1669, 1669, 1680, 1680, 1680, 1669, 1669,
    1680, 1690, 1690, 1680, 1690, 1690, 1680, 1690, 1690, 1712, 1680, 1680,
    1658, 1648, 1648, 1648, 1669, 1669, 1680, 1690, 1690, 1701, 1680, 1680,
    1669, 1680, 1680, 1680, 1701, 1701, 1701, 1690, 1690, 1701, 1712, 1712,
    1722, 1712, 1712, 1690, 1669, 1669, 1680, 1690, 1690, 1690, 1733, 1733,
    1765, 1776, 1861, 1882, 1936, 1936, 1968, 1989, 1989, 2032, 2053, 2053,
    2085, 2149, 2069, 2080, 2058, 2058, 1930, 1930, 1845, 1824, 1792, 1872,
    1840, 1754, 1754, 1722, 1680, 1680, 1680, 1637, 1637, 1637, 1637, 1637,
    1626, 1648, 1648, 1637, 1605, 1605, 1616, 1680, 1680, 1765, 1776, 1861,
    2042, 2106, 2021, 1776, 2480, 2400, 2176, 1632, 1637, 1360, 933,  928,
    1962, 1962, 2042, 2149, 3141, 3141, 2320, 1200, 1200, 1392, 1669, 1669,
    1658, 1701, 1701, 1701, 1701, 1701, 1722, 1690, 1690, 1690, 1680, 1680,
    1690, 1690, 1690, 1669, 1669, 1669, 1701, 1733, 1733, 1754, 1744, 1744,
    1733, 1733, 1733, 1722, 1765, 1765, 1765, 1733, 1733, 1733, 1722, 1722,
    1701, 1690, 1690, 1701, 1690, 1690, 1701, 1701, 1701, 1701, 1722, 1722,
    1712, 1722, 1722, 1733, 1733, 1733, 1733, 1712, 1712, 1712, 1733, 1733,
    1733, 1733, 1733, 1733, 1744, 1744, 1744, 1744, 1744, 1744, 1733, 1733,
    1722, 1722, 1722, 1722, 1722, 1722, 1733, 1722, 1722, 1722, 1722, 1722,
    1701, 1669, 1669, 1680, 1690, 1690, 1690, 1701, 1701, 1712, 1712, 1712,
    1690, 1669, 1669, 1680,
};

/**
 * @brief Generates a square waveform.
 * Fills the buffer with rectangular waveform values based on min and max
 * voltages.
 * @param buffer Pointer to the buffer to store waveform values.
 * @param min_volt Minimum voltage of the waveform.
 * @param max_volt Maximum voltage of the waveform.
 */
void wf_gen_sq(uint16_t *buffer, float min_volt, float max_volt) {
  int half = BITS_PS / 2;
  for (int i = 0; i < half; i++) {
    buffer[i] = VOLT_CONV_TR(min_volt);
    buffer[i + half] = VOLT_CONV_TR(max_volt);
  }
}

/**
 * @brief Generates a DC waveform.
 * Fills the buffer with DC waveform values based on min voltage.
 * @param buffer Pointer to the buffer to store waveform values.
 * @param min_volt Minimum voltage of the waveform.
 */
void wf_gen_dc(uint16_t *buffer, float min_volt) {
  uint16_t dig_min = VOLT_CONV_TR(min_volt);
  for (int i = 0; i < BITS_PS; i++) {
    buffer[i] = dig_min;
  }
}

/**
 * @brief Generates a triangular waveform.
 * Fills the buffer with triangular waveform values based on min and max
 * voltages.
 * @param buffer Pointer to the buffer to store waveform values.
 * @param min_volt Minimum voltage of the waveform.
 * @param max_volt Maximum voltage of the waveform.
 */
void wf_gen_tri(uint16_t *buffer, float min_volt, float max_volt) {
  uint16_t dig_max = VOLT_CONV_TR(max_volt);
  uint16_t dig_min = VOLT_CONV_TR(min_volt);
  uint16_t half = BITS_PS / 2;
  double incr = ((double)dig_max - (double)dig_min) / half;
  for (int i = 0; i < half; i++) {
    buffer[i] = (uint16_t)((double)dig_min + (double)(incr * i));
    buffer[i + half] = (uint16_t)((double)dig_max - (double)(incr * i));
  }
  buffer[BITS_PS - 1] = (uint16_t)(dig_min);
}

/**
 * @brief Generates a sine waveform.
 * Fills the buffer with sine waveform values based on min and max voltages.
 * @param buffer Pointer to the buffer to store waveform values.
 * @param min_volt Minimum voltage of the waveform.
 * @param max_volt Maximum voltage of the waveform.
 */
void wf_gen_sine(uint16_t *buffer, float min_volt, float max_volt) {
  float amplitude_digital = VOLT_CONV(max_volt - min_volt) / 2;
  float offset_digital = VOLT_CONV(min_volt);

  for (int i = 0; i < BITS_PS; i++) {
    double rad = i * 2 * 3.14 / BITS_PS;
    buffer[i] =
        (uint16_t)round(((sin(rad) + 1) * amplitude_digital) + offset_digital);
  }
}

/**
 * @brief Generates an EKG-like waveform.
 * Fills the buffer with EKG-like waveform values.
 * @param buffer Pointer to the buffer to store waveform values.
 */
void wf_gen_ekg(uint16_t *buffer) {
  waveform_copy_into(buffer, (uint16_t *)wf_ekg_table, WF_LEN);
}

/**
 * @brief Generates a waveform of a specified type.
 * Creates sine, square, triangular, or EKG-like waveforms based on parameters.
 * @param buffer Pointer to the buffer to store waveform values.
 * @param type Character indicating the type of waveform to generate.
 * @param min_volt Minimum voltage of the waveform.
 * @param max_volt Maximum voltage of the waveform.
 */
void waveform_generate(uint16_t *buffer, char type, float min_volt,
                       float max_volt) {
  switch (type) {
  case WF_SINE:
    wf_gen_sine(buffer, min_volt, max_volt);
    break;
  case WF_SQ:
    wf_gen_sq(buffer, min_volt, max_volt);
    break;
  case WF_TRI:
    wf_gen_tri(buffer, min_volt, max_volt);
    break;
  case WF_EKG:
    wf_gen_ekg(buffer);
    break;
  case WF_DC:
    wf_gen_dc(buffer, min_volt);
    break;
  }
}

/**
 * @brief Copies one waveform buffer to another.
 * Copies the contents of one buffer to another.
 * @param buffer Pointer to the buffer to copy from.
 *  @param copy Pointer to the buffer to copy to.
 */
void waveform_copy_into(uint16_t *buffer, uint16_t *copy, uint32_t len) {
  for (uint32_t i = 0; i < len; i++) {
    buffer[i] = copy[i];
    fprintln("%d %d", buffer[i], copy[i]);
  }
}

/**
 * @brief Converts a character to a waveform string.
 * Interprets a character and returns its corresponding waveform string.
 * @param chr Character to be converted.
 * @return String representing the waveform.
 */
string char_to_wf_string(char chr) {
  switch (chr) {
  case WF_SINE:
    return "Sine";
  case WF_SQ:
    return "Square";
  case WF_TRI:
    return "Triangle";
  case WF_EKG:
    return "Arbitrary";
  case WF_DC:
    return "DC";
  case WF_CAP:
    return "Captured";
  default:
    return "Unknown";
  }
}
