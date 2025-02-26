// C pointers Practicum with malloc and free
// SWEN-250
// Larry Kiser October 30, 2015
// Larry Kiser updated to summer version July 11, 2017
// Larry Kiser (LLK) updated March 20, 2018

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include "cpracticum2.h"
#include "unit_tests.h"

// Determine if the p_unknown pointer points to one of the integers in the  array of integers.
// You need to use num_int_in_array -- this is the number of integers in the p_int_array array.
// return 1 if it does, return 0 otherwise.
// For example, if p_int_array has 3 entries then p_unknown points to one of the integers
// in the array if it points to the first, second, or third entry.
// Hint -- use pointer arithmetic to solve this.
int is_pointer_in_array( int *p_int_array, int *p_unknown, int num_int_in_array )
{
	// your code here
	for(int i = 0; i < num_int_in_array; i++){
		if(p_int_array == p_unknown)
			return 1;
		p_int_array++;
	}

	return 0;	// Fix this -- it is incorrect but allows the program to compile and run.
}

// The first time this is called return the first unsigned char in the mylist array.
// The second time this is called return the unsigned char in the mylist array.
// On subsequent calls return the next entry in mylist.
// HOWEVER, after you return the last item in the array the next call must return
// the first element in the array.
unsigned char my_random()
{
	unsigned char mylist[] = { 5, 9, 254, 129, 55, 8, 32, 99, 207 } ; // DO NOT change this array!
	static int index = 0;    // permanent storage that is initialized only once
							 // retains value if changed on subsequent calls to this function
	// your code here
	if(index >= 9)
		index = 0;

	return mylist[index++];	// Fix this -- it is incorrect but allows the program to compile and run.
}

// Using malloc create an array that contains the product of the each element in *p_first with the
// corresponding element in *p_second and store that sum in the corresponding element in the
// returned array of integers.
// e.g. p_result[0] = p_first[0] * p_second[0]; p_result[1] = p_first[1] * p_second[1]
// Note -- this array must be the same size as the passed in arrays.
// Note that you can assume that the p_first and p_second arrays are the same size.
// The third parameter is the number of entries in each array.
// Return NULL if either array pointer is NULL.
// Return NULL if the number of entries is <= 0.
int *create_array_of_products( int *p_first, int *p_second, int number_of_entries )
{
	int *p_mult = malloc(number_of_entries * 4);

	if(!p_first || !p_second || number_of_entries <= 0)
		return NULL;

	for(int i = 0; i < number_of_entries; i++){
		p_mult[i] = p_first[i] * p_second[i];
	}

	return p_mult;	// Fix this -- this value causes all unit tests to fail.
}

// Calculate the total of all of the entries in the passed array and return that total.
// Return a zero if the passed array (pointer) is NULL or the number of entries
// is less than or equal to 0.
// Only free the passed pointer if it is not NULL and the number of entries is greater than 0.
// hint: be sure to free the passed pointer only as described above!
int get_total_and_free( int *p_array, int number_of_entries )
{
	int sum = 0;

	if(!p_array || number_of_entries <= 0)
		return 0;

	for(int i = 0; i < number_of_entries; i++){
		sum += p_array[i];
	}
	
	free(p_array);

	return sum;		// Fix this -- causes all unit tests to fail.
}

// This function is implemented incorrectly. You need to correct it.
// When fixed it returns 1 if there are any digits (0 through 9) anywhere in the passed string.
// If there are no digits it returns 0.
// It also returns 0 if the passed string pointer is NULL or if the string points to an empty string.
// You can re-write this code completely if you prefer.
int fix_bad_code( char *pstring )
{
	int result = 0;	// is this a good choice for initialization?
	
	// Fix this next line so it correctly returns 0 if the passed pointer is NULL or
	// if the pointer points to an empty string.
	if ( ( ( pstring == NULL ) ) || *pstring == '\0' )
		return 0;
	
	// does this loop work correctly?
	while ( *pstring != '\0' )
		if ( isdigit( *pstring++ ) )	// isdigit returns true if the passed character is a number 0 through 9
			result = 1 ;
		
	return result;
}
