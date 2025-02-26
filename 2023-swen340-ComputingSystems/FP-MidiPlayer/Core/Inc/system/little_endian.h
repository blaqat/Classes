/*
 * little_endian.h
 *
 *  Created on: Marsh 23, 2023
 *      Author: Aiden Green
 *
 */

#ifndef INC_LITTLE_ENDIAN_H_
#define INC_LITTLE_ENDIAN_H_

#include <stdint.h>
#include "interpreter.h"

uint16_t convert_to_uint16 (uint8_t* p_value);
uint32_t convert_to_uint32 (uint8_t* p_value);
unsigned int convert_to_uint24 (uint8_t* p_value);
Header* convert_header (Header* h);

#endif /* INC_LITTLE_ENDIAN_H_ */
