/*
 * utils.c
 *
 *  Created on: Feb 12, 2024
 *      Author: Aiden Green
 */

#include "utils.h"
#include "usart.h"

/**
 * @brief Close the program
 */
void close() {
  error("Exiting program");
  exit(1);
}

/**
 * @brief Generate a random integer between min and max
 *
 * @param min
 * @param max
 * @return int
 */
int rand_int(int min, int max) { return (rand() % (max - min + 1)) + min; }

/**
 * @brief Swap two integers
 *
 * @param a
 * @param b
 */
void order(uint32_t *a, uint32_t *b) {
  if (*a < *b) {
    int temp = *a;
    *a = *b;
    *b = temp;
  }
}

/**
 * @brief Convert a time unit to MHz
 *
 * @param unit
 * @return uint32_t
 */
Unit get_unit(const char unit) {
  switch (unit) {
  case 'K':
    return UNIT_K;
  case 'H':
    return UNIT_H;
  case 'D':
    return UNIT_D;
  case 'U':
    return UNIT_U;
  case 'd':
    return UNIT_d;
  case 'c':
    return UNIT_c;
  case 'm':
    return UNIT_m;
  case 'i':
    return UNIT_i;
  default:
    return UNIT_UNKNOWN;
  }
}

/**
 * @brief Convert between units
 *
 * @param x, value to convert
 * @param unit_1, unit to convert from
 * @param unit_2, unit to convert to
 * @return uint32_t
 */
uint32_t convert(uint32_t x, const char unit_1, const char unit_2) {
  Unit u1 = get_unit(unit_1);
  Unit u2 = get_unit(unit_2);

  if (u1 == UNIT_UNKNOWN || u2 == UNIT_UNKNOWN) {
    // Handle error for unknown units
    return 0;
  }

  // Conversion factors for upscaling
  const uint32_t upscale_factors[][8] = {
      // K, H, D, U, d, c, m, i
      {1, 10, 100, 1000, 10000, 100000, 1000000, 1000000000}, // From K
      {0, 1, 10, 100, 1000, 10000, 100000, 100000000},        // From H
      {0, 0, 1, 10, 100, 1000, 10000, 10000000},              // From D
      {0, 0, 0, 1, 10, 100, 1000, 1000000},                   // From U
      {0, 0, 0, 0, 1, 10, 100, 100000},                       // From d
      {0, 0, 0, 0, 0, 1, 10, 10000},                          // From c
      {0, 0, 0, 0, 0, 0, 1, 1000},                            // From m
      {0, 0, 0, 0, 0, 0, 0, 1}                                // From i
  };

  // Check if direct conversion is possible
  if (upscale_factors[u1][u2] != 0) {
    return x * upscale_factors[u1][u2];
  } else {
    // Inverse conversion (downscaling)
    return x / upscale_factors[u2][u1];
  }
}

/**
 * @brief Convert a time unit to MHz
 *
 * @param unit
 * @return uint32_t
 */
uint32_t sec_to_MHz(string unit) {
  uint32_t mhz = 0;
  if (strcmp(unit, "s") == 0) {
    mhz = SECOND_TO_MHZ;
  } else if (strcmp(unit, "ms") == 0) {
    mhz = MILLISECOND_TO_MHZ;
  } else if (strcmp(unit, "mi") == 0) {
    mhz = 1;
  }

  return mhz * MHZ_MULTIPLIER;
}

/**
 * @brief Get the color code for a color
 *
 * @param color
 * @return uint32_t
 */
uint32_t get_color(string color) {
  if (strcmp(color, "black") == 0) {
    return 30;
  } else if (strcmp(color, "red") == 0) {
    return 31;
  } else if (strcmp(color, "green") == 0) {
    return 32;
  } else if (strcmp(color, "yellow") == 0) {
    return 33;
  } else if (strcmp(color, "blue") == 0) {
    return 34;
  } else if (strcmp(color, "magenta") == 0) {
    return 35;
  } else if (strcmp(color, "cyan") == 0) {
    return 36;
  } else if (strcmp(color, "white") == 0) {
    return 37;
  } else {
    return 0;
  }
}

/**
 * @brief Format a string with a color
 *
 * @param color
 * @param message
 * @varargs, format arguments
 * @return char*
 */
string col_str(string color, string message, ...) {
  char buffer[100];
  va_list args;
  va_start(args, message);
  vsprintf(buffer, message, args);
  va_end(args);

  string f = format("\x1B[%dm%s\x1B[0m", get_color(color), buffer);
  return f;
}

/**
 * @brief Clear the console
 */
// void clear() { print("\x1B[2J"); }
void clear() { print("\033[2J\033[1;1H"); }

/**
 * @brief Print a character to the console
 *
 * @param c
 */
// void put(char c) { USART_Write(USART2, (uint8_t *)&c, 1); }
void put(char c) {
  string buffer = (string)malloc(2);
  buffer[0] = c;
  buffer[1] = '\0';
  USART_Transmit(buffer);
  free(buffer);
}

/**
 * @brief Print a message to the console
 *
 * @param message
 */
// void print(string message) {
//   USART_Write(USART2, (uint8_t *)message, strlen(message));
// }
void print(string message) { USART_Transmit(message); }

/**
 * @brief Print a message to the console with a newline
 *
 * @param message
 */
void println(string message) {
  print(message);
  print("\n\r");
}

/**
 * @brief Print a message to the console in magenta, then waits for user
 * validation
 *
 * @param message
 */
void sym(string message) { input(col_str("magenta", message)); }

/**
 * @brief Print a message to the console in green
 *
 * @param message
 */
void info(string message) { println(col_str("green", message)); }

/**
 * @brief Print a message to the console in yellow
 *
 * @param message
 */
void warn(string message) { println(col_str("yellow", message)); }
/**
 * @brief Print a message to the console in red
 *
 * @param message
 */
void error(string message) { println(col_str("red", message)); }

/**
 * @brief Format a string
 *
 * @param format
 * @varargs, format arguments
 * @return char*
 */
string format(string format, ...) {
  string buffer = (string)malloc(100);

  va_list args;
  va_start(args, format);
  vsprintf(buffer, format, args);
  va_end(args);

  return buffer;
}

/**
 * @brief Print a formatted string to the console.
 *
 * @param message
 * @varargs, format arguments
 */
void fprint(string message, ...) {
  char buffer[100];
  va_list args;
  va_start(args, message);
  vsprintf(buffer, message, args);
  va_end(args);
  print(buffer);
}

/**
 * @brief Print a formatted string to the console with a newline
 *
 * @param message
 * @varargs, format arguments
 */
void fprintln(string message, ...) {
  char buffer[100];
  va_list args;
  va_start(args, message);
  vsprintf(buffer, message, args);
  va_end(args);
  println(buffer);
}

void readc(uint8_t *buffer) { USART_Recieve(buffer, 1); }

void read(string buffer, int *index) {

  char c = USART_Read_Async(USART2);

  // Check for end of data or read error
  if (c == '\0') {
    return;
  } else if (c == '\n' || c == '\r') {
    buffer[*index] = '\r';
    buffer[++(*index)] = '\n';
    buffer[++(*index)] = '\0';
    print("\n\r");
    return;
  } else if (c == '\b' || c == 127) {
    if (index > 0) {
      buffer[*index] = '\0';
      (*index)--;
    }
    print("\b \b");
  } else {
    buffer[*index] = c;
    (*index)++;
    put(c);
  }

  // Null-terminate the string
  buffer[*index] = '\0';
}

/**
 * @brief Captures 1 line of text from console. Returns nul terminated
 * string when \n is entered
 *
 * @param buffer
 * @param max_length
 */
void readln(void *buffer, int max_length) {
  int b_length = 0;
  string message = (string)buffer;
  char c;

  // Set text color to blue
  fprint("\x1B[%dm", get_color("blue"));

  // Read characters until newline or max length
  while (b_length < max_length) {
    c = USART_Read_Async(USART2);

    // Handle backspace and delete press
    if (c == '\b' || c == 127) {
      if (b_length > 0) {
        b_length--;
        message--;
      }
      print("\b \b");
      // Handle enter and return press
    } else if (c == '\n' || c == '\r') {
      print("\n\r");
      break;
      // Add other characters to the buffer
    } else {
      *message = c;
      message++;
      b_length++;
      put(c);
    }
  }
  *message = '\0';

  // Reset color
  print("\x1B[0m");
  return;
}

/**
 * @brief Prompts user for an input, then captures 1 character from console
 *
 * @param message
 * @return char*
 */
char input(string message) {
  println(message);
  return USART_Read(USART2);
}

/**
 * @brief Prompts user for an input with a formatted message, then captures
 * 1 character from console
 *
 * @param message
 * @return char*
 */
char finput(string message, ...) {
  char buffer[100];
  va_list args;
  va_start(args, message);
  vsprintf(buffer, message, args);
  va_end(args);
  return input(buffer);
}

/**
 * @brief Prompts user for an input, then captures 1 line of text from
 * console
 *
 * @param message
 * @return char*
 */
string inputln(string message) {
  println(message);
  string buffer = (string)malloc(100);
  readln(buffer, 100);
  return buffer;
}

/**
 * @brief Prompts user for an input with a formatted message, then captures
 * 1 line of text from console
 *
 * @param message
 * @return char*
 */
string finputln(string message, ...) {
  char buffer[100];
  va_list args;
  va_start(args, message);
  vsprintf(buffer, message, args);
  va_end(args);
  return inputln(buffer);
}

/**
 * @brief Parse an integer from a string
 *
 * @param string
 * @return int
 */
int parse_int(string str) {
  char fchar;
  int num;
  return sscanf(str, "%d%c", &num, &fchar) == 1 ? num : -1;
};

char to_lower(char c) {
  if (c >= 'A' && c <= 'Z') {
    return c + 32;
  }
  return c;
}

_Bool range(int x, int min, int max) { return x >= min && x <= max; }
