/*
 * recipes.h
 *
 *      Author: Aiden Green
 */

#ifndef INC_RECIPES_H_
#define INC_RECIPES_H_

#include "servo.h"

typedef struct {
  uint8_t function;
  uint8_t args;
} command;

extern unsigned char recipe1[];
extern unsigned char recipe2[];
extern unsigned char recipe_required[];
extern unsigned char recipe_all_positions[];
extern unsigned char recipe_early_end[];
extern unsigned char recipe_mov_err[];
extern unsigned char recipe_nested_err[];
extern unsigned char recipe_rainbow[];
extern unsigned char *recipes[];

void Recipe_Next(servo *servo);

#endif /* INC_RECIPES_H_ */
