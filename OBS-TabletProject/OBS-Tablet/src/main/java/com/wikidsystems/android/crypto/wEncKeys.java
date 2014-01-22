package com.wikidsystems.android.crypto;

import java.io.DataOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

public abstract class wEncKeys {

    // While constructors and their parameters are not defined here,
    // the concrete class should probably have a constructor for generating a new keypair,
    // and a constructor for importing/deserializing an existing keypair.
    // (Or maybe some static methods if not constructors.)

    public abstract byte[] exportPubKey();

    public abstract byte[] exportPrivKey();

    public abstract PublicKey getPublicKey();

    public abstract PrivateKey getPrivateKey();

    public abstract byte[] unpackagePayload(byte[] ciphertext) throws wCryptoException;

    public abstract byte[] packagePayload(byte[] plaintext) throws wCryptoException;

    public abstract void writePubKeyBytes(DataOutputStream dos) throws IOException;

    public abstract void writePrivKeyBytes(DataOutputStream dos) throws IOException;
}