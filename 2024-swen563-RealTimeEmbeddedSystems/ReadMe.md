# Real Time & Embedded Systems Projects

*(CMPE663/EEEE663/SWEN563)*

### Description:
This repository contains the projects for the "Real Time & Embedded Systems" course at RIT. The projects are based on the STM32F4 Discovery board and are written in C.

### Commands:
1. Building: `compiledb make all -j 7`
2. Flashing: `STM32_Programmer_CLI -c port=SWD -w build/$(basename "$PWD").bin 0x08000000`
3. Opening UART: `screen /dev/tty.usbmodem2103 {baudrate}` (usually 115200)
4. Cleaning: `make clean`
6. Closing UART: `Ctrl + A + D`

### Notes:
- `compiledb` is a tool to generate `compile_commands.json` file for clang based tools.
- `STM32_Programmer_CLI` is a CLI by STMicroelectronics to do things like flashing, erasing, etc.
- `screen` is a terminal multiplexer that can be used to open a serial terminal.
- The gcc-arm-none-eabi toolchain is required to build the projects. The current one in the project is for macOS.