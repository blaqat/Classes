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
#define INIT_STRUCT(t) (t *)malloc(sizeof(t))

#define true 1
#define false 0

// Typedefs
#define string char *
typedef _Bool bool;
typedef uint8_t byte;

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
uint32_t sec_to_MHz(string unit);

// String Output functions
// f - formatted
// ln - prints with \r\n at the end
void clear();
void put(char c);
void print(string message);
void println(string message);
void sym(string message);
void info(string message);
void warn(string message);
void error(string message);
string format(string format, ...);
void fprint(string message, ...);

/**
 * @brief Print a formatted string to the console with a newline
 *
 * @param message
 * @varargs, format arguments
 */
void fprintln(string message, ...);
void readln(void *buffer, int max_length);
void read(string buffer, int *index);
void readc(uint8_t *buffer);

// User input functions
// f - formatted message
// ln - reads user input until \n
char input(string message);
string inputln(string message);
char finput(string message, ...);
string finputln(string message, ...);

// String manipulation functions
int parse_int(string str);
string col_str(string color, string message, ...);
char to_lower(char c);
#define STR_EQ(s1, s2) (strcmp(s1, s2) == 0)

// Math functions
#define RANGE(x, min, max) (x >= min && x <= max)
_Bool range(int x, int min, int max);
#endif /* INC_UTILS_H_ */
