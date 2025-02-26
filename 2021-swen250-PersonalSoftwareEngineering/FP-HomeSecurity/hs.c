/*
 * Home Security System
 */
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>

#include "hs_config.h"
#include "hs_util.h"

/*
 * An event node on the linked list of events for
 * a room. Consists of an event as gathered from a
 * reading and a link to the next node on the list
 * (if any).
 */
struct evnode_t {
	struct event_t event ;
	struct evnode_t *next ;
} ;

/*
 * Room event information
 * 	The room number.
 * 	Total number of events that have been logged.
 * 	Pointer to the most recent reading's node (if any).
 * 	Pointer to the next room (if any).
 */
struct room_t {
	int room ;
	int ecount ;
	struct evnode_t *event_list_head ;
	struct room_t *next_room ;
} ;

/*
 * Head of the list of rooms being monitored.
 */
static struct room_t *room_list = NULL ;

/*
 * Local support functions (static).
 * You have to fill in the details.
 * Feel free to add more to make your work easier!
 */
static void print_common_alert_text(int room_id, struct event_t event);
static void print_room_info(struct room_t *room, int sensor, char time_stamp[]);
static void print_event(struct event_t event);
static void process_a_reading(struct reading_t reading) ;
static struct room_t *find_room(int room) ;
static struct room_t *add_new_room(int room) ;
static void trim_list(struct room_t *room, int keep, char timestamp[]) ;

static struct evnode_t *make_event_node();

/*
 * Main driver function.
 *
 * First, initialize global data structures (rooms array).
 *
 * Next, read lines of text representing readings, parse these, and if there
 * are no syntax errors, record the associated events. Syntactically incorrect
 * input reading lines are silently discarded.
 * 
 */
int main() {
	char next_line[MAX_LINELENGTH+1] ;
	struct reading_t reading ;

	/*
	 * Read a line of text into next_line, then attempt to parse
	 * it as a <reading> line. If the line is parsable, get the
	 * last reading structure and process it according to the
	 * specification for the appropriate level.
	 * See hs_util.h for useful utility functions.
	 * 
	 * At the end of the program, clean up any dynamically allocated memory
	 */
	
	while( read_line(next_line, MAX_LINELENGTH) != EOF ) {
		if( parse_string(next_line, &reading) ) {
			process_a_reading(reading) ;
			
		// NOTE - echo_reading() not required in Part 2
		}
	}


	return 0 ;
}

// **** These three print functions are copied and modified from the Part 1 Solutions

/*
 * Print common event information for an alert. Any prefix (and possible
 * suffix) is dependent on the specific alert type.
 */
static void print_common_alert_text(int room_id, struct event_t event) {
	printf(" alert @ %s:", event.time_stamp) ;
	printf(" room %d / sensor %d", room_id, event.sensor) ;
	return ;
}

/*
 * Print information about a sensor event.
 */
static void print_event(struct event_t event) {
	printf("Sensor %d @ %s ", event.sensor, event.time_stamp) ;

	switch( event.event_id ) {
		case T_TEMPERATURE:
			printf("temperature reading %d degrees\n",
				event.event_info) ;
			break ;
		case T_CO:
			printf("carbon monoxide reading %d PPB\n",
				event.event_info) ;
			break ;
		case T_INTRUDER:
			printf("intruder alert\n") ;
			break ;
	}
}

/*
 * Print out the information about a room.
 *    The room id.
 *    The sensor that triggered the printout.
 *    The total number of events recorded for the room.
 *    The saved events, if any, from newest to oldest.
 */
static void print_room_info(struct room_t *room, int sensor, char time_stamp[]) {
	int room_id = room->room;
	struct evnode_t *ev_index;

	printf("*****\n") ;
	printf("Home Security System: Room %d @ %s\n",
			room_id, time_stamp) ;
	printf("Triggered by sensor %d\n",
			sensor) ;
	printf("%d total room events\n",
			room->ecount) ;

	if( room->ecount == 0 ) {
		return ;
	}

	ev_index = room->event_list_head;

	while(ev_index) {
		print_event(ev_index->event);
		ev_index = ev_index->next;
	}

	return ;
}

/*
 * Create a new evnode_t node, initialize it using the provided
 * event and a NULL link, and return the pointer to the node.
 */
static struct evnode_t *make_event_node(struct event_t event) {
	struct evnode_t *new_evnode = (struct evnode_t*)malloc(sizeof(struct evnode_t));;

	// FILL IN THE BODY
	new_evnode->event = event;
	new_evnode->next = NULL;

	return new_evnode ;
}

/*
 * Create a new room_t node for <room>, initialize its fields, and append
 * the node to the end of the <room_list>.
 * Returns a pointer to the new room's structure.
 */
static struct room_t *add_new_room(int room) 
{
	struct room_t *new_room = (struct room_t*)malloc(sizeof(struct room_t));; // ptr. to new room structure
	// FILL IN THE BODY
	new_room->ecount = 0;
	new_room->next_room = NULL;
	new_room->room = room;

	if(!room_list)
		room_list = new_room;
	else{
		struct room_t *t_room = room_list;
		while(t_room->next_room)
			t_room = t_room->next_room;
		t_room->next_room = new_room;
	}
	return new_room ;
}

/*
 * Find the specified <room> in the <room_list>, returning a pointer to the
 * found room_t structure or NULL if there is no such <room> in the list.
 */
static struct room_t *find_room(int room)
{
	struct room_t *the_room = room_list;

	if(!the_room)
		return NULL;

	while(the_room->room != room && the_room->next_room){
		the_room = the_room->next_room;
	}

	return the_room->room==room?the_room:NULL;
}

/*
 * Given a reading, process the included event for the room in the reading.
 * T_PRINT and T_TRIM readings are really commands; once executed they are
 * discarded.
 * For all other readings check to see whether an alert should be printed,
 * then add the event to as the newest event in the room's event list.
 */
static void process_a_reading(struct reading_t reading) {

	// FILL IN THE BODY
	int room_id = reading.room_id;
	struct room_t *room = find_room(room_id);
	struct event_t event = reading.event;

	//Makes sure there is a room.
	if(!room){
		room = add_new_room(room_id);
	}

	//System for Managing the different types of events
    //Also will do alerts when needed
	switch(event.event_id){

		case T_PRINT:
			print_room_info(room, event.sensor, event.time_stamp);
			return;

		case T_TRIM:
			trim_list(room, event.event_info, event.time_stamp);
			return;

		case T_INTRUDER:
			printf("Intruder");
			print_common_alert_text(room_id, event);
			printf(".\n");
			break;

		case T_CO:
            //If the Carbon monoxide level is exceeding the limit the alert is done
			if(event.event_info > CO_LIMIT){
				printf("Carbon monoxide");
				print_common_alert_text(room_id, event);
				printf(" /%d PPB.\n", event.event_info);
			}
			break;

		case T_TEMPERATURE:
            //If the Temperature level is above or below the temperature bounds the alert is done
			if(event.event_info < MIN_TEMP || event.event_info > MAX_TEMP ){
				printf("Temperature") ;
				print_common_alert_text(room_id, event) ;
				printf(" / %d degrees.\n", event.event_info) ;
			}
			break;
	}

    //Creates the new event and adds it to the head of the room events list
	struct evnode_t *new_event = make_event_node(event);

	new_event->next = room->event_list_head;
	room->event_list_head = new_event;
	room->ecount++;

	return ;
}


/*
 * If the <room> is in the <room_list>, trim the room's event node list
 * to at most <keep> entries. As the list is ordered from newest to
 * oldest, the oldest entries are discarded.
 *
 * Whether the room exists or not, and whether or not the list holds
 * anything, the specified "Trim log" message must be printed. Obviously,
 * for a non-existent room nothing is actually trimmed (removed).
 *
 * Note - dynamically allocated space for event nodes removed from
 *        the list must be freed.
 */
static void trim_list(struct room_t *the_room, int keep, char timestamp[]) {
	int room = the_room->room;
	struct evnode_t *ev_index;

	if(!the_room)
		return ;
	
	ev_index = the_room->event_list_head;
	int trimmed = 0;

    //This trims the event logs from the back of the list while the trimmed count
    //is not equal to the needed trimmed amount
	for(int i = 0; trimmed != the_room->ecount-keep && ev_index; i++){
		struct evnode_t *temp_evnode = ev_index;
		ev_index = ev_index->next;

		if(i >= keep){
            //If the first event in the list is being removed the event list head is set to NULL
			if(temp_evnode == the_room->event_list_head)
				the_room->event_list_head = NULL;
			
			free(temp_evnode);
			trimmed++;
		}
        //If the next event in the list is going to be trimmed
        //The event.next will be set to NULL
		else if(i + 1 >= keep){
			temp_evnode->next = NULL;
		}
	}

	printf("Trim log @ %s: room %d log trimmed to length %d (%d entries removed)\n", timestamp, room, keep, trimmed);

	return ;
}