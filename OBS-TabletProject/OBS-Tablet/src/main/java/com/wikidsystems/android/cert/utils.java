package com.wikidsystems.android.cert;

import com.wikidsystems.android.util.B64;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.cert.Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;





public class utils 
{

    public static final boolean DEBUG = false;

    public static Certificate getCertificate(String hostname)
	{
		if (DEBUG) System.err.println("getCertificate(): hostname = " + hostname);
		return getCertificate(hostname, 443);
	}

	public static Certificate getCertificate(String hostname, int port) 
	{
		Certificate cert = null;
		String hostname2 = hostname;

		if (DEBUG) System.err.println("getCertificate(): hostname = " + hostname + "; port = " + port);

		try {
			int i = -1;
			if ( (i=hostname.indexOf("://")) > 0 ) {
				hostname = hostname.substring(i+3);
				if ( (i=hostname.indexOf(":")) > 0 ) {
					hostname = hostname.substring(0, i);
				} else if ( (i=hostname.indexOf("/")) > 0 ) {
					hostname = hostname.substring(0, i);
				}
			}
		} catch (Exception ex) {
			if (DEBUG) System.err.println("getCertificate(): error parsing hostname: " + ex.toString());
			hostname = hostname2;
		}

		Socket socket = null;

		// Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[] {
			new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				public void checkClientTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
				}
				public void checkServerTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
				}
			}
    };

		try {
			if (DEBUG) System.err.println("getCertificate(): host = " + hostname);
			if (DEBUG) System.err.println("getCertificate(): port = " + port);

			// Create the client socket
			if (DEBUG) System.err.println("getCertificate(): starting socket connection ...");

			System.setProperty("java.protocol.handler.pkgs","com.sun.net.ssl.internal.www.protocol");

			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			// Install the all-trusting trust manager
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			SSLSocketFactory factory = HttpsURLConnection.getDefaultSSLSocketFactory();

			if (DEBUG) System.err.println("getCertificate(): opening socket ...");
			socket = factory.createSocket(hostname, port);

			// Connect to the server
			if (DEBUG) System.err.println("getCertificate(): connected to site ...");
			//socket.startHandshake();

			// send an HTTP command, just to be a good netizen
			OutputStream outputStream = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outputStream);
			out.print("HEAD / HTTP/1.0\r\n\r\n");
			out.flush();
			out.close();

			if (DEBUG) System.err.println("getCertificate(): fetching certs ...");
			// Retrieve the server's certificate chain
			Certificate[] serverCerts = ((SSLSocket)socket).getSession().getPeerCertificates();

			// return just the host's certificate, not the full chain
			cert = serverCerts[0];

			socket.close();
			socket = null;

		} catch (SSLPeerUnverifiedException e) {
			if (DEBUG) System.err.println("getCertificate(): closing the socket ...");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (socket != null || !socket.isClosed()) 
				{
					// Close the socket
					if (DEBUG) System.err.println("getCertificate(): closing the socket ...");
					socket.close();
					socket = null;
				}
			} catch (Exception e) {
				;
			}
		}

		return cert;
	}

	public static String getHash(String url) {
		if (DEBUG) System.err.println("getHash(): url = " + url);
		return getHash(getCertificate(url));
	}

	public static String getHash(Certificate cert) 
	{
		String hash_str = null;

		if (cert != null) {
			try {
				byte[] enc = cert.getEncoded();
				String msg = new String(enc);

				MessageDigest hash = MessageDigest.getInstance("SHA1");
				hash.update(enc, 0, enc.length);
				byte[] regHash = hash.digest();
				if (DEBUG) System.err.println("getHash(): starting with hash: " + regHash.toString());

/*
				long total = 1;
				for (int i = 0; i < regHash.length; i++) {
					total = total + (long) (Math.pow( (int) regHash[i], (int)(regHash.length/2)));
				}
				hash_str = wUtil.b62(total);
*/
				hash_str = new String(B64.encodeBytes(regHash));
				// hash_str = wUtil.b62(regHash); //.toString();

				if (DEBUG) System.err.println("getHash(): returning hash: " + hash_str);
			} catch (Throwable ex) {
				hash_str = null;
				if (DEBUG) System.err.println("getHash(): error = " + ex.toString() + "; returning null");
				ex.printStackTrace();
			}
		}

		return hash_str;
	}
}
