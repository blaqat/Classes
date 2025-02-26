/*
 * Implementation of functions that find values in strings.
 *****
 * YOU MAY NOT USE ANY FUNCTIONS FROM <string.h>
 *****
 */

#include <stdlib.h>
#include <stdbool.h>
#include <stdio.h>
#include "find.h"

#define NOT_FOUND (-1)	// integer indicator for not found.

/*
 * Return the index of the first occurrence of <ch> in <string>,
 * or (-1) if the <ch> is not in <string>.
 */
int find_ch_index(char string[], char ch) {
	for(int i = 0; string[i] != '\0'; i++){
		if(string[i] == ch)
			return i;
	}
	return NOT_FOUND ;	// placeholder
}

/*
 * Return a pointer to the first occurrence of <ch> in <string>,
 * or NULL if the <ch> is not in <string>.
 *****
 * YOU MAY *NOT* USE INTEGERS OR ARRAY INDEXING.
 *****
 */
char *find_ch_ptr(char *string, char ch) {
	
	while(*string != '\0'){
		if(*string == ch)
			return string;
		string++;
	}
	
	return NULL ;	// placeholder
}

/*
 * Return the index of the first occurrence of any character in <stop>
 * in the given <string>, or (-1) if the <string> contains no character
 * in <stop>.
 */
int find_any_index(char string[], char stop[]) {
	for(int i = 0; string[i] != '\0'; i++){
		if(find_ch_index(stop, string[i]) != NOT_FOUND)
			return i;
	}
	return NOT_FOUND ;	// placeholder
}

/*
 * Return a pointer to the first occurrence of any character in <stop>
 * in the given <string> or NULL if the <string> contains no characters
 * in <stop>.
 *****
 * YOU MAY *NOT* USE INTEGERS OR ARRAY INDEXING.
 *****
 */
char *find_any_ptr(char *string, char* stop) {
	do {
		if(find_ch_ptr(stop, *string) != NULL)
			return string;
		string++;
	}
	while(*string != '\0');

	return NULL ;	// placeholder
}

/*
 * Return a pointer to the first character of the first occurrence of
 * <substr> in the given <string> or NULL if <substr> is not a substring
 * of <string>.
 * Note: An empty <substr> ("") matches *any* <string> at the <string>'s
 * start.
 *****
 * YOU MAY *NOT* USE INTEGERS OR ARRAY INDEXING.
 *****
 */
char *find_substr(char *string, char* substr) {

	char *start = string;
	char *prev = substr;
	char *found = find_ch_ptr(substr, *string);

	do {
		string++;

		if(*prev == '\0')
			return start;
		if(*found == *prev){
			prev++;
		}
		else {
			prev = substr;
			start = string;
		}

		found = find_ch_ptr(substr, *string);
	} while(*string != '\0' && found != NULL || *prev == '\0');

	return NULL ;	// placeholder
}
