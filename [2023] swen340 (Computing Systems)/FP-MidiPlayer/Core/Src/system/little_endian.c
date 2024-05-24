/*   interpreter.c   */
#include "little_endian.h"
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>


uint16_t convert_to_uint16 (uint8_t* ptr) {
    uint16_t num = (ptr[0] << 8) + ptr [1];
    return num;
}

uint32_t convert_to_uint32 (uint8_t* ptr) {
    uint32_t num = (ptr[0] << 24) + (ptr [1] << 16) + (ptr[2] << 8) + ptr[3];
    return num;
}

unsigned int convert_to_uint24 (uint8_t* ptr) {
    unsigned int num = (unsigned int)malloc(sizeof(uint8_t) * 3);
	num = (ptr [0] << 16) + (ptr[1] << 8) + ptr [2];
    return num;
}

Header* convert_header (Header* h) {
	h->division = convert_to_uint16((uint8_t*)&h->division);
	h->format = convert_to_uint16((uint8_t*)&h->format);
	h->tracks = convert_to_uint16((uint8_t*)&h->tracks);
	h->len = convert_to_uint32((uint8_t*)&h->len);

	return h;
}

