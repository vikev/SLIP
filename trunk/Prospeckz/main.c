#include <stdint.h>
#include <stdlib.h>
#include <math.h>
#include "nrf.h"
/*#include "Include/s110/nrf_soc.h"*/
#include "nrf_gpio.h"
#include "Include/nrf_delay.h"
#include "Include/twi_master.h"
#include "Include/s110/nrf_error.h"
#include "app_timer.h"
#include "libjanek.h"
#include "../gcc-arm-none-eabi-4_8-2014q2/arm-none-eabi/include/math.h"
#include "nrf51-sdk/nrf51822/Include/simple_uart.h"
#include "Include/s110/ble.h"
#include "Include/ble//ble_services/ble_bas.h"
#include "Include/ble/ble_services/ble_hrs.h"
#include <Include/ble/ble_advdata.h>


#define APP_ADV_INTERVAL                     40                                         /**< The advertising interval (in units of 0.625 ms. This value corresponds to 25 ms). */
#define APP_ADV_TIMEOUT_IN_SECONDS           180                                        /**< The advertising timeout in units of seconds. */

static uint16_t                              m_conn_handle = BLE_CONN_HANDLE_INVALID;   /**< Handle of the current connection. */
static ble_bas_t                             m_bas;                                     /**< Structure used to identify the battery service. */
static ble_hrs_t                             m_hrs;
static ble_gap_adv_params_t                  m_adv_params;                              /**< Parameters to be passed to the stack when starting advertising. */
// Debug helper variables
static volatile bool init_ok, enable_ok, push_ok, pop_ok, tx_success;

/**@brief Error handler function, which is called when an error has occurred. 
 *
 * @warning This handler is an example only and does not fit a final product. You need to analyze 
 *          how your product is supposed to react in case of error.
 *
 * @param[in] error_code  Error code supplied to the handler.
 * @param[in] line_num    Line number where the handler is called.
 * @param[in] p_file_name Pointer to the file name. 
 */


static void ble_evt_dispatch(ble_evt_t * p_ble_evt);
/*static void sys_evt_dispatch(uint32_t sys_evt); */
void app_error_handler(uint32_t error_code, uint32_t line_num, const uint8_t * p_file_name);
static void ble_stack_init(void);
static void advertising_init(void);


void app_error_handler(uint32_t error_code, uint32_t line_num, const uint8_t * p_file_name)
{
 
    // This call can be used for debug purposes during development of an application.
    // @note CAUTION: Activating this code will write the stack to flash on an error.
    //                This function should NOT be used in a final product.
    //                It is intended STRICTLY for development/debugging purposes.
    //                The flash write will happen EVEN if the radio is active, thus interrupting
    //                any communication.
    //                Use with care. Un-comment the line below to use.
    // ble_debug_assert_handler(error_code, line_num, p_file_name);

    // On assert, the system can only recover with a reset.
    NVIC_SystemReset();
}

static void ble_stack_init(void)
{
    uint32_t err_code;

    // Initialize the SoftDevice handler module.

    SOFTDEVICE_HANDLER_INIT(NRF_CLOCK->LFCLKSRC, false);

#ifdef S110
    // Enable BLE stack
    ble_enable_params_t ble_enable_params;
    memset(&ble_enable_params, 0, sizeof(ble_enable_params));
    ble_enable_params.gatts_enable_params.service_changed = IS_SRVC_CHANGED_CHARACT_PRESENT;
    err_code = sd_ble_enable(&ble_enable_params);
    APP_ERROR_CHECK(err_code);
#endif

    // Register with the SoftDevice handler module for BLE events.
    err_code = softdevice_ble_evt_handler_set(ble_evt_dispatch);
    APP_ERROR_CHECK(err_code);

 /*   // Register with the SoftDevice handler module for BLE events.
    err_code = softdevice_sys_evt_handler_set(sys_evt_dispatch);
    APP_ERROR_CHECK(err_code); */
}

static void ble_evt_dispatch(ble_evt_t * p_ble_evt)
{
    dm_ble_evt_handler(p_ble_evt);
    ble_hrs_on_ble_evt(&m_hrs, p_ble_evt);
    ble_bas_on_ble_evt(&m_bas, p_ble_evt);
    ble_conn_params_on_ble_evt(p_ble_evt);
#ifdef BLE_DFU_APP_SUPPORT
    /** @snippet [Propagating BLE Stack events to DFU Service] */
    ble_dfu_on_ble_evt(&m_dfus, p_ble_evt);
    /** @snippet [Propagating BLE Stack events to DFU Service] */
#endif // BLE_DFU_APP_SUPPORT
    on_ble_evt(p_ble_evt);
}

static void on_ble_evt(ble_evt_t * p_ble_evt)
{
	uint32_t err_code = NRF_SUCCESS;

	switch (p_ble_evt->header.evt_id)
	{
		case BLE_GAP_EVT_CONNECTED:
			// nrf_gpio_pin_set(CONNECTED_LED_PIN_NO);
			// nrf_gpio_pin_clear(ADVERTISING_LED_PIN_NO);

			m_conn_handle = p_ble_evt->evt.gap_evt.conn_handle;
			break;


		default:
			break;
	}

	APP_ERROR_CHECK(err_code);
}

/*static void sys_evt_dispatch(uint32_t sys_evt)
{
    pstorage_sys_event_handler(sys_evt);
    on_sys_evt(sys_evt);
} */

/**@brief Function for initializing the Advertising functionality.
 *
 * @details Encodes the required advertising data and passes it to the stack.
 *          Also builds a structure to be passed to the stack when starting advertising.
 */
static void advertising_init(void)
{
    uint32_t      err_code;
    ble_advdata_t advdata;
    uint8_t       flags = BLE_GAP_ADV_FLAGS_LE_ONLY_GENERAL_DISC_MODE;

    ble_uuid_t adv_uuids[] =
    {
        {BLE_UUID_HEART_RATE_SERVICE,         BLE_UUID_TYPE_BLE},
        {BLE_UUID_BATTERY_SERVICE,            BLE_UUID_TYPE_BLE},
        {BLE_UUID_DEVICE_INFORMATION_SERVICE, BLE_UUID_TYPE_BLE}
    };

    // Build and set advertising data.
    memset(&advdata, 0, sizeof(advdata));

    advdata.name_type               = BLE_ADVDATA_FULL_NAME;
    advdata.include_appearance      = true;
    advdata.flags.size              = sizeof(flags);
    advdata.flags.p_data            = &flags;
    advdata.uuids_complete.uuid_cnt = sizeof(adv_uuids) / sizeof(adv_uuids[0]);
    advdata.uuids_complete.p_uuids  = adv_uuids;

    err_code = ble_advdata_set(&advdata, NULL);
    APP_ERROR_CHECK(err_code);

    // Initialize advertising parameters (used when starting advertising).
    memset(&m_adv_params, 0, sizeof(m_adv_params));

    m_adv_params.type        = BLE_GAP_ADV_TYPE_ADV_IND;
    m_adv_params.p_peer_addr = NULL;                           // Undirected advertisement.
    m_adv_params.fp          = BLE_GAP_ADV_FP_ANY;
    m_adv_params.interval    = APP_ADV_INTERVAL;
    m_adv_params.timeout     = APP_ADV_TIMEOUT_IN_SECONDS;
}

uint8_t acceldata[6];

int main()
{
  NRF_CLOCK->LFCLKSRC = 0; // RC Timer

  NRF_CLOCK->EVENTS_LFCLKSTARTED = 0;
  NRF_CLOCK->TASKS_LFCLKSTART = 1;

  ble_stack_init();
  /*advertising_init();*/
  /* Wait for the external oscillator to start up */
  while (NRF_CLOCK->EVENTS_LFCLKSTARTED == 0) 
  {
  }

  twi_master_init();

  NVIC_EnableIRQ(GPIOTE_IRQn);
  __enable_irq();

  MMA_init();

  simple_uart_config(0, 23, 0, 22, 0);
  nrf_gpio_cfg_output(0);
  uint16_t x, y, z;
  unsigned char buf[32];
  while (1) {
    nrf_gpio_pin_toggle(0);
    MMA_getdata(acceldata);
    x = acceldata[0] << 8 | acceldata[1];
    y = acceldata[2] << 8 | acceldata[3];
    z = acceldata[4] << 8 | acceldata[5];
    sprintf((char*)buf, "X: %u Y: %u Z: %u\n lol", x, y, z);
    simple_uart_putstring(buf);
    nrf_delay_ms(100);
  }
}
