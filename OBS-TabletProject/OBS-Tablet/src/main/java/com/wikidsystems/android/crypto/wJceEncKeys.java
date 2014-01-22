package com.wikidsystems.android.crypto;

import com.wikidsystems.android.client.WiKIDToken;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class wJceEncKeys extends wEncKeys {
	private KeyPair ekeys;
	private Cipher cipher;
		
//	static {
//		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//	}

	private void init(String cipherTran) throws NoSuchAlgorithmException, NoSuchPaddingException {
        cipher = Cipher.getInstance(cipherTran);  		
	}

	/**
	 * The constructor for generating a new keypair (using a pseudo-random number generator).
	 */
	protected wJceEncKeys(String keyAlg, String cipherTransformation, int keySize) throws wCryptoException {
		super();
		
		try {
			init(cipherTransformation);

			// optional second arg: the desired provider
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(keyAlg);
			
			SecureRandom rnd = java.security.SecureRandom.getInstance("SHA1PRNG");
	
			// optional second arg: a SecureRandom object / source of randomness.
			kpg.initialize(keySize, rnd);
	
			ekeys = kpg.generateKeyPair();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new wCryptoException(e);
		}
	}

	/**
	 * The constructor for importing (i.e. deserializing) an existing keypair.
	 * (Doesn't actually have to be the full pair - it can be just a public key, or just a private key.)
	 */	
	protected wJceEncKeys(String keyAlg, String cipherTransformation, byte[] publicKeyBytes, byte[] privateKeyBytes) throws wCryptoException {
		super();
				
		try {
			init(cipherTransformation);
			
			PublicKey  pubk  = null;
			PrivateKey privk = null;
			
			KeyFactory keyFactory = KeyFactory.getInstance( keyAlg );

			if (null != publicKeyBytes) {
				X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKeyBytes);
				pubk = keyFactory.generatePublic(pubKeySpec);
			}
			
			if (null != privateKeyBytes) {
				PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);	
				privk = keyFactory.generatePrivate(privKeySpec);
			}
			
			ekeys = new KeyPair(pubk, privk);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new wCryptoException(e);
		}
	}

    public PublicKey getPublicKey(){
        return  ekeys == null ? null :ekeys.getPublic();
    }

    public PrivateKey getPrivateKey(){
        return  ekeys == null ? null :ekeys.getPrivate();
    }

    public byte[] exportPubKey() {
		return ekeys.getPublic().getEncoded();
	}

	public byte[] exportPrivKey() {
		return ekeys.getPrivate().getEncoded();
	}

	private void writeKeyBytes(DataOutputStream dos, byte[] bytes) throws IOException {
		dos.writeInt(bytes.length);
		dos.write(bytes);
	}
	
	public void writePubKeyBytes(DataOutputStream dos) throws IOException {
		writeKeyBytes(dos, exportPubKey());
	}

	public void writePrivKeyBytes(DataOutputStream dos) throws IOException {
		writeKeyBytes(dos, exportPrivKey());
	}

	/**
	 * So a cipher transformation string looks like "RSA/ECB/PKCS1PADDING"...
	 * 
	 * I would think if the mode is "ECB" instead of "NONE" that I could pass the cipher say
	 * ten thousand bytes and it would happily handle breaking the bytes up into blocks, encrypting/decrypting
	 * them and hand me back the concatenated results.  (I think this was my experience with a DES cipher.)
	 *  
	 * But at least with Bouncycastle's RSA, it looks like we have to encrypt/decrypt each block separately.
	 * If you give the cipher a thousand bytes it complains you're exceeding the block size.
	 * 
	 * So here is the method to do what the Cipher won't do for me: break the input into blocks, encrypt/decrypt
	 * each and concatenate the result.
	 * 
	 * Since I'm not explicitly dealing with initialization vectors and such, I think I'm implimenting ECB 
	 * (Electronic Code Book) below.  I'm not sure what you're getting if the cipher transformation string's 
	 * mode is something like "CBC" (Cipher Block Chaining) and you successfully instantiate a Cipher object.  
	 * I imagine as long as this method is being called to process a block at a time that you'll either get 
	 * something that doesn't work (since no initialization vectors are explicitly identified and sent/remembered) 
	 * or that is no more secure than ECB.
	 * 
	 * But I don't really know.
	 */
	private static byte[] convertBlocks(Cipher cipher, byte[] input, boolean decryptMode) throws javax.crypto.IllegalBlockSizeException, java.io.IOException, javax.crypto.BadPaddingException {
		
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		
		int numOfBlocks = 0;
		int bs = cipher.getBlockSize();
        if (bs==0) bs=128;
        WiKIDToken.setMsg("Cipher's block size is "+ bs);
//		System.out.println("Cipher's block size is "+ bs);

		for (int i = 0; i < input.length; i += bs) {
			
			int len;
			if (i + bs <= input.length) {
				len = bs;
			} else {
				len = input.length - i;
			}
			
			byte[] out = cipher.doFinal(input, i, len);
			bOut.write( out );
//			System.out.println("just converted "+ len +" bytes to "+ out.length +" bytes");
			numOfBlocks++;
		}

//		System.out.println("number of blocks converted: "+ numOfBlocks);
		
		bOut.close();
		return bOut.toByteArray();
	}

	
	public byte[] unpackagePayload(byte[] ciphertext) throws wCryptoException {
        byte[] output = new byte[0];

        try {
            cipher.init(Cipher.DECRYPT_MODE, ekeys.getPrivate());
            output = convertBlocks(cipher, ciphertext, true);
            //output = cipher.doFinal(ciphertext);
        } catch(IllegalBlockSizeException ibse){
            System.out.println("illegal block size!!");
        } catch (Exception e) {
        	e.printStackTrace();
        	throw new wCryptoException(e);
        }

        return output;
	}
	
	public byte[] packagePayload(byte[] input) throws wCryptoException {
        byte[] output = new byte[0];
       try {
            cipher.init(Cipher.ENCRYPT_MODE, ekeys.getPublic());

//            output = convertBlocks(cipher, input, false);
            output = cipher.doFinal(input);

        }
        catch (Exception e) {
            e.printStackTrace();
            throw new wCryptoException(e);
        }
        
        return output;
	}
}