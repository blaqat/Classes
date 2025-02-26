/*   printf.c   */
#include "printf.h"
#include "UART.h"
#include <stdarg.h>
#include <stdio.h>
#include <string.h>

#define BUFFER_MAX (80)
static uint8_t buffer[BUFFER_MAX];

int puts (const char* string) {
    int n = sprintf ((char*) buffer, "%s\n", string);
    USART_Write (USART2, buffer, n);
    return n;
}

int printf (const char* format, ...) {

	va_list aptr;
	int n;

	va_start(aptr, format);
	n = vsprintf((char*)buffer, format, aptr);
	va_end(aptr);

	USART_Write (USART2, buffer, n);

	return n;
}

void printlf(const char* format, ...) {
	printf(format);
	printf("\n\r");
}
