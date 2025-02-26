/*
 * Home Security System
 * Interface to the utility parsing module.
 */
#ifndef HS_UTIL
#define HS_UTIL 0

/*
 * Read the next line of text into 'buffer'. At most 'max' characters
 * will be read; characters after this limit are silently discarded.
 * A NUL ('\0') character is placed after the last character in buffer,
 * so its size must be at least (max + 1) characters.
 *
 * Returns the number of characters placed in the buffer or EOF if
 * end of file is detected.
 */
int read_line(char* buffer, int maxSize);

/*
 * Parse the string in 'str' using the CSV format from the problem spec.
 * Assumes 'str' is a proper C string ('\0' terminated).
 * Assumes 'str' is valid csv format per spec
 * 
 */
int parse_string(char* str, struct reading_t* reading);


#endif
