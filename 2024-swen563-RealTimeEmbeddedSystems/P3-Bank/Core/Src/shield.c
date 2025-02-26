/**
 * @file    shield.c
 * @brief   This file provides code for the configuration
 *          of the shield
 */

#include "shield.h"

const char SEGMENT_MAP[] = {0xC0, 0xF9, 0xA4, 0xB0, 0x99,
                            0x92, 0x82, 0xF8, 0X80, 0X90};

const char SEGMENT_SELECT[] = {0xF1, 0xF2, 0xF4, 0xF8};

/**
 * @brief  Shift out a byte of data
 * @param  val: the byte to shift out
 */
void shift_out(uint8_t val) {
  for (int ii = 0x80; ii; ii >>= 1) {

    HAL_GPIO_WritePin(SHLD_D7_SEG7_Clock_GPIO_Port, SHLD_D7_SEG7_Clock_Pin,
                      GPIO_PIN_RESET); // clear clock pin

    if (ii & val) // if this bit in `value` is set
      HAL_GPIO_WritePin(SHLD_D8_SEG7_Data_GPIO_Port, SHLD_D8_SEG7_Data_Pin,
                        GPIO_PIN_SET); //   set it in shift register
    else
      HAL_GPIO_WritePin(SHLD_D8_SEG7_Data_GPIO_Port, SHLD_D8_SEG7_Data_Pin,
                        GPIO_PIN_RESET); //   else clear it

    HAL_GPIO_WritePin(SHLD_D7_SEG7_Clock_GPIO_Port, SHLD_D7_SEG7_Clock_Pin,
                      GPIO_PIN_SET); // set clock pin
  }
}

/**
 * @brief  Write an integer to a segment
 * @param  Segment: the segment to write to
 * @param  Value: the value to write
 */
void write_int_to_segment(int Segment, int Value) {

  HAL_GPIO_WritePin(SHLD_D4_SEG7_Latch_GPIO_Port, SHLD_D4_SEG7_Latch_Pin,
                    GPIO_PIN_RESET);

  shift_out(SEGMENT_MAP[Value]);

  shift_out(SEGMENT_SELECT[Segment]);

  HAL_GPIO_WritePin(SHLD_D4_SEG7_Latch_GPIO_Port, SHLD_D4_SEG7_Latch_Pin,
                    GPIO_PIN_SET);
}

/**
 * @brief Display an integer on the 7-segment display
 */
int last_segment = 0;
void SHLD_Display_Int(uint16_t val) {

  int digit[4];

  digit[0] = val / 1000;
  digit[1] = (val / 100) % 10;
  digit[2] = (val / 10) % 10;
  digit[3] = val % 10;

  write_int_to_segment(last_segment, digit[last_segment]);
  last_segment = (last_segment + 1) % 4;

  // for (int i = 0; i < 4; i++) {
  //   if (digit[i] == 0) {
  //     write_int_to_segment(i, 0);
  //   } else {
  //     write_int_to_segment(i, digit[i]);
  //   }
  // }
}

/**
 * @brief Initialize the buttons on the shield
 */
void SHLD_Button_Init(GPIO_TypeDef *GPIO_port, uint32_t GPIO_pins) {
  // Enable the GPIO clock for the button
  __HAL_RCC_GPIOA_CLK_ENABLE();
  __HAL_RCC_GPIOB_CLK_ENABLE();

  // Configure the button pin as an input
  GPIO_InitTypeDef GPIO_InitStruct = {0};
  GPIO_InitStruct.Pin = GPIO_pins;
  GPIO_InitStruct.Mode = GPIO_MODE_INPUT;
  GPIO_InitStruct.Pull = GPIO_PULLUP;
  GPIO_InitStruct.Speed = GPIO_SPEED_FREQ_LOW;

  HAL_GPIO_Init(GPIO_port, &GPIO_InitStruct);
}

/**
 * @brief Check if a button is pressed
 */
_Bool SHLD_Button_IsPressed(GPIO_TypeDef *GPIO_port, uint16_t GPIO_pin) {
  return HAL_GPIO_ReadPin(GPIO_port, GPIO_pin) == GPIO_PIN_RESET;
}

/**
 * @brief Get the state of a button
 */
GPIO_PinState SHLD_Button_GetState(GPIO_TypeDef *GPIO_port, uint16_t GPIO_pin) {
  return HAL_GPIO_ReadPin(GPIO_port, GPIO_pin);
}
