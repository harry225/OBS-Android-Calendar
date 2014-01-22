package com.wikidsystems.android.crypto;

import java.io.DataInputStream;
import java.io.IOException;

public class wJceEncKeysFactory extends wEncKeysFactory {
	String kA, ciphertran;
	int kS;
	
	public wJceEncKeysFactory(String keyAlg, String cipherTransformation, int keySize) {
		super();
		kS = keySize;
		kA = keyAlg;
		ciphertran = cipherTransformation;
	}
	
	public wEncKeys generatePair() throws wCryptoException {
		return new wJceEncKeys(kA, ciphertran, kS);
	}

	public wEncKeys create(byte[] publicKey, byte[] privateKey) throws wCryptoException {
		return new wJceEncKeys(kA, ciphertran, publicKey, privateKey);
	}

	private byte[] readKeyBytes(DataInputStream dis) throws IOException {
		int bytesToRead = dis.readInt();
		
		byte[] rv = new byte[ bytesToRead ];
		
		dis.readFully(rv);
		
		return rv;
	}
	
	public byte[] readPubKeyBytes(DataInputStream dis) throws IOException {
		return readKeyBytes(dis);
	}

	public byte[] readPrivKeyBytes(DataInputStream dis) throws IOException {
		return readKeyBytes(dis);
	}
}