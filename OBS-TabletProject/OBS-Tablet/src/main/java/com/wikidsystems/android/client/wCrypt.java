package com.wikidsystems.android.client;

import com.wikidsystems.android.crypto.wCryptoException;
import com.wikidsystems.android.crypto.wEncKeys;
import com.wikidsystems.android.crypto.wEncKeysFactory;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class wCrypt {

    private static wEncKeysFactory wkeyfactory_tmp;
    private static String cryptoQSS_tmp, rs_file_suffix_tmp;

    static {
////ifndef oss
//        String cryptoType = System.getProperty("wikid.crypto");
//
//        if ((null == cryptoType) || cryptoType.equalsIgnoreCase("ntru")) {
//            if (null != System.getProperty("wikid.keysize")) {
//                System.err.println("Please do not specify the keysize with ntru/classic crypto.");
//                System.exit(1);
//            }
//
//            //wkeyfactory_tmp    = new com.wikidsystems.crypto.wNtruEncKeysFactory();
//            cryptoQSS_tmp = "";
//            rs_file_suffix_tmp = "";
//            System.out.println("Going to use classic/ntru cryptography.");
//        } else if (cryptoType.equalsIgnoreCase("rsa")) {
////endif
//            int keySize = Integer.parseInt(
//                    System.getProperty("wikid.keysize", "1024")
//            );
//
            wkeyfactory_tmp = new com.wikidsystems.android.crypto.wJceEncKeysFactory("RSA", "RSA/ECB/PKCS1PADDING", 1024);
            cryptoQSS_tmp = "CT=1";
            rs_file_suffix_tmp = ".JCE.RSA";

//ifndef oss
//            System.out.println("Going to use rsa cryptography.");
//        }
//        // ECC has not been added to the server Db yet.
////		else if (cryptoType.equalsIgnoreCase("ec") || cryptoType.equalsIgnoreCase("ecc")) {
////			int keySize = Integer.parseInt(
////					System.getProperty("wikid.keysize", "239")
////			);
////
////			wkeyfactory_tmp    = new com.wikidsystems.crypto.wJceEncKeysFactory("ECIES", "ECIES", keySize);
////			cryptoQSS_tmp      = "CT=2";
////			rs_file_suffix_tmp = ".JCE.ECC";
////
////			System.out.println("Going to use E.Curve cryptography.");
////		}
//        else {
//            System.err.println("Sorry, I don't know how to use crypto type \"" + cryptoType + "\"");
//            System.exit(2);
//        }
////endif
    }

    public static final wEncKeysFactory wkeyfactory = wkeyfactory_tmp;
    public static final String CRYPTO_QUERY_STRING_SEGMENT = cryptoQSS_tmp;
    public static final String RS_FILE_SUFFIX = rs_file_suffix_tmp;

    wEncKeys ekeys = null;
    boolean keyGenDone = false;

    TokenConfiguration tokenConfig;

    public wCrypt() {
        ekeys = null;
    }

    public void setWd(TokenConfiguration tc) {
        tokenConfig = tc;
    }

//	protected wEncKeys generateKeys()
//	{
//		try {
//			ekeys = wkeyfactory.generatePair();
//			//System.out.println("PubKeySize:" + ekeys.exportPubKey(false).length + " , PrivKeySize:" + ekeys.exportPrivKey(false).length );
//			wdObj.setDeviceKeys(ekeys);
//			keyGenDone = true;
//		}
//		catch (wCryptoException ne) {
//			ne.printStackTrace();
//		}
//		return ekeys;
//	}

//	public void run()
//	{
//		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
//		Thread.currentThread().yield();
//		ekeys = generateKeys();
//		keyGenDone = true;
//	}

    protected boolean done() {
        return keyGenDone;
    }

    protected wEncKeys getKeys() {
        return ekeys;
    }

    protected static byte[] packagePayload(String payload, WiKIDDomain dest) throws wCryptoException {
        byte[] input = payload.getBytes();
        return dest.getEncKeys(1024).packagePayload(input);
    }

    protected static byte[] getAESData() {
        // Generate 16 bytes for AES key
        byte[] aesSeed = new byte[16];
        SecureRandom rnd = null;
        try {
            rnd = java.security.SecureRandom.getInstance("SHA1PRNG");
        }
        catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            System.out.println("No AES crypto provider available. Quitting.");
            System.exit(3);
        }
        rnd.nextBytes(aesSeed);

        return aesSeed;
    }

    public static byte[] AESDecrypt(byte[] ciphertext, byte[] seed) {
        if(ciphertext.length<16)return ciphertext;
        Key rk;
        Cipher decrypt;
        byte[] plain = null;
        try {
            rk = new SecretKeySpec(seed, "AES");
            decrypt = Cipher.getInstance("AES");
            decrypt.init(Cipher.DECRYPT_MODE, rk);
            plain = decrypt.doFinal(ciphertext);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("AES Decrypt blew up");
        }
        return plain;
    }
}
