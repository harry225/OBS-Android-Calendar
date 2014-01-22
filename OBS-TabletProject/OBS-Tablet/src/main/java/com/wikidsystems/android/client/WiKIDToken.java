package com.wikidsystems.android.client;


import com.wikidsystems.android.crypto.wCryptoException;

import org.jdom.JDOMException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by Eric Shoemaker
 * Date: Dec 25, 2005
 * Time: 5:43:52 PM
 */
public class WiKIDToken {

    private static File tokenConfigFile;
    private TokenConfiguration tokenConfig;
    private char[] passphrase;
    private static String msg;
    private boolean open = false;
    private static boolean newConfig = true;

    /*
        Static method for geting a token that maintains persistence
        for the life of the JVM unless explicitly killed.
    */

    public WiKIDToken(){
    }

    public static boolean hasConfig(){
        return tokenConfigFile.exists();
    }
    public static boolean hasConfig(String configFile){
        if (configFile.startsWith("file:")) {
            try {
                configFile = new URI(configFile).getPath();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return new File(configFile).exists();
    }

    public boolean isOpen() {
        return open;
    }

    public static boolean isNewConfig() {
        return newConfig;
    }

    public static void setNewConfig(boolean newConfig) {
        WiKIDToken.newConfig = newConfig;
    }

    public TokenConfiguration load(String configFile, String passphrase)  {
        if (configFile == null || passphrase== null)
            throw new RuntimeException("You must provide both a configuration file and passphrase to load a token!");
        tokenConfigFile = new File(toFSPath(configFile));
        this.passphrase = passphrase.toCharArray();
        try {
            tokenConfig = new TokenConfiguration().load(tokenConfigFile, this.passphrase);
            open=true;
        } catch (IOException e) {
            open=false;
            throw new RuntimeException(e);
        } catch (JDOMException e) {
            open=false;
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            open=false;
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            open=false;
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            open=false;
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            open=false;
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            open=false;
            throw new RuntimeException(e);
        }
        return tokenConfig;
    }

    public static WiKIDToken getToken(String passPhrase, String configFileLocation) throws Exception{
        return getToken(passPhrase.toCharArray(), toFSPath(configFileLocation));
    }

    public static WiKIDToken getToken(char[] passPhrase, String configFileLocation)  throws Exception {
        return new WiKIDToken(passPhrase, configFileLocation);
    }

    private WiKIDToken(char[] passPhrase, String configFilePath) throws IOException, JDOMException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException {
        this.passphrase = passPhrase;
        tokenConfigFile = new File(configFilePath);
        // Check for the ability to read or create configuration file
        if (TokenConfiguration.checkFilePerms(tokenConfigFile)) {
            tokenConfig = (new TokenConfiguration()).load(tokenConfigFile, this.passphrase);  // load the config file
            open = true;
        } else {
            open=false;
            System.out.println("Cannot open or read configuration file.");
        }
    }

    public static void main(String[] args) {
        WiKIDToken tok;
        WiKIDDomain thisDomain;
        String domainCode, PIN, regcode, passcode;
        try {
            if (args.length > 0) {
                // If there are multiple parameters, the first one should be the config file, otherwise,
                // get a token with the default config file (./WiKIDToken.wkd).
                if (args.length > 1) {
                    tok = WiKIDToken.getToken(args[1].toCharArray(), args[0]);
                } else {
                    //if (new File(args[0]).exists()) {
                        //tok = WiKIDToken.getToken(promptPassPhrase(), args[0]);
                    //} else {
                        tok = WiKIDToken.getToken("passphrase".toCharArray(), args[0]);
                    //}
                }
            } else {
                //if (new File("WiKIDToken.wkd").exists())
                    //tok = WiKIDToken.getToken(promptPassPhrase(), null);
                //else
                    tok = WiKIDToken.getToken("".toCharArray(), null);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter Domain Code (12 digits):");
            domainCode = br.readLine();
            if(domainCode.length()!=12) throw new RuntimeException("Domain codes must be exactly 12 digits.");
            thisDomain= tok.getDomainConfig(domainCode);
            System.out.println("Enter Pin for " + thisDomain.getName() + " domain ("+thisDomain.getMinPIN()+" digits): ");
            PIN = br.readLine();
            if(PIN.length() < thisDomain.getMinPIN()) throw new RuntimeException("PIN for this domain must be at least "+thisDomain.getMinPIN()+" digits.");
            regcode = tok.setDomainPIN(thisDomain,PIN);
            if (regcode != null && regcode.length() > 0){
                System.out.println("Registered token.  Registration code: " + regcode);
            } else {
                throw new RuntimeException("Did not receive a valid registration code.");
            }
            System.out.println("Requesting passcode...");
            passcode = tok.getPasscode(thisDomain, PIN);
            System.out.println("Received passcode: "+ passcode + "\nProcess complete.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    private static char[] promptPassPhrase() {
//        try {
//            return PasswordField.getPassword(System.in, "PassPhrase: ");
//        } catch (IOException e) {
//            new RuntimeException(e);
//        }
//        return null;
//    }

    public TokenConfiguration getTokenConfig() {
        return tokenConfig;
    }

    public WiKIDDomain getDomainConfig(String serverCode) {
        try {
            return wComms.pullConfig(serverCode, getTokenConfig());
        } catch (WikidException e) {
            System.err.println(e.getMessage());
//            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            //e.printStackTrace();
        } catch (wCryptoException e) {
            System.err.println(e.getMessage());
            //e.printStackTrace();
        }
        return null;
    }

    public String setDomainPIN(WiKIDDomain domain, String pin) {
        return setDomainPIN(domain,pin.toCharArray());
    }

    public String setDomainPIN(WiKIDDomain domain, char[] pin) {
        try {
            String regCode = wComms.setPIN(domain, pin, getTokenConfig());
            if (regCode.length()>0){
                getTokenConfig().addDomain(domain);
                TokenConfiguration.save(tokenConfig,tokenConfigFile,passphrase);   // write the config file
            }
            return regCode;
        } catch (WikidException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPasscode(WiKIDDomain domain, String pin) {
        return getPasscode(domain,pin.toCharArray());
    }

    public String getPasscode(WiKIDDomain domain, char[] pin) {
        try {
            String passcode;
            passcode = wComms.proc(pin,getTokenConfig(),domain);
            WiKIDToken.setMsg(passcode);
            return passcode;
        } catch (WikidException e) {
            WiKIDToken.setMsg(e.getMessage());
            System.err.println(e.getMessage());
            //e.printStackTrace();
        } catch (Exception e) {
            WiKIDToken.setMsg(e.getMessage());
            System.err.println(e.getMessage());
            //e.printStackTrace();
        }

        return null;
    }


    // These methods are for debugging the Firefox extension
    public static void setMsg(String msg) {
        WiKIDToken.msg = WiKIDToken.msg + "\n"+ msg;
    }

    public static String getMsg() {
        return msg;
    }

    public static String 
            toFSPath(String filePathOrUri){
        if (filePathOrUri.startsWith("file:")) {
            try {
                filePathOrUri = new URI(filePathOrUri).getPath();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return filePathOrUri;
    }
    
    public void save(){
        TokenConfiguration.save(tokenConfig,tokenConfigFile,passphrase);
    }
}

