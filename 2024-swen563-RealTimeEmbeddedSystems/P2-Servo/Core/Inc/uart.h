#ifndef __STM32L476G_DISCOVERY_UART_H
#define __STM32L476G_DISCOVERY_UART_H

#include "stm32l476xx.h"

#define BufferSize 50

// HAL definitions
#define UART_WORDLENGTH_8B 0x00000000U   /*!< 8-bit long UART frame */
#define UART_PARITY_NONE 0x00000000U     /*!< No parity   */
#define UART_OVERSAMPLING_16 0x00000000U /*!< Oversampling by 16 */
#define UART_STOPBITS_1 0x00000000U      /*!< UART frame with 1 stop bit    */
#define UART_HWCONTROL_NONE 0x00000000U  /*!< No hardware control       */

void USART_Write(USART_TypeDef *USARTx, uint8_t *buffer, uint32_t nBytes);
uint8_t USART_Read(USART_TypeDef *USARTx);
uint8_t USART_Read_Async(USART_TypeDef *USARTx);
void USART_Delay(uint32_t us);
void USART_IRQHandler(USART_TypeDef *USARTx, uint8_t *buffer,
                      uint32_t *pRx_counter);
void USART2_Init(int baudrate);

#endif /* __STM32L476G_DISCOVERY_UART_H */
