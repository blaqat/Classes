################################################################################
# Automatically-generated file. Do not edit!
# Toolchain: GNU Tools for STM32 (10.3-2021.10)
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../Core/Src/devices/GPIO.c \
../Core/Src/devices/LED.c \
../Core/Src/devices/SysClock.c \
../Core/Src/devices/UART.c \
../Core/Src/devices/adc.c \
../Core/Src/devices/dac.c 

OBJS += \
./Core/Src/devices/GPIO.o \
./Core/Src/devices/LED.o \
./Core/Src/devices/SysClock.o \
./Core/Src/devices/UART.o \
./Core/Src/devices/adc.o \
./Core/Src/devices/dac.o 

C_DEPS += \
./Core/Src/devices/GPIO.d \
./Core/Src/devices/LED.d \
./Core/Src/devices/SysClock.d \
./Core/Src/devices/UART.d \
./Core/Src/devices/adc.d \
./Core/Src/devices/dac.d 


# Each subdirectory must supply rules for building sources it contributes
Core/Src/devices/%.o Core/Src/devices/%.su: ../Core/Src/devices/%.c Core/Src/devices/subdir.mk
	arm-none-eabi-gcc "$<" -mcpu=cortex-m4 -std=gnu11 -g3 -DDEBUG -DUSE_HAL_DRIVER -DSTM32L476xx -c -I../Core/Inc -I../Core/Inc/demos -I../Core/Inc/devices -I../Core/Inc/midi -I../Core/Inc/system -I../Core/Src/system -I../Core/Src/midi -I../Core/Src/devices -I../Core/Src/demos -I../Drivers/STM32L4xx_HAL_Driver/Inc -I../Drivers/STM32L4xx_HAL_Driver/Inc/Legacy -I../Drivers/CMSIS/Device/ST/STM32L4xx/Include -I../Drivers/CMSIS/Include -O0 -ffunction-sections -fdata-sections -Wall -fstack-usage -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" --specs=nano.specs -mfpu=fpv4-sp-d16 -mfloat-abi=hard -mthumb -o "$@"

clean: clean-Core-2f-Src-2f-devices

clean-Core-2f-Src-2f-devices:
	-$(RM) ./Core/Src/devices/GPIO.d ./Core/Src/devices/GPIO.o ./Core/Src/devices/GPIO.su ./Core/Src/devices/LED.d ./Core/Src/devices/LED.o ./Core/Src/devices/LED.su ./Core/Src/devices/SysClock.d ./Core/Src/devices/SysClock.o ./Core/Src/devices/SysClock.su ./Core/Src/devices/UART.d ./Core/Src/devices/UART.o ./Core/Src/devices/UART.su ./Core/Src/devices/adc.d ./Core/Src/devices/adc.o ./Core/Src/devices/adc.su ./Core/Src/devices/dac.d ./Core/Src/devices/dac.o ./Core/Src/devices/dac.su

.PHONY: clean-Core-2f-Src-2f-devices

