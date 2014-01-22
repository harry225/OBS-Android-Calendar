package com.wikidsystems.android.client;


import com.wikidsystems.android.crypto.wCryptoException;
import com.wikidsystems.android.crypto.wEncKeys;
import com.wikidsystems.android.crypto.wEncKeysFactory;
import com.wikidsystems.android.util.B64;

import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

//import com.obs.android.HomeMenu;

/**
 * Created by Eric Shoemaker
 * Date: Dec 25, 2005
 * Time: 6:17:17 PM
 */
public class TokenConfiguration {

    static {
//        String cryptoType = System.getProperty("wikid.crypto");
//
//        if ((null == cryptoType) || cryptoType.equalsIgnoreCase("ntru")) {
//            if (null != System.getProperty("wikid.keysize")) {
//                System.err.println("Please do not specify the keysize with ntru/classic crypto.");
//                System.exit(1);
//            }
//            System.out.println("Going to use classic/ntru cryptography.");
//        }
//        else if (cryptoType.equalsIgnoreCase("rsa")) {
//            int keySize = Integer.parseInt(
//                    System.getProperty("wikid.keysize", "1024")
//            );
        int keySize = 1024;
        wkeyfactory = new com.wikidsystems.android.crypto.wJceEncKeysFactory("RSA", "RSA/ECB/PKCS1PADDING", keySize);
//            System.out.println("Going to use rsa cryptography.");
//        }
//        else {
//            System.err.println("Sorry, I don't know how to use crypto type \""+ cryptoType +"\"");
//            System.exit(2);
//        }
    }

    private static wEncKeysFactory wkeyfactory;
    public static final int TOKEN_CONFIGURATION_PARSER_VERSION = 3;

    private int configVersion = -1; //the version of the configuration file
    private long deviceID = -1;
    private wEncKeys keys;
    private final List<WiKIDDomain> domains = new ArrayList<WiKIDDomain>();
    private final HashMap domainsByName = new HashMap();
    private final HashMap domainsByCode = new HashMap();
    private boolean populatedWithDuplicateNames = false;
    private boolean proxyEnabled;
    private String proxyHost;
    private String proxyPort;

    private static File configFile;
    private static char[] configFilePassphrase;

    public File getConfigFile(){
        return configFile;
    }

    public char[] getConfigFilePassphrase(){
        return configFilePassphrase;
    }

    public int getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(int configVersion) {
        this.configVersion = configVersion;
    }

    public long getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(long deviceID) {
        this.deviceID = deviceID;
    }

    public wEncKeys getKeys() {
        return keys;
    }

    public void setKeys(wEncKeys keys) {
        this.keys = keys;
    }

    public List getDomains() {
        return domains;
    }

    public void setDomains(List domains) {
        ArrayList l = new ArrayList();
        domainsByName.clear();
        domainsByCode.clear();
        if(isPopulatedWithDuplicateNames()){
            for(Iterator i=domains.iterator();i.hasNext();){
                WiKIDDomain wd = (WiKIDDomain) i.next();
                l.add(wd);
                domainsByName.put(wd.getName()+"/"+wd.getServerCode(), wd);
                domainsByCode.put(wd.getServerCode(), wd);
            }
        } else {
            for(Iterator i=domains.iterator();i.hasNext();){
                WiKIDDomain wd = (WiKIDDomain) i.next();
                if(domainsByName.containsKey(wd.getName())){
                    setPopulatedWithDuplicateNames(true);
                    break;
                }
                l.add(wd);
                domainsByName.put(wd.getName(), wd);
                domainsByCode.put(wd.getServerCode(), wd);
            }
            if(isPopulatedWithDuplicateNames()){
                setDomains(domains);
                return;
            }
        }
        this.domains.clear();
        this.domains.addAll(l);
    }

    public void addDomain(WiKIDDomain newDomain){
        getDomains().add(newDomain);
        setDomains(getDomains());
//        domainsByName.clear();
//        domainsByCode.clear();
//        if(isPopulatedWithDuplicateNames()){
//            for(Iterator i=domains.iterator();i.hasNext();){
//                WiKIDDomain wd = (WiKIDDomain) i.next();
//                domainsByName.put(wd.getName()+"/"+wd.getServerCode(), wd);
//                domainsByCode.put(wd.getServerCode(), wd);
//            }                
//        } else {
//            for(Iterator i=domains.iterator();i.hasNext();){
//                WiKIDDomain wd = (WiKIDDomain) i.next();
//                if(domainsByName.containsKey(wd.getName())){
//                    setPopulatedWithDuplicateNames(true);
//                    break;
//                }
//                domainsByName.put(wd.getName(), wd);
//                domainsByCode.put(wd.getServerCode(), wd);
//            }
//        }
    }

    public void deleteDomain(WiKIDDomain domain){
        getDomains().remove(domain);
    }

    public void deleteDomain(String servercode){
        WiKIDDomain domain = getDomainByCode(servercode);
        getDomains().remove(domain);
    }

    public void deleteAllDomains(){
        domains.clear();
        domainsByCode.clear();
        domainsByName.clear();
    }


    public void save(String configFile, String passphrase) {
        save(configFile,passphrase.toCharArray());
    }

    public void save(String configFile, char[] passphrase) {
        save(this,new File(WiKIDToken.toFSPath(configFile)),passphrase);
    }

    public void save() {
        save(this, getConfigFile(), getConfigFilePassphrase());
    }


    public static void save(TokenConfiguration config, File configFile, char[] passphrase) {
        try {
            Document doc = new Document();
//            doc.setDocType(new DocType("tokenConfig").setSystemID("http://wva1.wikidsystems.com/dtd/WiKIDToken.dtd"));
            Element rootElement = new Element("tokenConfig");
            doc.setRootElement(rootElement);
            rootElement.setAttribute("configVersion", config.getConfigVersion() + "");
            rootElement.addContent((new Element("deviceID")).setText(config.getDeviceID() + ""));
            rootElement.addContent((new Element("publicKey")).setText(
                    new String(B64.encodeBytes(config.getKeys().exportPubKey()))));
            rootElement.addContent((new Element("privateKey")).setText(
                    new String(B64.encodeBytes(config.getKeys().exportPrivKey()))));
            Element proxyElement = (new Element("proxy"));
            proxyElement.setAttribute("enabled",config.isProxyEnabled()+"");
            proxyElement.setAttribute("host",config.getProxyHost()==null?"":config.getProxyHost());
            proxyElement.setAttribute("port",config.getProxyPort()==null?"":config.getProxyPort());
            rootElement.addContent(proxyElement);
            rootElement.addContent(new Element("domainSet").addContent(markupDomains(config)));
            XMLOutputter outputter = new XMLOutputter();

            String plainConfig = outputter.outputString(doc);
            byte[] cipherConfig = AESEncrypt(plainConfig.getBytes(), passphrase);

            FileOutputStream fos = new FileOutputStream(configFile);
            fos.write(cipherConfig);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Collection markupDomains(TokenConfiguration config) {
        List xmlDoms = new ArrayList();
        if (config == null) return xmlDoms;
        for (Iterator i = config.getDomains().iterator(); i.hasNext();) {
            WiKIDDomain wd = (WiKIDDomain) i.next();
            Element xmlDom = new Element("domain");
            xmlDom.setAttribute("serverCode", wd.getServerCode())
                    .setAttribute("minPIN", wd.getMinPIN() + "")
                    .setAttribute("PINLifetime", wd.getPINLifetime() + "")
                    .setAttribute("name", wd.getName());
            xmlDom.addContent(new Element("deviceID").setText(wd.getDeviceID() + ""));
            xmlDom.addContent(new Element("offlineKey").setText(new String(B64.encodeBytes(wd.getOfflineKey()))));
            xmlDom.addContent(new Element("registeredURL").setText(wd.getRegisteredURL()));
            xmlDom.addContent(new Element("pubKey").setText(new String(B64.encodeBytes(wd.getPubKey().getEncoded()))));
            xmlDoms.add(xmlDom);
        }
        return xmlDoms;
    }

    public TokenConfiguration parseConfigDoc(Document doc) {
        TokenConfiguration tc = new TokenConfiguration();
        Element rootElement = doc.getRootElement();
        try {
            int configVersion = rootElement.getAttribute("configVersion").getIntValue();
            tc.setConfigVersion(configVersion);
            tc.setDeviceID(Long.parseLong(rootElement.getChildText("deviceID")));
            tc.setKeys(convertKeys(rootElement.getChildText("publicKey"), rootElement.getChildText("privateKey")));
            Element proxyElement = rootElement.getChild("proxy");
            if (proxyElement!=null){
                tc.setProxyEnabled(Boolean.valueOf(proxyElement.getAttributeValue("enabled")).booleanValue());
                tc.setProxyHost(proxyElement.getAttributeValue("host"));
                tc.setProxyPort(proxyElement.getAttributeValue("port"));
            }
            tc.setDomains(loadDomains(rootElement.getChild("domainSet").getChildren()));
        } catch (DataConversionException e) {
            e.printStackTrace();
        }
        return tc;
    }


    private static wEncKeys convertKeys(String pubKey, String privKey) {
        try {
            return wkeyfactory.create(B64.decode(removeFormatting(pubKey)), B64.decode(removeFormatting(privKey)));
        } catch (wCryptoException e) {
            throw new RuntimeException(e);
        }
    }

    private List loadDomains(List doms) {
        List l = new LinkedList();
        for (Iterator i = doms.iterator(); i.hasNext();) {
            Element e = (Element) i.next();
            WiKIDDomain wd = new WiKIDDomain();
            try {
                wd.setServerCode(e.getAttribute("serverCode").getValue());
                wd.setMinPIN(e.getAttribute("minPIN").getIntValue());
                wd.setPINLifetime(e.getAttribute("PINLifetime").getIntValue());
                wd.setName(e.getAttribute("name").getValue());
                wd.setDeviceID(Long.parseLong(e.getChildText("deviceID")));
                wd.setOfflineKey(B64.decode(removeFormatting(e.getChildText("offlineKey"))));
                wd.setRegisteredURL(e.getChildText("registeredURL"));
                wd.setPubKey(wkeyfactory.create(B64.decode(removeFormatting(e.getChildText("pubKey"))), null).getPublicKey());
            } catch (wCryptoException e1) {
                e1.printStackTrace();
            } catch (DataConversionException e1) {
                e1.printStackTrace();
            }
            l.add(wd);
        }
        return l;
    }

    private static String removeFormatting(String base64EncString) {
        return base64EncString.replaceAll(" ", "").replaceAll("\n", "");
    }

    public TokenConfiguration load(File tokenConfigFile, char[] passPhrase) throws IOException, JDOMException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException {
        configFile = tokenConfigFile;
        configFilePassphrase = passPhrase;
        if (tokenConfigFile !=null && tokenConfigFile.length() != 0) {
            SAXBuilder sax = new SAXBuilder();
            Document xmlConfigDoc = null;

            byte[] encFileContents = new byte[(int) tokenConfigFile.length()];
            FileInputStream fis = new FileInputStream(tokenConfigFile);
            if (fis.read(encFileContents) > 0) {
                byte[] plainFileContents = AESDecrypt(encFileContents, passPhrase);
                xmlConfigDoc = sax.build(new ByteArrayInputStream(plainFileContents));
            }
            fis.close();
//            xmlConfigDoc= sax.build(tokenConfigFile);         // this reads an unencrypted file
            WiKIDToken.setNewConfig(false);
            return parseConfigDoc(xmlConfigDoc);
        } else {
            return buildNewConfigDoc();
        }
    }

    private TokenConfiguration buildNewConfigDoc() {
        try {
            TokenConfiguration newTC = new TokenConfiguration();
            newTC.setConfigVersion(TokenConfiguration.TOKEN_CONFIGURATION_PARSER_VERSION);
            newTC.setDeviceID(SecureRandom.getInstance("SHA1PRNG").nextLong());
            newTC.setKeys(wkeyfactory.generatePair());
            return newTC;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (wCryptoException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] AESDecrypt(byte[] ciphertext, char[] seed) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException {
        Key rk;
        Cipher decrypt;
        byte[] plain = null;
        MessageDigest md = MessageDigest.getInstance("MD5");
        for (int i = 0; i < seed.length; i++) {
            md.update((byte) seed[i]);
        }
        rk = new SecretKeySpec(md.digest(), "AES");
        decrypt = Cipher.getInstance("AES");
        decrypt.init(Cipher.DECRYPT_MODE, rk);
        try {
            plain = decrypt.doFinal(ciphertext);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e.getMessage());
        }
        return plain;
    }

    public static byte[] AESEncrypt(byte[] plaintext, char[] seed) {
        Key rk;
        Cipher crypt;
        byte[] ciphertext = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            for (int i = 0; i < seed.length; i++) {
                md.update((byte) seed[i]);
            }
            rk = new SecretKeySpec(md.digest(), "AES");
            crypt = Cipher.getInstance("AES");
            crypt.init(Cipher.ENCRYPT_MODE, rk);

            ciphertext = crypt.doFinal(plaintext);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("AES Encrypt blew up");
        }
        return ciphertext;
    }


    public static boolean checkFilePerms(File configFile) {
        if (configFile == null) return false;
        return ((configFile.exists() && configFile.canRead()) || !configFile.exists());
    }

    public WiKIDDomain getDomain(String name) {
        return (WiKIDDomain) domainsByName.get(name);
    }


    public WiKIDDomain getDomainByCode(String serverCode) {
        return (WiKIDDomain) domainsByCode.get(serverCode);
    }

    public boolean isPopulatedWithDuplicateNames() {
        return populatedWithDuplicateNames;
    }

    private void setPopulatedWithDuplicateNames(boolean populatedWithDuplicateNames) {
        this.populatedWithDuplicateNames = populatedWithDuplicateNames;
    }

    public boolean isProxyEnabled() {
        return proxyEnabled;
    }

    public void setProxyEnabled(boolean proxyEnabled) {
        this.proxyEnabled = proxyEnabled;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }
}
