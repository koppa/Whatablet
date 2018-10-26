package whatsapp.cbc

import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

fun decrypt(key: ByteArray, iv: ByteArray?, code: ByteArray) : ByteArray {

    var secret = SecretKeySpec(key, "aes")
    var instance = Cipher.getInstance("aes");
    instance.init(Cipher.DECRYPT_MODE, secret);

    if (code.size < instance.blockSize) {
        throw Error("ciphertext is shorter then block size: ${code.size} / ${instance.blockSize}")
    }

    if (iv == null) {
        val iv_ = code.slice(0..instance.blockSize-1).toByteArray()
        val code_ = code.slice(instance.blockSize..code.size-1).toByteArray()
    } else {
        val iv_ = iv
        val code_ = code
    }

    cbc := cipher.NewCBCDecrypter(block, iv)
    cbc.CryptBlocks(ciphertext, ciphertext)

    return unpad(ciphertext)

}

fun encrypt(key: ByteArray, iv: ByteArray, plain: ByteArray) : ByteArray {

}

