################################################################################
# Automatically-generated file. Do not edit!
# Toolchain: GNU Tools for STM32 (10.3-2021.10)
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../Core/Src/demos/adc_dac_demo.c \
../Core/Src/demos/demo.c 

OBJS += \
./Core/Src/demos/adc_dac_demo.o \
./Core/Src/demos/demo.o 

C_DEPS += \
./Core/Src/demos/adc_dac_demo.d \
./Core/Src/demos/demo.d 


# Each subdirectory must supply rules for building sources it contributes
Core/Src/demos/%.o Core/Src/demos/%.su: ../Core/Src/demos/%.c Core/Src/demos/subdir.mk
	arm-none-eabi-gcc "$<" -mcpu=cortex-m4 -std=gnu11 -g3 -DDEBUG -DUSE_HAL_DRIVER -DSTM32L476xx -c -I../Core/Inc -I../Core/Inc/demos -I../Core/Inc/devices -I../Core/Inc/midi -I../Core/Inc/system -I../Core/Src/system -I../Core/Src/midi -I../Core/Src/devices -I../Core/Src/demos -I../Drivers/STM32L4xx_HAL_Driver/Inc -I../Drivers/STM32L4xx_HAL_Driver/Inc/Legacy -I../Drivers/CMSIS/Device/ST/STM32L4xx/Include -I../Drivers/CMSIS/Include -O0 -ffunction-sections -fdata-sections -Wall -fstack-usage -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" --specs=nano.specs -mfpu=fpv4-sp-d16 -mfloat-abi=hard -mthumb -o "$@"

clean: clean-Core-2f-Src-2f-demos

clean-Core-2f-Src-2f-demos:
	-$(RM) ./Core/Src/demos/adc_dac_demo.d ./Core/Src/demos/adc_dac_demo.o ./Core/Src/demos/adc_dac_demo.su ./Core/Src/demos/demo.d ./Core/Src/demos/demo.o ./Core/Src/demos/demo.su

.PHONY: clean-Core-2f-Src-2f-demos

