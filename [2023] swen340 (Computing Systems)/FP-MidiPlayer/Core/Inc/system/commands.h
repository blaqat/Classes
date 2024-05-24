/*
 * commands.h
 *
 *  Created on: Marsh 23, 2023
 *      Author: Aiden Green
 *
 */

#ifndef INC_COMMANDS_H_
#define INC_COMMANDS_H_

#include <stdint.h>
#include "commands.h"
#include "song.h"

void run_command(char command[], uint8_t* flash);
void run_song();

#endif /* INC_COMMANDS_H_ */
