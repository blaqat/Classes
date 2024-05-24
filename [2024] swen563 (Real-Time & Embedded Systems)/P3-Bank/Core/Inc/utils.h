/*
 * utils.h
 *
 *  Created on: Feb 12, 2024
 *      Author: Aiden Green
 */
#ifndef INC_UTILS_H_
#define INC_UTILS_H_

#include <stdarg.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define SECOND_TO_MHZ 1000000
#define MILLISECOND_TO_MHZ 1000
#define MHZ_MULTIPLIER 80
#define MAX(a, b) (a > b ? a : b)
#define MIN(a, b) (a < b ? a : b)
#define UPDATE_AVG(i, old, new) ((old * (i) + (new)) / ((i) + 1))

// Enumerations
typedef enum {
  UNIT_K,
  UNIT_H,
  UNIT_D,
  UNIT_U,
  UNIT_d,
  UNIT_c,
  UNIT_m,
  UNIT_i,
  UNIT_UNKNOWN
} Unit;

// Function prototypes
void close();
int rand_int(int min, int max);
void order(uint32_t *a, uint32_t *b);
int gen_random(int min, int max);

// Unit conversion functions
uint32_t convert(uint32_t x, const char unit_1, const char unit_2);
uint32_t sec_to_MHz(char *unit);

// String Output functions
// f - formatted
// ln - prints with \r\n at the end
void clear();
void put(char c);
void print(char *message);
void println(char *message);
void sym(char *message);
void info(char *message);
void warn(char *message);
void error(char *message);
char *format(char *format, ...);
void fprint(char *message, ...);

/**
 * @brief Print a formatted string to the console with a newline
 *
 * @param message
 * @varargs, format arguments
 */
void fprintln(char *message, ...);
void readln(void *buffer, int max_length);
void read(char *buffer, int *index);

// User input functions
// f - formatted message
// ln - reads user input until \n
char input(char *message);
char *inputln(char *message);
char finput(char *message, ...);
char *finputln(char *message, ...);

// String manipulation functions
int parse_int(char *string);
char *col_str(char *color, char *message, ...);
char to_lower(char c);
_Bool range(int x, int min, int max);

int gen_random(int min, int max);
#endif /* INC_UTILS_H_ */
