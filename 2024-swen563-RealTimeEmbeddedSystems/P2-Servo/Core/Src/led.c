/*
 * led.c
 *
 *  Created on: Feb 26, 2024
 *      Author: Aiden Green
 */
#include "led.h"

/**
 * @brief Initialize the LED on GPIOA port
 *
 * @param pin
 */
void LED_Init(uint32_t pin) {
  // Enable the peripheral clock of GPIO Port
  RCC->AHB2ENR |= RCC_AHB2ENR_GPIOBEN | RCC_AHB2ENR_GPIOEEN;

  GPIOA->MODER &=
      ~(0x3 << (pin * 2)); // first, clear the two MODE bits for this pin
  GPIOA->MODER |= 0x1 << (pin * 2); // 0x1 means output
  //
  // ...with medium speed
  GPIOA->OSPEEDR &=
      ~(0x3 << (pin * 2)); // first, clear the two OSPEED bits for this pin
  GPIOA->OSPEEDR |= 0x1 << (pin * 2); // 0x1 means medium speed

  // ...and as push-pull drive
  GPIOA->OTYPER &= ~(0x1 << pin);

  // ...with no pull-up/pull-down
  GPIOA->PUPDR &= ~(0x3 << (pin * 2));
}

/**
 * @brief Turn on the LED
 *
 * @param pin
 */
void LED_On(uint16_t pin) { HAL_GPIO_WritePin(GPIOA, pin, GPIO_PIN_RESET); }

/**
 * @brief Turn off the LED
 *
 * @param pin
 */
void LED_Off(uint16_t pin) { HAL_GPIO_WritePin(GPIOA, pin, GPIO_PIN_SET); }

/**
 * @brief Set the LED state
 *
 * @param pin
 * @param state
 */
void LED_Set(uint16_t pin, uint32_t state) {
  if (state)
    LED_On(pin);
  else
    LED_Off(pin);
}

/**
 * @brief Toggle the LED
 *
 * @param pin
 */
void LED_Toggle(uint32_t pin) { GPIOA->ODR ^= (1 << pin); }

/**
 * @brief Handle the LED state
 *
 * @param servo
 */
void LED_handler(servo *servo) {
  uint8_t change_led = (servo->led_state & 0b100) >> 2;
  uint8_t led_2 = (servo->led_state & 0b010) >> 1;
  uint8_t led_1 = servo->led_state & 0b001;

  if (!change_led) {
    return;
  }

  LED_Set(LED2, led_2);
  LED_Set(LED1, led_1);

  servo->led_state &= 0b011;
}
