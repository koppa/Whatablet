package whatsapp

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI
import java.security.MessageDigest

val uri = URI("wss://w3.web.whatsapp.com/ws")
val headers = mapOf(Pair("Origin", "https://web.whatsapp.com"))

// TODO
val macKey ="abc"

class WhatsappConn(msgTimeout: Int) {
    val wsConn = object : WebSocketClient(uri, headers) {
        override fun onOpen(handshakedata: ServerHandshake?) {
            TODO("onOpen(   ) not implemented")
        }

        override fun onClose(code: Int, reason: String?, remote: Boolean) {
            println("websocket connection closed(%d, %s)\n".format(code, reason))

            throw NotImplementedError()
            // // from default CloseHandler
            // message := websocket.FormatCloseMessage(code, "")
            // wsConn.WriteControl(websocket.CloseMessage, message, time.Now().Add(time.Second))

            // // our close handling
            // if websocket.IsUnexpectedCloseError(err, websocket.CloseNormalClosure, websocket.CloseGoingAway) {
            //     fmt.Println("Trigger reconnect")
            //     go wac.reconnect()
            // }
            // return nil
        }

        override fun onMessage(message: String?) {
            TODO("onMessage(   ) not implemented")
        }

        override fun onError(ex: Exception?) {
            TODO("onError(   ) not implemented")
        }

    }

    var wsConnOk = false
    val wsConnMutex
    val sesion
    val listener
    val listenerMutex
    val writeChan
    val handler
    val msgCount
    val Info
    val store
    val serverLastSeen

    val longClientName = "koppa/java-whatsapp"
    val shortClientName = "java-whatsapp"



    fun login(qrcallback: (qr: String) -> Unit) : Session {

    }




    fun decryptBinaryMessage(byten : ByteArray) : Unit {
        //message validation
        val h2 = MessageDigest.getInstance("sha256", macKey)
        val ret = h2.digest(byten.sliceArray(32..byten.size))
        assert(ret.size == 32)
        if (! ret.equals(byten.slice(0..32))) {
            throw Error("message received with invalid hmac")
        }
        // message decrypt
        d, err := cbc.Decrypt(wac.session.EncKey, nil, msg[32:])
        if err != nil {
            return nil, fmt.Errorf("error decrypting message with AES: %v", err)
        }

        // message unmarshal
        message, err := binary.Unmarshal(d)
        if err != nil {
            return nil, fmt.Errorf("error decoding binary: %v", err)
        }

        return message, nil
    }

}


