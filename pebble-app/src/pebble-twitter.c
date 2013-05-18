#include "pebble_os.h"
#include "pebble_app.h"
#include "pebble_fonts.h"

#define MY_UUID { 0xa9, 0xe8, 0x9a, 0x76, 0x34, 0x27, 0x4a, 0xd0, 0x86, 0x1a, 0xa6, 0x29, 0xd2, 0x15, 0x29, 0x91 }
PBL_APP_INFO(MY_UUID,
             "Pebble-Twitter", "Defined Code Ltd",
             1, 0, /* App version */
             DEFAULT_MENU_ICON,
             APP_INFO_STANDARD_APP);

Window window;
ScrollLayer scroll;
TextLayer text;

void in_rcv_handler(DictionaryIterator *received, void *context) {
  //fuck yea
  Tuple *tuple = dict_read_first(received);
  text_layer_set_text(&text, tuple->value->cstring);
  layer_mark_dirty(&text.layer);
}
void in_drp_handler(void *context, AppMessageResult reason) {
  //oh fuck.
}

void handle_init(AppContextRef ctx) {
  (void)ctx;

  window_init(&window, "Window");
  window_stack_push(&window, true /* Animated */);
  scroll_layer_init(&scroll, layer_get_frame(&window.layer));
  text_layer_init(&text, GRect(0, 0, 144, 500));
  text_layer_set_font(&text, fonts_get_system_font(FONT_KEY_GOTHIC_24));
  text_layer_set_text(&text, "Loading tweets (make sure app has been opened on your phone/is active).");
  layer_add_child(&window.layer, &scroll.layer);
  scroll_layer_add_child(&scroll, &text.layer);
  scroll_layer_set_click_config_onto_window(&scroll, &window);
}


void pbl_main(void *params) {
  PebbleAppHandlers handlers = {
    .init_handler = &handle_init,
    .messaging_info = {
      .buffer_sizes = {
        .inbound = 512,
        .outbound = 64,
      },
      .default_callbacks.callbacks = {
        .in_received = in_rcv_handler,
        .in_dropped = in_drp_handler,
      },
    },
  };
  app_event_loop(params, &handlers);
}
