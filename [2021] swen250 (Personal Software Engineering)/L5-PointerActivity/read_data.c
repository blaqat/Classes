/*
 * Implementation of the read_data module.
 *
 * See read_data.h for a description of the read_data function's
 * specification.
 *
 * Note that the parameter declarations in this module must be
 * compatible with those in the read_data.h header file.
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "read_data.h"

// Reads the three fields from lines such as W$1349$1.414$ into
// local variables using getchar().
// Converts the two numeric fields to numbers using atoi for the
// integer fields and atof for the double.
// Using a correctly defined set of parameters (use pointers) pass
// those values back to the program that called this function.
void read_data(char* chr, int* num, double* dec){
	char str[30];
	int c = 0;
	int typ = 0;

	for(char got = ' '; got != EOF && got != '\n' && got != '\0'; got = getchar()){
		if(got == '$'){
			if(typ == 0){
				*chr = str[1];
			} else if(typ == 1){
				*num = atoi(str);
			} else if(typ == 2){
				*dec = atof(str);
			}
			char str[30];
			c = 0;
			typ++;
		}
		else {
			str[c] = got;
			c++;
		}
		
	}
	
	return ;
}
