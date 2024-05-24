################################################################################
# Automatically-generated file. Do not edit!
# Toolchain: GNU Tools for STM32 (10.3-2021.10)
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../Core/Src/system/commands.c \
../Core/Src/system/little_endian.c \
../Core/Src/system/printf.c \
../Core/Src/system/stm32l4xx_hal_msp.c \
../Core/Src/system/stm32l4xx_it.c \
../Core/Src/system/syscalls.c \
../Core/Src/system/sysmem.c \
../Core/Src/system/system_stm32l4xx.c \
../Core/Src/system/systick.c 

OBJS += \
./Core/Src/system/commands.o \
./Core/Src/system/little_endian.o \
./Core/Src/system/printf.o \
./Core/Src/system/stm32l4xx_hal_msp.o \
./Core/Src/system/stm32l4xx_it.o \
./Core/Src/system/syscalls.o \
./Core/Src/system/sysmem.o \
./Core/Src/system/system_stm32l4xx.o \
./Core/Src/system/systick.o 

C_DEPS += \
./Core/Src/system/commands.d \
./Core/Src/system/little_endian.d \
./Core/Src/system/printf.d \
./Core/Src/system/stm32l4xx_hal_msp.d \
./Core/Src/system/stm32l4xx_it.d \
./Core/Src/system/syscalls.d \
./Core/Src/system/sysmem.d \
./Core/Src/system/system_stm32l4xx.d \
./Core/Src/system/systick.d 


# Each subdirectory must supply rules for building sources it contributes
Core/Src/system/%.o Core/Src/system/%.su: ../Core/Src/system/%.c Core/Src/system/subdir.mk
	arm-none-eabi-gcc "$<" -mcpu=cortex-m4 -std=gnu11 -g3 -DDEBUG -DUSE_HAL_DRIVER -DSTM32L476xx -c -I../Core/Inc -I../Core/Inc/demos -I../Core/Inc/devices -I../Core/Inc/midi -I../Core/Inc/system -I../Core/Src/system -I../Core/Src/midi -I../Core/Src/devices -I../Core/Src/demos -I../Drivers/STM32L4xx_HAL_Driver/Inc -I../Drivers/STM32L4xx_HAL_Driver/Inc/Legacy -I../Drivers/CMSIS/Device/ST/STM32L4xx/Include -I../Drivers/CMSIS/Include -O0 -ffunction-sections -fdata-sections -Wall -fstack-usage -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" --specs=nano.specs -mfpu=fpv4-sp-d16 -mfloat-abi=hard -mthumb -o "$@"

clean: clean-Core-2f-Src-2f-system

clean-Core-2f-Src-2f-system:
	-$(RM) ./Core/Src/system/commands.d ./Core/Src/system/commands.o ./Core/Src/system/commands.su ./Core/Src/system/little_endian.d ./Core/Src/system/little_endian.o ./Core/Src/system/little_endian.su ./Core/Src/system/printf.d ./Core/Src/system/printf.o ./Core/Src/system/printf.su ./Core/Src/system/stm32l4xx_hal_msp.d ./Core/Src/system/stm32l4xx_hal_msp.o ./Core/Src/system/stm32l4xx_hal_msp.su ./Core/Src/system/stm32l4xx_it.d ./Core/Src/system/stm32l4xx_it.o ./Core/Src/system/stm32l4xx_it.su ./Core/Src/system/syscalls.d ./Core/Src/system/syscalls.o ./Core/Src/system/syscalls.su ./Core/Src/system/sysmem.d ./Core/Src/system/sysmem.o ./Core/Src/system/sysmem.su ./Core/Src/system/system_stm32l4xx.d ./Core/Src/system/system_stm32l4xx.o ./Core/Src/system/system_stm32l4xx.su ./Core/Src/system/systick.d ./Core/Src/system/systick.o ./Core/Src/system/systick.su

.PHONY: clean-Core-2f-Src-2f-system

