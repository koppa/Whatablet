package whatsapp

import whatsapp.WhatsappConn

fun test () {
   val conn = WhatsappConn(5);

   val session = conn.login { x -> println(x)}

    println("Login successfussll")
}