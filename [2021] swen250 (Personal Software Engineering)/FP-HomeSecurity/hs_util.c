/*
 * Implementation of the utility module functions.
 *
 * kmr: 04/2020: 
 * Use standard strtok C library function to parse csv line.  Removed all regex
 */
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "hs_config.h"
#include "hs_util.h"

/*
 * Read the next line of text into 'buffer'. At most 'max' characters
 * will be read; characters after this limit are silently discarded.
 * A NUL ('\0') character is placed after the last character in buffer,
 * so its size must be at least (max + 1) characters.
 *
 * Returns the number of characters placed in the buffer (zero if EOF)
 */
int read_line(char* buffer, int maxSize)
{
	char c; int count = 0;
	c = getchar();
	while ((c != '\n') && (c != EOF) && (count < maxSize))
	{
		buffer[count] = c;
		count++;
		c = getchar();//Get the next character
	}
	buffer[count] = '\0';
	return c;
}

/*
 * Parse the string in 'line' using the CSV format from the problem spec.
 * Assumes 'line' is a proper C string ('\0' terminated).
 * Returns number of fields found
 */
int parse_string(char* str, struct reading_t* reading)
{
	//ROOM,SID,HH:MM:SS,TYPE[,VALUE]
	//room_id, sensor_id, time_stamp, event_id (type), event_info
	char *split;
	char temp[128];
	int fieldIndex = 0;

	strcpy(temp, str);
	split = strtok(temp, ",");
	while (split != NULL)
	{
		switch (fieldIndex)
		{
			case 0: //Room ID
				reading->room_id = atoi(split);
				break;
			case 1: //Sensor ID
				reading->event.sensor = atoi(split);
				break;
			case 2: //Time Stamp
				strcpy(reading->event.time_stamp, split);
				break;
			case 3: //Event Type aka event_id
				reading->event.event_id = atoi(split);
				break;
			case 4: //Event value (temp/ ppm etc); Optional data item
				reading->event.event_info = atoi(split);
				break;							
		}
		split = strtok(NULL, ",");
		fieldIndex++;
	}

	return fieldIndex;
}
