# List all source files the application uses.
APPLICATION_SRCS = $(notdir $(wildcard ../*.c)) nrf_delay.c simple_uart.c twi_sw_master.c
APPLICATION_SRCS += device_manager_peripheral.c
APPLICATION_SRCS += ble_dis.c
APPLICATION_SRCS += ble_bas.c
APPLICATION_SRCS += ble_hrs.c

APPLICATION_SRCS += ble_srv_common.c
APPLICATION_SRCS += ble_sensorsim.c
APPLICATION_SRCS += softdevice_handler.c
APPLICATION_SRCS += ble_advdata.c
APPLICATION_SRCS += ble_debug_assert_handler.c
APPLICATION_SRCS += ble_error_log.c
APPLICATION_SRCS += ble_conn_params.c
APPLICATION_SRCS += app_timer.c
APPLICATION_SRCS += pstorage.c
APPLICATION_SRCS += crc16.c
APPLICATION_SRCS += app_trace.c
APPLICATION_SRCS += simple_uart.c
APPLICATION_SRCS += app_gpiote.c
APPLICATION_SRCS += app_button.c
# Use shell to find name of root folder. Possible but horrible to do in make.
PROJECT_NAME = $(shell basename "$(realpath ../)")

DEVICE = NRF51
BOARD = BOARD_PCA10001
#SEGGER_SERIAL =


OUTPUT_FILENAME := hahaho
#USE_SOFTDEVICE = s110

SOFTDEVICE = ../nrf51-sdk/s110_nrf51822_6.0.0_softdevice.hex
SDK_PATH = ../nrf51-sdk/nrf51822/
TEMPLATE_PATH = ../nrf51-pure-gcc-setup/template/

CFLAGS = -Os

GDB_PORT_NUMBER = 2331

LIBRARY_PATHS += -I "$(SDK_PATH)/Include"
LIBRARY_PATHS += -I "$(SDK_PATH)/Board/nrf6310/s110/ble_app_alert_notification"
LIBRARY_PATHS += -I "$(SDK_PATH)/Include/s110"
LIBRARY_PATHS += -I "$(SDK_PATH)/Include/ble"
LIBRARY_PATHS += -I "$(SDK_PATH)/Include/ble/device_manager"
LIBRARY_PATHS += -I "$(SDK_PATH)/Include/ble/ble_services"
LIBRARY_PATHS += -I "$(SDK_PATH)/Include/app_common"
LIBRARY_PATHS += -I "$(SDK_PATH)/Include/sd_common"
LIBRARY_PATHS += -I "$(SDK_PATH)/Include/sdk"
LIBRARY_PATHS += -I "$(SDK_PATH)/Include/bootloader_dfu"

SOURCE_PATHS += $(SDK_PATH)Source/ble
SOURCE_PATHS += $(SDK_PATH)Source/ble/device_manager
SOURCE_PATHS += $(SDK_PATH)Source/ble/ble_services
SOURCE_PATHS += $(SDK_PATH)Source/app_common
SOURCE_PATHS += $(SDK_PATH)Source/sd_common

include $(TEMPLATE_PATH)Makefile

