// unit_tests.c
// Larry Kiser Nov. 29, 2015
// Larry Kiser July 11, 2017 removed stdout switching code

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdarg.h>
#include <unistd.h>

#include "cpracticum2.h"
#include "unit_tests.h"

// Simple boolean assert function for unit testing
int assert( int test_result, char error_format[], ... ) {
	va_list arguments ;
	static int test_number = 1 ;
	int result = 1 ;	// return 1 for test passed or 0 if failed
	
	if ( ! test_result ) {
		va_start( arguments, error_format ) ;
		printf( "Test # %d failed: ", test_number ) ;
		vprintf( error_format, arguments ) ;
		printf( "\n" ) ;
		result = 0 ;
	}
	test_number++ ;
	return result ;
}

//////////////////////////////////////////////////////////////////////////
// Begin unit tests //////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////

// do the unit tests
int test()
{
	int passcount = 0 ;
	int failcount = 0 ;
	
	int result = 0 ;
	
	printf( "\nTests for is_pointer_in_array.......................\n" ) ;
	// Test 1
	int first_array[] = { 1, 2, 3, 4, 5, 6 } ;

	result = is_pointer_in_array( first_array, first_array, sizeof(first_array) / sizeof(int) ) ;
	assert( result == 1,
		"Points to the first position in the array so expected a 1 but got a %d",  result )
		? passcount++ : failcount++ ;
	// Test 2
	result = is_pointer_in_array( first_array, &first_array[5], sizeof(first_array) / sizeof(int) ) ;
	assert( result == 1,
		"Points to the last position in the array so expected a 1 but got a %d",  result )
		? passcount++ : failcount++ ;
	// Test 3
	result = is_pointer_in_array( first_array, &first_array[6], sizeof(first_array) / sizeof(int) ) ;
	assert( result == 0,
		"Points to an invalid position just past the end of the array so expected a 0 but got a %d",  result )
		? passcount++ : failcount++ ;

	printf( "\nTests for my_random()...............................\n" ) ;
	// Test 4
	unsigned char answer ;
	answer = my_random() ;
	assert( answer == 5,
		"First random number must be 5 but got a %u",  answer )
		? passcount++ : failcount++ ;
	// Test 5
	answer = my_random() ;
	assert( answer == 9,
		"Second random number must be 9 but got a %u",  answer )
		? passcount++ : failcount++ ;
	// Test 6
	my_random() ;	// skip next five numbers
	my_random() ;
	my_random() ;
	my_random() ;
	my_random() ;
	answer = my_random() ;
	assert( answer == 99,
		"Last random number must be 99 but got a %u",  answer )
		? passcount++ : failcount++ ;
	// Test 7
	my_random() ;		// skip one more to wrap around
	answer = my_random() ;
	assert( answer == 5,
		"Must now be back at the first random number of 5 but got a %u",  answer )
		? passcount++ : failcount++ ;

	printf( "\nTests for create_array_of_products..........................\n" ) ;
	// Test 8 -- expect NULL pointer
	int *p_products = NULL ;
	//int first_array[] = { 1, 2, 3, 4, 5, 6 } ; commented out copy of previous declaration for convenience
	int second_array[] = { 5, 6, 7, 8, 9, 10 } ;

	p_products = create_array_of_products( first_array, NULL, sizeof( first_array ) / sizeof( int ) ) ;
	assert( p_products == NULL,
		"Expected a NULL pointer since 2nd array is NULL" )
		? passcount++ : failcount++ ;
	// Test 9 -- expect NULL pointer
	p_products = create_array_of_products( first_array, second_array, 0 ) ;
	assert( p_products == NULL,
		"Expected a NULL pointer since number of elements is 0" )
		? passcount++ : failcount++ ;
	// Test 10 -- expect valid pointer
	p_products = create_array_of_products( first_array, second_array, sizeof( first_array ) / sizeof( int ) ) ;
	assert( p_products != NULL && p_products != first_array,
		"The returned p_products pointer is NULL or is still set to first_array!" )
		? passcount++ : failcount++ ;
	// Test 11 -- verify access to valid pointer's first entry
	assert( p_products && *p_products == 5,
		"The first entry in the sums array should be 5 but got %d (if a -1 that is because the pointer is NULL)", p_products ? *p_products : -1 )
		? passcount++ : failcount++ ;
	// Test 12 -- verify access to valid pointer's last entry
	assert( p_products && *( p_products + 5 ) == 60,
		"The last entry in the sums array should be 60 but got %d (if a -1 that is because the pointer is NULL)",  p_products ? *( p_products + 5 ) : -1 )
		? passcount++ : failcount++ ;
	// avoid memory leaks
	if ( p_products && p_products != first_array )
		free( p_products ) ;

	printf( "\nTests for get_total_and_free..........................\n" ) ;
	// Test 13 -- test for NULL pointer
	int total = -1 ;
	
	total = get_total_and_free( NULL, 5 ) ;
	assert( total == 0,
		"Expected 0 because the pointer is NULL but got %d", total )
		? passcount++ : failcount++ ;
	// Test 14 -- test for 0 entries
	total = get_total_and_free( p_products, 0 ) ;
	assert( total == 0,
		"Expected 0 because the number of entries is zero but got %d", total )
		? passcount++ : failcount++ ;
	// Test 15 -- test for correct sums
	// setup code for test 15
	#define SOURCE_SIZE (4)
	int source_array[SOURCE_SIZE] = { 10, 15, 20, 25 } ;
	int *p_temp = malloc( SOURCE_SIZE * sizeof(int) ) ;
	memcpy( p_temp, source_array, SOURCE_SIZE * sizeof(int) ) ;

	total = get_total_and_free( p_temp, SOURCE_SIZE ) ;
	assert( total == 70,
		"Expected 70 but got %d", total )
		? passcount++ : failcount++ ;
	
		
	printf( "\nTests for fix_bad_code..............................\n" ) ;
	// Test 16
	result = fix_bad_code( "test" ) ;
	assert( result == 0,
		"Expected 0 because there are no digits but got %d",  result )
		? passcount++ : failcount++ ;
	// Test 17
	result = fix_bad_code( "test1" ) ;
	assert( result == 1,
		"Expected 1 because there is one digit but got %d",  result )
		? passcount++ : failcount++ ;
	// Test 18
	result = fix_bad_code( "" ) ;
	assert( result == 0,
		"Expected 0 because it is an empty string but got %d",  result )
		? passcount++ : failcount++ ;
	// Test 19
	result = fix_bad_code( NULL ) ;
	assert( result == 0,
		"Expected 0 because the string pointer is a null pointer but got %d",  result )
		? passcount++ : failcount++ ;
		
	printf( "\nAdditional tests to confirm correct implementation" ) ;
	printf( "\nTests for is_pointer_in_array.......................\n" ) ;
	// Test 20
	result = is_pointer_in_array( first_array, first_array, sizeof(first_array) / sizeof(int) ) ;
	assert( result == 1,
		"Points to the first position in the array so expected a 1 but got a %d",  result )
		? passcount++ : failcount++ ;
	// Test 21
	result = is_pointer_in_array( first_array, &first_array[5], sizeof(first_array) / sizeof(int) ) ;
	assert( result == 1,
		"Points to the last position in the array so expected a 1 but got a %d",  result )
		? passcount++ : failcount++ ;
	// Test 22
	result = is_pointer_in_array( first_array, &first_array[6], sizeof(first_array) / sizeof(int) ) ;
	assert( result == 0,
		"Points to an invalid position just past the end of the array so expected a 0 but got a %d",  result )
		? passcount++ : failcount++ ;

	printf( "\nTests for my_random()...............................\n" ) ;
	// Test 23
	answer = my_random() ;
	assert( answer == 9,
		"Second random number must be 9 but got a %u",  answer )
		? passcount++ : failcount++ ;
	// Test 24
	my_random() ;	// skip next two numbers
	my_random() ;
	answer = my_random() ;
	assert( answer == 55,
		"Last random number must be 55 but got a %u",  answer )
		? passcount++ : failcount++ ;
	// Test 25
	my_random() ;
	my_random() ;
	my_random() ;
	answer = my_random() ;
	assert( answer == 207,
		"Must now be at the last random number of 207 but got a %u",  answer )
		? passcount++ : failcount++ ;

	// Test 26
	answer = my_random() ;
	assert( answer == 5,
		"Must now be at the first random number of 5 but got a %u",  answer )
		? passcount++ : failcount++ ;

	printf( "\nTests for fix_bad_code..............................\n" ) ;
	// Test 27
	result = fix_bad_code( "bigstringtosearch" ) ;
	assert( result == 0,
		"Expected 0 because there are no digits but got %d",  result )
		? passcount++ : failcount++ ;
	// Test 28
	result = fix_bad_code( "test1" ) ;
	assert( result == 1,
		"Expected 1 because there is one digit but got %d",  result )
		? passcount++ : failcount++ ;
	// Test 29
	result = fix_bad_code( "9firstsecondthird" ) ;
	assert( result == 1,
		"Expected 1 because it has a digit in the first position but got %d",  result )
		? passcount++ : failcount++ ;

	printf( "\nSummary of unit tests:\n%d tests passed\n%d tests failed\n\n", passcount, failcount ) ;
	
	return failcount ;
}
