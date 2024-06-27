/***
 * Functions for the DMV customer scheduling and service application.
 * Implementation file.
 ***/

#include <stdlib.h>
#include "dmv_schedule.h"

/*
 * Actual definition of the line array of service lines.
 */
struct service_line line[NLINES] ;

/*
 * Initialize the service lines so that (a) line[0] has priority 'A'
 * through line[2] having priority 'C' and (b) there are no customers
 * in any line.
 * NOTES: As usual, an empty list is signified by a NULL pointer.
 */
void init_service_lines() {
	// Placeholder for your code
	for(int i = 0; i < NLINES; i++){
		struct service_line new_line;
		
		new_line.priority = (char)((int)('A') + i);
		new_line.head_of_line = NULL;

		line[i] = new_line;
	}
}

/*
 * Return the next ticket number.
 * 	The first customer gets ticket number 1.
 *	The number increases by 1 on each call.
 * Local (static) int ticket holds the current ticket value.
 */
static int ticket = 1 ;
int next_ticket() {	
	return ticket++;	// Placeholder for your code.
}

/*
 * A new customer arrives with the given <priority> and
 * <ticket_number>. The customer is placed at the end of
 * the appropriate service line.
 */
void new_customer(char priority, int ticket_number) {
	// Placeholder for your code.
	struct customer *customers = line[(int)(priority - 'A')].head_of_line;

	struct customer *customer = (struct customer*)malloc(sizeof(struct customer));
	customer->next_customer = NULL;
	customer->ticket_number = ticket_number;

	if(customers == NULL){
		line[(int)(priority - 'A')].head_of_line = customer;
	}
	else{
		
		while(customers->next_customer){
			customers = customers->next_customer;
		}
		
		customers->next_customer = customer;
	}
}

/*
 * Return the ticket number of the first customer in the
 * line for <priority> after removing the customer from the
 * associated service_line.
 *
 * Return NO_CUSTOMER if there are no customers in the indicated line.
 */
int serve_customer(char priority) {
	struct customer *head = line[(int)(priority - 'A')].head_of_line;

	if(head != NULL){
		int ticket = head->ticket_number;
		line[(int)(priority - 'A')].head_of_line = head->next_customer;

		free(head);

		return ticket;
	}

	return NO_CUSTOMER;
}

/*
 * Return the ticket number of the first customer in the highest
 * priority line that has customers after removing the customer
 * from the line. 'A' is highest priority and 'C' is lowest.
 *
 * Return NO_CUSTOMER if there are no customers in any of the lines.
 *
 * Example: if there are 0 customers in the 'A' line, 3 customers in the 'B'
 *          line and 2 customers in the 'C' line, then the first customer in
 *          the 'B' line would be removed and his/her ticket number returned.
 */
int serve_highest_priority_customer() {
	int served = NO_CUSTOMER;

	for(int i = 0; i < NLINES && served == NO_CUSTOMER; i++){
		served = serve_customer((char)((int)('A') + i));
	}

	return served; // Placeholder for your code
}

/*
 * Return the number of customers in the service line for <priority>
 */
int customer_count(char priority) {
	struct customer *customers = line[(int)(priority - 'A')].head_of_line;
	int count = 0;

	while(customers != NULL){
		count++;
		customers = customers->next_customer;
	}
	
	return count; // Placeholder for your code.
}

/*
 * Return the number of customers in all service lines.
 */
int customer_count_all() {
	int count = 0;

	for(int i = 0; i < NLINES; i++){
		count += customer_count((char)((int)('A') + i));
	}

	return count; // Placeholder for your code.
}
