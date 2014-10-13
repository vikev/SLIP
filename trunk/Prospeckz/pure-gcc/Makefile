# List all source files the application uses.
APPLICATION_SRCS = $(notdir $(wildcard ../*.c))
APPLICATION_SRCS += app_gpiote.c
APPLICATION_SRCS += app_button.c
APPLICATION_SRCS += app_timer.c
APPLICATION_SRCS += app_trace.c
APPLICATION_SRCS += ble_advdata.c
APPLICATION_SRCS += ble_conn_params.c
APPLICATION_SRCS += ble_sensorsim.c
APPLICATION_SRCS += pstorage.c
APPLICATION_SRCS += device_manager_peripheral.c
APPLICATION_SRCS += ble_bas.c
APPLICATION_SRCS += ble_dis.c
APPLICATION_SRCS += ble_hrs.c
APPLICATION_SRCS += ble_srv_common.c
APPLICATION_SRCS += crc16.c
APPLICATION_SRCS += softdevice_handler.c
APPLICATION_SRCS += simple_uart.c

PROJECT_NAME = $(shell basename "$(realpath ../)")

DEVICE = NRF51
BOARD = BOARD_NRF6310
#SEGGER_SERIAL =

USE_SOFTDEVICE = s110

SOFTDEVICE ?= ../nrf51-sdk/s110_nrf51822_7.0.0_softdevice.hex
SDK_PATH = ../nrf51-sdk/nrf51822/
TEMPLATE_PATH = ../nrf51-pure-gcc-setup/template/

LIBRARY_PATHS += $(SDK_INCLUDE_PATH)bootloader_dfu/

CFLAGS = -O0 -g -DS110

GDB_PORT_NUMBER = 2331

include $(TEMPLATE_PATH)Makefile
