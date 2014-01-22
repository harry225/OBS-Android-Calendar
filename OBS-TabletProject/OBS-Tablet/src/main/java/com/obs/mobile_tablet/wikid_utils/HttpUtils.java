// $Id: HttpUtils.java,v 1.6 2007/02/21 23:54:21 jblum Exp $
package com.obs.mobile_tablet.wikid_utils;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class HttpUtils
 *
 * @author Jason Blumenkrantz
 * @author Copyright(c) 2006 Online Banking Solutions. All Rights Reserved
 * @version $Revision: 1.6 $
 */
public final class HttpUtils {
    
    //-----------------------------------------------------------------------
    // Constants
    //----------------------------------------------------------------------- 

//    private static Object[] EV_POLICIES;
//
//    static {
//        try {
//            EV_POLICIES = new Object[]{
//                    Class.forName("sun.security.util.ObjectIdentifier").getConstructor(String.class).newInstance("2.16.840.1.113733.1.7.23.6"), // Verisign
//                    Class.forName("sun.security.util.ObjectIdentifier").getConstructor(String.class).newInstance("1.3.6.1.4.1.14370.1.6"), // Geo-Trust of Verisign
//                    Class.forName("sun.security.util.ObjectIdentifier").getConstructor(String.class).newInstance("2.16.840.1.113733.1.7.48.1") // Thawte
//            };
//        } catch (Exception e) {
//            throw new IllegalStateException("Invalid OIDs");
//        }
//    }
//
//    private static Map CIPHER_SUITES = new HashMap();
//    static {
//        CIPHER_SUITES.put("TLS_RSA_WITH_AES_128_CBC_SHA", 128);
//        CIPHER_SUITES.put("TLS_RSA_WITH_AES_256_CBC_SHA", 256);
//        CIPHER_SUITES.put("TLS_DHE_RSA_WITH_AES_128_CBC_SHA", 128);
//        CIPHER_SUITES.put("TLS_DHE_RSA_WITH_AES_256_CBC_SHA", 256);
//        CIPHER_SUITES.put("TLS_DHE_DSS_WITH_AES_128_CBC_SHA", 128);
//        CIPHER_SUITES.put("TLS_DHE_DSS_WITH_AES_256_CBC_SHA", 256);
//        CIPHER_SUITES.put("TLS_DH_anon_WITH_AES_128_CBC_SHA", 128);
//        CIPHER_SUITES.put("TLS_DH_anon_WITH_AES_256_CBC_SHA", 256);
//        CIPHER_SUITES.put("SSL_RSA_WITH_3DES_EDE_CBC_SHA", 168);
//        CIPHER_SUITES.put("SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA", 168);
//        CIPHER_SUITES.put("SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA", 168);
//        CIPHER_SUITES.put("SSL_RSA_WITH_RC4_128_MD5", 128);
//        CIPHER_SUITES.put("SSL_RSA_WITH_RC4_128_SHA", 128);
//        CIPHER_SUITES.put("SSL_RSA_WITH_DES_CBC_SHA", 56);
//        CIPHER_SUITES.put("SSL_DHE_RSA_WITH_DES_CBC_SHA", 56);
//        CIPHER_SUITES.put("SSL_DHE_DSS_WITH_DES_CBC_SHA", 56);
//        CIPHER_SUITES.put("SSL_RSA_EXPORT_WITH_RC4_40_MD5", 40);
//        CIPHER_SUITES.put("SSL_RSA_EXPORT_WITH_DES40_CBC_SHA", 40);
//        CIPHER_SUITES.put("SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA", 40);
//        CIPHER_SUITES.put("SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA", 40);
//        CIPHER_SUITES.put("SSL_RSA_WITH_NULL_MD5", 0);
//        CIPHER_SUITES.put("SSL_RSA_WITH_NULL_SHA", 0);
//        CIPHER_SUITES.put("SSL_DH_anon_WITH_RC4_128_MD5", 128);
//        CIPHER_SUITES.put("SSL_DH_anon_WITH_3DES_EDE_CBC_SHA", 168);
//        CIPHER_SUITES.put("SSL_DH_anon_WITH_DES_CBC_SHA", 56);
//        CIPHER_SUITES.put("SSL_DH_anon_EXPORT_WITH_RC4_40_MD5", 40);
//        CIPHER_SUITES.put("SSL_DH_anon_EXPORT_WITH_DES40_CBC_SHA", 40);
//    }


    //-----------------------------------------------------------------------
    // Class methods
    //-----------------------------------------------------------------------

    public static String doDownload(HttpURLConnection http) throws IOException {
        try {
            InputStream is = http.getInputStream();
            return FileUtils.readStreamAsString(is);
        } finally {
            http.disconnect();
        }
    }

    public static HttpURLConnection openConnection(URL url, final ProxySettings proxySettings) throws IOException {
        HttpURLConnection connection = null;
        connection = getJava15Connection(url, proxySettings);
        connection.setDefaultUseCaches(false);
        connection.setUseCaches(false);
        if (connection instanceof HttpsURLConnection) {
            ((HttpsURLConnection) connection).setHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String string, SSLSession sslSession) {
                    return true;
                }
            });
        }
        connection.setRequestProperty("Connection", "close");
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        return connection;
    }

//    public static Certificate[] getCertificate(String url, ProxySettings proxySettings) throws Exception {
//        return getCertificate(url, proxySettings, null);
//    }
//    public static Certificate[] getCertificate(String url, ProxySettings proxySettings, SSLContext sslContext) throws Exception {
//        Certificate[] certificates = null;
//        URL urlObject = new URL(url);
//        if ("https".equalsIgnoreCase(urlObject.getProtocol())) {
//            int port = urlObject.getPort();
//            if (port == -1) {
//                port = 443;
//            }
//            HttpsURLConnection connection = (HttpsURLConnection) openConnection(urlObject, proxySettings);
//            if (connection instanceof HttpsURLConnection && sslContext != null) {
//                ((HttpsURLConnection) connection).setSSLSocketFactory(sslContext.getSocketFactory());
//            }
//            connection.getInputStream();
//            certificates = connection.getServerCertificates();
//        }
//        return certificates;
//    }
//
//    public static CertificateInfo getCertificateInfo(String url, ProxySettings proxySettings) throws Exception {
//        return getCertificateInfo(url, proxySettings, null);
//    }
//
//    public static CertificateInfo getCertificateInfo(String url, ProxySettings proxySettings, SSLContext sslContext) throws Exception {
//        SSLContext ctx = null;
//        URL urlObject = new URL(url);
//        if ("https".equalsIgnoreCase(urlObject.getProtocol())) {
//            int port = urlObject.getPort();
//            if (port == -1) {
//                port = 443;
//            }
//            HttpsURLConnection connection = (HttpsURLConnection) openConnection(urlObject, proxySettings);
//            if (connection instanceof HttpsURLConnection && sslContext != null) {
//                ((HttpsURLConnection) connection).setSSLSocketFactory(sslContext.getSocketFactory());
//            }
//            connection.getInputStream();
//            X509Certificate certificate = (X509Certificate)connection.getServerCertificates()[0];
//            return new CertificateInfo(certificate, connection.getCipherSuite());
//        }
//        return null;
//    }
//
//    public static byte[] getCertificateHash(Certificate cert) throws Exception {
//        return DigestUtils.generateFingerprint(cert.getEncoded(),"MD5");
//    }
    
//    public static boolean isEVCertificate(Certificate cert) throws Exception {
//        //Uses reflection to avoid sun private warnings
//        //CertificatePoliciesExtension ext = ((X509CertImpl) cert).getCertificatePoliciesExtension();
//        //List<PolicyInformation> policies = (List<PolicyInformation>) ext.get(CertificatePoliciesExtension.POLICIES);
//        //boolean evCert = false;
//        //for (PolicyInformation info : policies) {
//        //   CertificatePolicyId id = info.getPolicyIdentifier();
//        //   if (isEVPolicy(id)) {
//        //       evCert = true;
//        //       break;
//        //    }
//        //}
//        Object ext = Class.forName("sun.security.x509.X509CertImpl").getMethod("getCertificatePoliciesExtension").invoke(cert);
//        if (ext == null) return false;
//        List policies = (List)Class.forName("sun.security.x509.CertificatePoliciesExtension").getMethod("get", String.class).invoke(ext, "policies");
//        for (Object info : policies) {
//            Object cpid = Class.forName("sun.security.x509.PolicyInformation").getMethod("getPolicyIdentifier").invoke(info);
//            Object id = Class.forName("sun.security.x509.CertificatePolicyId").getMethod("getIdentifier").invoke(cpid);
//            for (Object oid : EV_POLICIES) {
//                if (oid.equals(id)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
    
    //-----------------------------------------------------------------------
    // Constructors.
    //-----------------------------------------------------------------------

    private HttpUtils() {
    }
    
    //-----------------------------------------------------------------------
    // Inner classes 
    //-----------------------------------------------------------------------

//    public static class CertificateInfo {
//        private X509Certificate _certificate;
//        private byte[] _hash;
//        private boolean _isEV;
//        private String _cipherSuite;
//        private int _cipherStrength;
//
//        public CertificateInfo(X509Certificate certificate, String cipherSuite) throws Exception {
//            _certificate = certificate;
//            _hash = HttpUtils.getCertificateHash(_certificate);
//            _cipherSuite = cipherSuite;
//            _cipherStrength = (Integer)HttpUtils.CIPHER_SUITES.get(cipherSuite);
//            _isEV = HttpUtils.isEVCertificate(_certificate);
//        }
//
//        public X509Certificate getCertificate() {
//            return _certificate;
//        }
//
//        public byte[] getCertificateHash() {
//            return _hash;
//        }
//
//        public boolean isEV() {
//            return _isEV;
//        }
//
//        public String getCipherSuite() {
//            return _cipherSuite;
//        }
//
//        public int getCipherStrength() {
//            return _cipherStrength;
//        }
//    }
    //-----------------------------------------------------------------------
    // Private methods
    //-----------------------------------------------------------------------

    private static HttpURLConnection getJava14Connection(URL url, ProxySettings proxySettings)
            throws IOException {
        if (proxySettings != null && proxySettings.isProxyEnabled()) {
            System.setProperty("http.proxyHost", proxySettings.getProxyHost());
            System.setProperty("https.proxyHost", proxySettings.getProxyHost());
            System.setProperty("http.proxyPort", String.valueOf(proxySettings.getProxyPort()));
            System.setProperty("https.proxyPort", String.valueOf(proxySettings.getProxyPort()));
            Authenticator.setDefault(new ProxyAuthenticator(proxySettings));
        } else {
            System.getProperties().remove("http.proxyHost");
            System.getProperties().remove("https.proxyHost");
            System.getProperties().remove("http.proxyPort");
            System.getProperties().remove("https.proxyPort");
            Authenticator.setDefault(new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("none", new char[0]);
                }
            });
        }
        return (HttpURLConnection) url.openConnection();
    }

    private static HttpURLConnection getJava15Connection(URL url, ProxySettings proxySettings)
            throws IOException {
        //Treat this to mean the system should handle it, for the applet context
        if (proxySettings == null) {
            return (HttpURLConnection) url.openConnection();
        }
        Object proxy = null;
        try {
            Class proxyClass = Class.forName("java.net.Proxy");
            if (proxySettings != null && proxySettings.isProxyEnabled()) {
                InetSocketAddress isa = new InetSocketAddress(proxySettings.getProxyHost(), proxySettings.getProxyPort());
                Class proxyTypeClass = Class.forName("java.net.Proxy$Type");
                Constructor ctor = proxyClass.getConstructor(new Class[]{proxyTypeClass, SocketAddress.class});
                Object httpProxyType = proxyTypeClass.getField("HTTP").get(null);
                proxy = ctor.newInstance(new Object[]{httpProxyType, isa});
                Authenticator.setDefault(new ProxyAuthenticator(proxySettings));
            } else {
                proxy = proxyClass.getField("NO_PROXY").get(null);
                Authenticator.setDefault(new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("none", new char[0]);
                    }
                });
            }
            Method openConnection = URL.class.getMethod("openConnection", new Class[]{proxyClass});
            return (HttpURLConnection) openConnection.invoke(url, new Object[]{proxy});
        } catch (Exception ex) {
//            LOG.error("Reflection error creating Java 1.5 proxy: " + ex.getMessage(), ex);
            throw new IOException(ex.getMessage());
        }
    }

    //-----------------------------------------------------------------------
    // Mainline
    //-----------------------------------------------------------------------

//    public static void main(String[] args) {
//        try {
//            CertificateInfo cert = HttpUtils.getCertificateInfo(args[0], null);
//            System.out.println("Suite: " + cert.getCipherSuite());
//            System.out.println("Strength: " + cert.getCipherStrength());
//            System.out.println("Is EV: " + cert.isEV());
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//            e.printStackTrace();
//        }
//        System.exit(0);
//    }
}

