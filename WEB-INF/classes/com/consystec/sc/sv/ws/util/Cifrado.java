package com.consystec.sc.sv.ws.util;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Esta clase es la encargada de encriptar y desencriptar una cadena de
 * caracteres proporcionando una llave unica, con lo cual se logra generar un
 * valor unico por llave y valor de cadena.
 * 
 * @author CONSYSTEC
 *
 */
public class Cifrado extends ControladorBase {

    /*
     * AES es el algorito de encriptacion que se esta usando
     */
    public static final String AES = "AES";
    /*
     * En caso la cadena no tiene la longitud necesaria se complementa con la
     * variable Keysufix, este es un valor concatenado al final de la cadena
     */

    public static final String KEYSUFIX = "CON2010SYS03TEC01";

    /*
     * Este es un valor agregador en caso no se complete la cadena + keysufix
     */
    public static final String KEYAUX = "94F5CAAF5E92554DEBEF4734CD569A01";

    /**
     * La funcion convierte una cadena con texto comun a una cadena representada
     * en valor hexadecimal.
     * 
     * @param word
     *            Cadena la cual se desea convertir a hexdecimal
     * @return Cadena hexadecimal dada una cadena como argumento
     */
    public static String stringToHexString(String word) {
        byte[] b = word.getBytes();
        return byteArrayToHexString(b);
    }

    /**
     * Convierte un arreglo de bytes en una variable String que contenga un
     * valor hexadecimal
     * 
     * @param b
     * @return Variable String con un valor hexadecimal.
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * Convierte una cadena que contiene un valor en hexadecimal a un arreglo de
     * bytes.
     * 
     * @param s
     * @return byte[]
     */
    private static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    /**
     * Encripta la cadena "value" dada su llave, esto con el algoritmo AES.
     * 
     * @param value
     * @param key
     * @return String
     * @throws GeneralSecurityException
     */
    public static String encrypt(String value, String key) throws GeneralSecurityException {
        String keyc = stringToHexString((key + KEYSUFIX));
        if (keyc.length() < 32)
            keyc = keyc + KEYAUX;

        byte[] a1 = hexStringToByteArray(keyc.substring(0, 32));

        SecretKeySpec sks = new SecretKeySpec(a1, AES);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());

        byte[] encrypted = cipher.doFinal(value.getBytes());
        return byteArrayToHexString(encrypted);
    }

    /**
     * Desencipta la variable "message" seg\u00FAn la llave "key" dados en los
     * argumentos.
     * 
     * @param message
     * @param key
     * @return String
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws GeneralSecurityException
     */
    public static String decrypt(String message, String key) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String keyc = stringToHexString((key + KEYSUFIX));
        if (keyc.length() < 32)
            keyc = keyc + KEYAUX;
        byte[] a1 = hexStringToByteArray(keyc.substring(0, 32));

        SecretKeySpec sks = new SecretKeySpec(a1, AES);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, sks);

        byte[] decrypted = cipher.doFinal(hexStringToByteArray(message));
        return new String(decrypted);
    }
}
