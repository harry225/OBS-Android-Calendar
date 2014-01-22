/* @COPYRIGHT@ */
/* @LICENSE@ */

/*
 * @(#)wClient.java	1.3 01/24/04
 *
 * Copyright 2001-2004 WiKID Systems, Inc. All rights reserved.
 *
 */
package com.wikidsystems.android.client;

import com.thoughtworks.xstream.XStream;
import com.wikidsystems.android.data.User;
import com.wikidsystems.android.data.WiKIDEvent;
import com.wikidsystems.android.data.WiKIDEventListener;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.SocketException;
import java.security.KeyStore;
import java.util.Hashtable;
import java.util.Properties;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;


/**
 * <p> This is the core SSL client for WiKID Authentication.  <code>wClient</code> manages communication between Network Clients (NC)
 *  and the WiKID Authentication Server.
 *
 *  <p> Due to the overhead of SSL and the RSA key generation and negotiation, each wClient object maintains its own persistent
 *  SSL connection to the WAS.  This allows the application to create one <code>wClient</code> object for each WAS server and maintain
 *  this object over whatever lifetime is appropriate.
 *
 *  <p> All method transactions are atomic.
 */
public class wClient
{
    private SSLSocketFactory factory = null;
    private SSLSocket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String host, keyfile, pass;
    private String caStore = null;
    private String caStorePass = "changeit";
    private int port;
    private int CBport=8385;

    private callbackListener callbackListnr;
    private Thread CBThread;
    
    boolean gotConnection = false;
    boolean gotCBConnection = false;

    private static final String WCLIENT_VERSION = "1.2";

    /**
     * <p> This constructor allows the <code>wClient</code> module to be initialized from a properties file. The required
     * properties keys are:
     *
     * <ul>
     * <li> host - The IP address or hostname of the WAS server
     * <li> port - The SSL listener port for the wAuth daemon on the WAS server
     * <li> keyfile - The PKCS12 keystore generated for this client by the WAS server
     * <li> pass - The passphrase securing the keys in keyfile
     * <li> caStore - The certificate authority store for validating the WAS server certificate
     * <li> caStorePass - The passphrase securing the caStore file
     * </ul>
     *
     * <p>
     *
     * @param propertiesFile path to properties file
     */
    public wClient(String propertiesFile)
    {
//        log.debug("wClient(propertiesFile) called ...");
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(propertiesFile));
            this.host = (String) props.get("host");
            this.port = Integer.parseInt( (String) props.get("port"));
            this.keyfile = (String) props.get("keyfile");
            this.pass = (String) props.get("pass");
            this.caStore = (String) props.get("caStore");
            this.caStorePass = (String) props.get("caStorePass");
        }
        catch (FileNotFoundException fnfe) {
            System.err.println("FATAL: Could not load properties file for wClient initialization");
//            log.error("FATAL: Could not load properties file for wClient initialization");
            fnfe.printStackTrace();
        }
        catch (IOException ioe) {
            System.err.println("FATAL: IO error while loading properties file for wClient initialization");
//            log.error("FATAL: IO error while loading properties file for wClient initialization");
            ioe.printStackTrace();
        }
        init();
    }

    /**
     * <p> This constructor allows the <code>wClient</code> module to be initialized with passed values.  This is useful
     * in environments where saving passphrases in a properties file is not permitted.
     *
     * @param host String The IP address or hostname of the WAS server
     * @param port int The SSL listener port for the wAuth daemon on the WAS server
     * @param keyfile String The PKCS12 keystore generated for this client by the WAS server
     * @param pass String The passphrase securing the keys in keyfile
     * @param caStore String The certificate authority store for validating the WAS server certificate
     * @param caStorePass String The passphrase securing the caStore file
     */
    public wClient(String host, int port, String keyfile, String pass, String caStore, String caStorePass)
    {
//        log.debug("wClient(" + host + ", " + port + ", " + keyfile + ", " + pass + ", " + caStore + ", " + caStorePass + ") called ...");
        this.host = host;
        this.port = port;
        this.keyfile = keyfile;
        this.pass = pass;
        this.caStore = caStore;
        this.caStorePass = caStorePass;
        init();
    }

    /**
     * <p> This constructor allows the <code>wClient</code> module to be initialized with passed values.  This is useful
     * when creating a <code>wClient</code> connction from the WAS.  This constructor uses default values for the CA trust
     * store that mathe the WAS server defaults.
     *
     * @param host String String The IP address or hostname of the WAS server
     * @param port int The SSL listener port for the wAuth daemon on the WAS server
     * @param keyfile String The PKCS12 keystore generated for this client by the WAS server
     * @param pass String The passphrase securing the keys in keyfile
     */
    public wClient(String host, int port, String keyfile, String pass)
    {
//        log.debug("wClient(" + host + ", " + port + ", " + keyfile + ", " + pass + ") called ...");
        this.host = host;
        this.port = port;
        this.keyfile = keyfile;
        this.pass = pass;
        init();
    }

    /**
     * <p> This method closes the <code>wClient</code> connection to the WiKID server and exits.
     */
    public void close()
    {
//        log.debug("close() called ...");
        try {
            this.gotConnection = false;
            if (socket != null)
                socket.close();
        }
        catch (IOException ex) {
        }
    }

    private void init()
    {
//        log.debug("init() called ...");
        try {
            // Set up a key manager for client authentication
            //Security.addProvider(new BouncyCastleProvider());
//            if (log.isDebugEnabled())
//            {
////                System.setProperty("javax.net.debug", "ssl:handshake");
//            }

            System.setProperty("javax.net.ssl.trustStore", caStore);
            System.setProperty("javax.net.ssl.trustStorePassword", caStorePass);
//			try {

            SSLContext ctx;
            KeyManagerFactory kmf;
            KeyStore ks;
            char[] passphrase = pass.toCharArray();
            ctx = SSLContext.getInstance("TLS");
            kmf = KeyManagerFactory.getInstance("SunX509");
            ks = KeyStore.getInstance("PKCS12");
//            log.debug("creating keyFile object: " + keyfile);
            File keyFile = new File(keyfile);
//            log.debug("keyFile object created ...");
            if (keyFile.exists()) {
//                log.debug("Key File exists: " + keyFile.toString());
//                log.debug("Last Modified: " + new java.util.Date(keyFile.lastModified()).toString());
            }
            else
            {
//                log.debug("Key File  '" + keyFile.toString() + "' does not exist!");
            }
            if (keyFile.canRead()) {
//                log.debug("Key File is OK: " + keyFile.toString());
                ks.load(new FileInputStream(keyFile), passphrase);
//                log.debug("keyFile loaded ...");
                kmf.init(ks, passphrase);
//                log.debug("keymanager initialized ...");
                ctx.init(kmf.getKeyManagers(), null, null);
//                log.debug("context initialized ...");
                factory = ctx.getSocketFactory();
//                log.debug("socket factory created ...");
            }
            else {
                System.out.println("Configuration ERROR: Cannot open file " + keyFile.toString());
//                log.error("Configuration ERROR: Cannot read file " + keyFile.toString());
//                log.debug("User: " + System.getProperty("user.name"));
                throw new FileNotFoundException();
            }
//			} catch (FileNotFoundException fnfe){


//			} catch (Exception e) {
//				throw new IOException(e.getMessage());
//			}
            socket = (SSLSocket) factory.createSocket(host, port);
            socket.setKeepAlive(true);
//            log.debug("socket created ...");
            socket.startHandshake();
//            log.debug("handshake started ...");
//            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            out= new ObjectOutputStream(socket.getOutputStream());
//            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            in = new ObjectInputStream(socket.getInputStream());
            this.gotConnection = startConnection();

        }
        catch (FileNotFoundException fnfe) {
//            log.error("ERROR: file not found: " + fnfe.toString());
            fnfe.printStackTrace();
        }
        catch (Exception e) {
//            log.error("ERROR: " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * <p> Returns whether this wClient object is connected to a WAS
     *
     * @return boolean true indicates a connection has been established
     */
    public boolean isConnected()
    {
        return this.gotConnection;
    }

    private void reconnect()
    {
//        log.debug("reconnect() called.");

        try {
//            if (log.isDebugEnabled())
//            {
//                log.debug("this.gotConnection: "+ this.gotConnection);
//                log.debug("socket.isClosed(): "+ socket.isClosed());
//                log.debug("socket.isInputShutdown(): "+ socket.isInputShutdown());
//                log.debug("socket.isOutputShutdown(): "+ socket.isOutputShutdown());
//            }
            if (!this.gotConnection || socket.isClosed() || socket.isInputShutdown() || socket.isOutputShutdown()) {
                reEstablishConnection();
            }
            try{
                socket.getInputStream().available();
            } catch(Exception sslException){
                reEstablishConnection();
            }
            try{
                out.writeUTF("TX");
                out.flush();
                String reply = in.readUTF();
                if (!reply.equals("ACK")){
//                    log.error("Got invalid preamble reply!");
                    reEstablishConnection();
                    out.writeUTF("TX");
                    out.flush();
                }
            }catch (SocketException e) {
                System.err.println("Caught Socket Exception! Reconnecting...");
                reEstablishConnection();
                out.writeUTF("TX");
                out.flush();
                String reply = in.readUTF();
                if (!reply.equals("ACK")){
//                    log.error("Got invalid preamble reply!");
                    reEstablishConnection();
                }                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reEstablishConnection(){
        try {
//        log.debug("Socket dead. Reconnecting...");
        socket = (SSLSocket) factory.createSocket(host, port);
        socket.startHandshake();
        socket.setKeepAlive(true);
        out= new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        this.gotConnection = startConnection();
        }
        catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <p> This method verifies credentials generated using the online mechanism.
     *
     * @param user String users login ID in this authentication domain
     * @param passcode String passcode provided by the user
     * @param servercode String 12 digit code representing this authentication domain
     * @return boolean true indicates credentials were valid, false if credentials were invalid or an error occured
     */
    public boolean CheckCredentials(String user, String passcode, String servercode)
    {
        //log.debug("CheckCredentials(" + user + ", " + passcode + ", " + servercode + ") called ...");
        reconnect();
        boolean validCredentials = false;
        String inputLine;
        //log.debug("Checking Credentials");
        try {
            synchronized (out) {
                //log.debug("Writing out...");

                out.writeUTF("VERIFY:" + user + "\t" + passcode + "\t" + servercode);
                out.flush();
                if ( (inputLine = in.readUTF()) != null) {
                    //log.debug("Reading in...");
                    if (inputLine.startsWith("VERIFY:VALID")) {
                        validCredentials = true;
                    }
                    else {
                    }
                    //log.debug("Read response.");
                }
                else {
                    this.gotConnection=false;
                    //log.error("Error reading from server");
                }
            }
        }
        catch (IOException ioe) {
            //log.error("IO error talking to server: " + ioe.getMessage(), ioe);
        }
        //log.debug("Returning Results...");

        return validCredentials;
    }

    public boolean testNoPreamble()
    {
//        reconnect();
        boolean validCredentials = false;
        String inputLine;
        //log.debug("Testing transaction without preamble");

        try {
            out.writeUTF("INVALIDTX:");
            out.flush();
            if ( (inputLine = in.readUTF()) != null) {
                if (inputLine.startsWith("VERIFY:VALID")) {
                    validCredentials = true;
                }                
            }
            else {
                this.gotConnection=false;
                System.out.println("Error reading from server");
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return validCredentials;
    }


    /* */
    public boolean chapVerify(String user, String servercode, byte[] chapPassword, byte[] chapChallenge)
    {
        reconnect();
        boolean validCredentials = false;
        String inputLine;
        //log.debug("Checking Chap Credentials");

        try {
            out.writeUTF("CHAPVERIFY:" + user + "\t" + "null" + "\t" + servercode);
            out.flush();
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeInt(chapPassword.length);
            dos.write(chapPassword);
            dos.writeInt(chapChallenge.length);
            dos.write(chapChallenge);
            dos.flush();
            if ( (inputLine = in.readUTF()) != null) {
                if (inputLine.startsWith("VERIFY:VALID")) {
                    validCredentials = true;
                }
                else {
                }
            }
            else {
                this.gotConnection=false;
                //log.error("Error reading from server");
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return validCredentials;
    }

    public boolean chapVerify(String user, String servercode, String wikidChallenge, byte[] chapPassword, byte[] chapChallenge)
    {
        reconnect();
        boolean validCredentials = false;
        String inputLine;
        //log.debug("Checking Chap Credentials");

        try {
            out.writeUTF("CHAPOFFVERIFY:" + user + "\t" + "null" + "\t" + servercode + "\t" + wikidChallenge);
            out.flush();
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeInt(chapPassword.length);
            dos.write(chapPassword);
            dos.writeInt(chapChallenge.length);
            dos.write(chapChallenge);
            dos.flush();
            if ( (inputLine = in.readUTF()) != null) {
                if (inputLine.startsWith("VERIFY:VALID")) {
                    validCredentials = true;
                }
                else {
                }
            }
            else {
                this.gotConnection=false;
                //log.error("Error reading from server");
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return validCredentials;
    }

    /**
     * <p>  Retuns a list of domains on the WAS.
     *
     * @return Hashtable list of domains on the WAS
     */
    public Hashtable getDomains()
    {
        reconnect();
        String inputLine;
        //log.debug("Getting Domains");

        try {
            synchronized (out) {
                //log.debug("Writing out...");
                XStream xs = new XStream();
                out.writeUTF("DOMAINS:");
                out.flush();
                if ( (inputLine = in.readUTF()) != null) {
                    //log.debug("Reading in...");

                    if (inputLine.startsWith("DOMAINLIST")) {
                        return (Hashtable) xs.fromXML(in.readUTF());
                    }
                    else {
                        return null;
                    }
                }
                else {
                    this.gotConnection=false;
                    //log.error("Error reading from server");
                }
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        /*catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }*/
        //log.debug("Returning Results...");
        return null;
    }

    /**
     * <p> This method verifies credentials generated using the challenge-response mechanism.
     *
     * @param user String users login ID in this authentication domain
     * @param challenge String challenge value provided to user (numeric)
     * @param response String response value entered by user
     * @param servercode String 12 digit code representing this authentication domain
     * @return boolean true indicates credentials were valid, false if credentials were invalid or an error occured
     */
    public boolean CheckCredentials(String user, String challenge, String response, String servercode)
    {
        reconnect();
        boolean validCredentials = false;
        String inputLine;
        //log.debug("CheckCredentials");
        try {
            out.writeUTF("OFFLINEVERIFY:" + user + "\t" + challenge + "\t" + response + "\t" + servercode);
            out.flush();
            if ( (inputLine = in.readUTF()) != null) {
                if (inputLine.startsWith("VERIFY:VALID")) {
                    validCredentials = true;
                }
            }
            else {
                this.gotConnection=false;
                //log.error("Error reading from server");
            }
        }
        catch (IOException ioe) {
            //log.error("IO error talking to server: " + ioe.getMessage(), ioe);
        }
        return validCredentials;
    }

    /**
     * <p> This method creates an association between the userid and the device registered by the user.
     *
     * @param uname String users login ID in this authentication domain
     * @param regcode String registration code provided to user when setting up this domain on users device
     * @param servercode String 12 digit code representing this authentication domain
     * @return int result code from the registration attempt
     */
    public int registerUsername(String uname, String regcode, String servercode)
    {
        reconnect();
        String inputLine;
        //log.debug("registerUsername");
        synchronized (out) {
            try {
                out.writeUTF("REGUSER:" + uname + "\t" + regcode + "\t" + servercode);
                out.flush();
                if ( (inputLine = in.readUTF()) != null) {
                    if (inputLine.startsWith("REGUSER:SUCESS")) {
                        return 0;
                    }
                    else {
                        inputLine = inputLine.trim();
                        //log.warn("received error code: " + inputLine);
                        return Integer.parseInt( (inputLine.split(":"))[2].trim());
                    }
                }
                else {
                    this.gotConnection=false;
                    //log.error("Error reading from server");
                    return 2;
                }
            }
            catch (IOException ioe) {
                //log.error("IO error talking to server: " + ioe.getMessage(), ioe);
                return 2;
            }
        }
    }

    /**
     * <p> This method creates an association between the userid and the device registered by the user. A
     * passcode generated on a device currently registered to this uname must be provided to  validate
     * the registration.
     *
     * @param uname String users login ID in this authentication domain
     * @param regcode String registration code provided to user when setting up this domain on users device
     * @param servercode String 12 digit code representing this authentication domain
     * @param passcode A passcode generated by a device currently registered the userid
     * @return int result code from the registration attempt
     */
    public int registerUsername(String uname, String regcode, String servercode, String passcode)
    {
        reconnect();
        String inputLine;
        //log.debug("registerUsername");

        synchronized (out) {
            try {
                out.writeUTF("ADDREGUSER:" + uname + "\t" + regcode + "\t" + servercode+ "\t" + passcode);
                out.flush();
                if ( (inputLine = in.readUTF()) != null) {
                    if (inputLine.startsWith("REGUSER:SUCESS")) {
                        return 0;
                    }
                    else {
                        inputLine = inputLine.trim();
                        return Integer.parseInt( (inputLine.split(":"))[2].trim());
                    }
                }
                else {
                    this.gotConnection=false;
                    //log.error("Error reading from server");
                    return 2;
                }
            }
            catch (IOException ioe) {
                //log.error("IO error talking to server: " + ioe.getMessage(), ioe);
                return 2;
            }
        }
    }

    /**
     * <p> This method creates an association between the userid and the device registered by the user.  The
     * server must have the ALLOW_REG_WITHOUT_PASSCODE parameter devfined and set to True for this call to
     * succeed.
     *
     * @param uname String users login ID in this authentication domain
     * @param regcode String registration code provided to user when setting up this domain on users device
     * @param servercode String 12 digit code representing this authentication domain
     * @return int result code from the registration attempt
     */
    public int registerUsernameWithoutCheck(String uname, String regcode, String servercode)
    {
        reconnect();
        String inputLine;
        //log.debug("Regisering device w/o check");

        synchronized (out) {
            try {
                out.writeUTF("NOCHECKADDREGUSER:" + uname + "\t" + regcode + "\t" + servercode);
                out.flush();
                if ( (inputLine = in.readUTF()) != null) {
                    if (inputLine.startsWith("REGUSER:SUCESS")) {
                        return 0;
                    }
                    else {
                        inputLine=inputLine.trim();
                        return Integer.parseInt( (inputLine.split(":"))[2].trim());
                    }
                }
                else {
                    this.gotConnection=false;
                    //log.error("Error reading from server");
                    return 2;
                }
            }
            catch (IOException ioe) {
                //log.error("IO error talking to server: " + ioe.getMessage(), ioe);
                return 2;
            }
        }
    }


//TODO:  javadoc
    /**
     * <p> Registers a callback interface to the WAS for notification of events.
     *
     * @param eventCallback Object that implements the WiKIDEventListener interface.
     *
     */
    public void registerCallback(WiKIDEventListener eventCallback) throws IOException {
            SSLSocket CBsocket = (SSLSocket) factory.createSocket(host, CBport);
            //log.debug("Callback socket created ...");
            CBsocket.startHandshake();
            CBsocket.setKeepAlive(true);
            //log.debug("CallBack handshake started ...");
            ObjectOutputStream CBout= new ObjectOutputStream(CBsocket.getOutputStream());
            ObjectInputStream CBin = new ObjectInputStream(CBsocket.getInputStream());
            this.gotCBConnection=startCBConnection(CBout,CBin);
            String inputLine;
            synchronized (CBout) {
                //log.debug("Register callback");
                CBout.writeUTF("REGCALLBACK:");
                CBout.flush();
                if ( (inputLine = CBin.readUTF()) != null) {
                    //log.debug("Reading register callback result...");
                    if (inputLine.startsWith("REGCALLBACK:FAILED")) {
                        //log.debug("Register callback Failed");
                        throw new RuntimeException("Register callback Failed");
                    }  else {
                        callbackListnr =new callbackListener(CBin,CBout, eventCallback,this);
                        CBThread = new Thread(callbackListnr);
                        CBThread.start();
                    }
                }
                else {
                    this.gotConnection=false;
                    //log.error("Error reading from server");
                }
            }
    }

    public void reconnectCB(WiKIDEventListener eventCB) {
        if(callbackListnr.getEventCB()==eventCB) callbackListnr.setRunning(false);
        boolean reconnected = false;
        while (!reconnected){
            try {
                //log.warn("Reconnecting CallBack...");
                registerCallback(eventCB);
                reconnected=true;
            } catch (ConnectException ce){
                reconnected = false;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();  
            }
        }
    }

    /**
     * <p>  Deletes a user from the WAS.  Use <code>finduser</code> to obtain a reference
     * to the user to be deleted on the WAS.
     *
     * @param deletedUser User to be deleted.
     * @return int result code from the deletion attempt
     */
    public int deleteUser(User deletedUser){
        reconnect();
        String inputLine;
        synchronized (out) {
            try {
                //log.debug("Deleting User");
                XStream xs = new XStream();
                out.writeUTF("DELETEUSER:");
                out.flush();
                out.writeUTF(xs.toXML(deletedUser));
                out.flush();
                if ( (inputLine = in.readUTF()) != null) {
                    //log.debug("Reading delete result...");

                    if (inputLine.startsWith("DELETEUSER:FAILED")) {
                        //log.debug("Deleting User Failed");
                    }
                    return in.readInt();
                }
                else {
                    this.gotConnection=false;
                    //log.error("Error reading from server");
                }
            }
            catch (IOException ioe) {
                //log.error("IO error talking to server: " + ioe.getMessage(), ioe);
                return -1;
            }
        }
        return -2;
    }


    /**
     * <p>  Makes changes to a user on the WAS.  Use <code>finduser</code> to obtain a reference
     * to the user to be deleted on the WAS.
     *
     * @param updatedUser User to be modified.
     * @return int result code from the update attempt
     */
    public int updateUser(User updatedUser){
        reconnect();
        String inputLine;
        synchronized (out) {
            try {
                //log.debug("Updating User");
                XStream xs = new XStream();
                out.writeUTF("UPDATEUSER:");
                out.flush();
                out.writeUTF(xs.toXML(updatedUser));
                out.flush();
                if ( (inputLine = in.readUTF()) != null) {
                    //log.debug("Reading update result...");

                    if (inputLine.startsWith("UPDATEUSER:FAILED")) {
                        //log.debug("Updating User Failed");
                    }
                    return in.readInt();
                }
                else {
                    this.gotConnection=false;
                    //log.error("Error reading from server");
                }
            }
            catch (IOException ioe) {
                //log.error("IO error talking to server: " + ioe.getMessage(), ioe);
                return -1;
            }
        }
        return -2;
    }


    /**
     * <p> Re a reference
     * to the user to be deleted on the WAS.
     *
     * @param servercode domaincode to locate this user
     * @param userID User to be located
     * @return User object holding user data from the WAS
     */
    public User findUser(String servercode, String userID){
        reconnect();
        String inputLine;
        //log.debug("Finding User");
        User user = null;
        synchronized (out) {
            try {
                //log.debug("Finding user...");
                out.writeUTF("FINDUSERBYNAME:");
                out.flush();
                out.writeUTF(servercode);
                out.writeUTF(userID);
                out.flush();
                if ( (inputLine = in.readUTF()) != null) {
                    //log.debug("Reading user...");

                    if (inputLine.startsWith("USEROBJECT")) {
                          XStream xs = new XStream();
                          return (User) xs.fromXML(in.readUTF());
                    }
                    else if (inputLine.startsWith("FINDUSERBYNAME:FAILED")) {

                        return null;
                    }
                }
                else {
                    this.gotConnection=false;
                    //log.error("Error reading from server");
                }
            }
            catch (IOException ioe) {
                //log.error("IO error talking to server: " + ioe.getMessage(), ioe);
                return user;
            } /*catch (ClassNotFoundException e) {
                e.printStackTrace();
            }*/
        }
        return user;
    }

    private boolean startConnection()
    {
        //log.debug("startConnection() called ...");
        String inputLine;
        boolean connected = false;
        // The client initiates the transaction
        try {
            out.writeUTF("CONNECT: wClientConn v"+WCLIENT_VERSION);
            out.flush();
            if ( (inputLine = in.readUTF()) != null) {
                if (inputLine.startsWith("CONNECT:ACCEPT")) {
                    //log.debug("wClient connection ACCEPTED");
                    connected = true;
                }
                else {
                    //log.warn("wClient connection FAILED");
                }
            }
            else {
                this.gotConnection=false;
                //log.warn("Error reading from server");
            }
        }
        catch (IOException ioe) {
            //log.error("IO error talking to server: " + ioe.getMessage(), ioe);
        }
        return connected;
    }

    private boolean startCBConnection(ObjectOutputStream CBout, ObjectInputStream CBin)
    {
        //log.debug("startCBConnection() called ...");
        String inputLine;
        boolean connected = false;
        // The client initiates the transaction
        try {
            CBout.writeUTF("CONNECT: wClientConnCB v"+WCLIENT_VERSION);
            CBout.flush();
            if ( (inputLine = CBin.readUTF()) != null) {
                if (inputLine.startsWith("CONNECT:ACCEPT")) {
                    //log.debug("wClient Callback connection ACCEPTED");
                    connected = true;
                }
                else {
                    //log.warn("wClient Callback connection FAILED");
                }
            }
            else {
                this.gotCBConnection=false;
                //log.warn("Error reading CB from server");
            }
        }
        catch (IOException ioe) {
            //log.error("IO error talking to server: " + ioe.getMessage(), ioe);
        }
        return connected;
    }

}

class callbackListener implements Runnable{

    ObjectInputStream in;
    ObjectOutputStream out;
    WiKIDEventListener eventCB;
    wClient parent;
    boolean running=true;

    public callbackListener(ObjectInputStream in, ObjectOutputStream out, WiKIDEventListener eventCB, wClient parent) {
        this.in = in;
        this.out = out;
        this.eventCB = eventCB;
        this.parent = parent;
    }

    public void run() {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        try {
            XStream xs = new XStream();
            WiKIDEvent eventIn = (WiKIDEvent) xs.fromXML(in.readUTF());
            while (running) {
                eventCB.eventNotify(eventIn);
                if(eventIn.getEventType()==WiKIDEvent.CALLBACK_DEREGISTERED) running=false;
                if (running) eventIn = (WiKIDEvent) xs.fromXML(in.readUTF());
            }
        } catch (IOException e) {
            if (running) parent.reconnectCB(eventCB);
            running=false;
        }  finally {
            try {
                in.close();
                out.close();
            }
            catch (IOException e) {
                // already closed
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public WiKIDEventListener getEventCB() {
        return eventCB;
    }
}

