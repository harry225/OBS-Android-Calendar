package com.wikidsystems.android.crypto;

import java.io.DataInputStream;
import java.io.IOException;


public abstract class wEncKeysFactory {

	public abstract wEncKeys generatePair() throws wCryptoException;

	public abstract wEncKeys create(byte[] publicKey, byte[] privateKey) throws wCryptoException;

	public abstract byte[] readPubKeyBytes(DataInputStream dis) throws IOException;

	public abstract byte[] readPrivKeyBytes(DataInputStream dis) throws IOException;

}