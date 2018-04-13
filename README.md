#Whatablet

A small wrapper around the whatsapp web client.

The interface is optimized for android tablet. Currently the design is not modified.

### Stuff to fix:

* Image Upload
* Replace stolen icon from whatsapp


### TODO
* Write short article for website
* Handle:
    > E/web js: WebSocket connection to 'wss://w4.web.whatsapp.com/ws' failed: WebSocket opening handshake timed out
* Test javascript hooks into the page for notifications
* Features
    * Support notifications
    * File upload
    * Let run the app in the background ==> Better notifications
   
### Nice to have
* Layout fixes
    * Fix layout of client on smaller displays
    * Remove background for performance reasons (in chat windows)

        Reverse engineering
            div#main
                div.YUoyu tag: data-asset-chat-background:true 
                
                [data-asset-chat-background] {
                    background-image: url(/img/8a05552….png);
                }
            
            Full link https://web.whatsapp.com/img/8a055527b27b887521a9f084497d8879.png
