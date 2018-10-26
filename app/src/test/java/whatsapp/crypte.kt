package whatsapp

import whatsapp.cbc.decrypt
import whatsapp.cbc.encrypt


fun test_cbc() {

    val key = "MySecretSecretSecretSecretKey123"
    val plain  = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."

    val cipher = encrypt(key, null, plain)
    val p = decrypt(key, null, cipher)

    assert(plain.equals(p))
}