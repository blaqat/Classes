#ifndef INC_UTILS_H_
#define INC_UTILS_H_

#include <stdint.h>

#define SECOND_TO_MHZ 1000000
#define MILLISECOND_TO_MHZ 1000
#define MHZ_MULTIPLIER 80

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
void fprintln(char *message, ...);
void readln(void *buffer, int max_length);

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

#endif /* INC_UTILS_H_ */
