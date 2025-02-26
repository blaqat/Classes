// unit_tests.c
// Larry Kiser Feb. 13, 2018

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdarg.h>
#include <unistd.h>

#include "cpracticum.h"
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
	int result ;
	
	printf( "\n----------------- testing count_number_of_numeric_characters --------------------\n" ) ;
	// Test 1 -- 
	result = count_number_of_numeric_characters( "" ) ;
	assert( result == 0,
		"Empty string must report a count of 0. Your count is %d", result )
		? passcount++ : failcount++ ;
		
	// Test 2 -- 
	result = count_number_of_numeric_characters( "0" ) ;
	assert( result == 1,
		"Single numeric character string must report a count of 1. Your count is %d", result )
		? passcount++ : failcount++ ;
		
	// Test 3 -- 
	result = count_number_of_numeric_characters( ";abCA==" ) ;
	assert( result == 0,
		"No numeric characters in this string so count must be 0. Your count is %d", result )
		? passcount++ : failcount++ ;
		
	// Test 4 -- 
	result = count_number_of_numeric_characters( "abcdef0" ) ;
	assert( result == 1,
		"Single numeric character at the end of the string. Count must be 1. Your count is %d", result )
		? passcount++ : failcount++ ;
		
	// Test 5 -- 
	result = count_number_of_numeric_characters( "9Ac=3ff" ) ;
	assert( result == 2,
		"Two numeric character string. Count must be 2. Your count is %d", result )
		? passcount++ : failcount++ ;
		
	// Test 6 -- 
	result = count_number_of_numeric_characters( "kljhadkjhfkjhksk 445678889966554" ) ;
	assert( result == 15,
		"Fifteen numeric character string. Count must be 15. Your count is %d", result )
		? passcount++ : failcount++ ;
	
	printf( "\n----------------- testing convert_to_upper_case --------------------\n" ) ;
	// Test 7 --
	char test7[] = "" ;
	convert_to_upper_case( test7 ) ;
	assert( test7[0] == '\0',
		"An empty string must not be altered by convert_to_upper_case. The end of string character was changed." )
		? passcount++ : failcount++ ;
		
	// Test 8 --
	char test8[] = "z" ;
	convert_to_upper_case( test8 ) ;
	assert( strcmp( test8, "Z" ) == 0,
		"Expected a string with just an upper case Z. Your string is %s", test8 )
		? passcount++ : failcount++ ;
		
	// Test 9 --
	char test9[] = "F390 ==!c" ;
	convert_to_upper_case( test9 ) ;
	assert( strcmp( test9, "F390 ==!C" ) == 0,
		"Expected F390 ==!C but your string is %s", test9 )
		? passcount++ : failcount++ ;
		
	// Test 10 --
	char test10[] = "KKaazzBB" ;
	convert_to_upper_case( test10 ) ;
	assert( strcmp( test10, "KKAAZZBB" ) == 0,
		"Expected KKAAZZBB but your string is %s", test10 )
		? passcount++ : failcount++ ;
		
	// Test 11 --
	char test11[] = "@#%&^&$&*(" ;
	convert_to_upper_case( test11 ) ;
	assert( strcmp( test11, "@#%&^&$&*(" ) == 0,
		"Expected @#%&^&$&*( but your string is %s", test11 )
		? passcount++ : failcount++ ;
		
	printf( "\n----------------- testing range_test --------------------\n" ) ;
	// Test 12 --
	char letter ;
	letter = range_test( -10, -9, 4 ) ;
	assert( letter == 'L',
		"Expected L because -10 is less than -9. Your result is %c", letter )
		? passcount++ : failcount++ ;
		
	// Test 13 --
	letter = range_test( -10, -10, 4 ) ;
	assert( letter == 'M',
		"Expected M because -10 is the lower limit. Your result is %c", letter )
		? passcount++ : failcount++ ;
		
	// Test 14 --
	letter = range_test( 4, -10, 4 ) ;
	assert( letter == 'M',
		"Expected M because 4 is the upper limit. Your result is %c", letter )
		? passcount++ : failcount++ ;
		
	// Test 15 --
	letter = range_test( 5, -10, 4 ) ;
	assert( letter == 'H',
		"Expected H because 5 is above the upper limit. Your result is %c", letter )
		? passcount++ : failcount++ ;

	printf( "\n----------------- testing fix_bad_code --------------------\n" ) ;
	// Test 16 -- 
	int numbers1[] = { 5, 10, 15, 20, 25, 99 } ;
	result = fix_bad_code( numbers1, 2 ) ;
	assert( result == 15,
		"Expected 15 for sum of first two. Got %d", result )
		? passcount++ : failcount++ ;
		
	// Test 17 -- 
	result = fix_bad_code( numbers1, 5 ) ;
	assert( result == 75,
		"Expected 75 for sum of first five. Got %d", result )
		? passcount++ : failcount++ ;
		
	printf( "\nSummary of unit tests:\n%d tests passed\n%d tests failed\n\n", passcount, failcount ) ;
	
	return failcount ;
}
