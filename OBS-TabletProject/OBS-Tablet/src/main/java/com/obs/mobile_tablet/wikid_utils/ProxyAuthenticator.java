package com.obs.mobile_tablet.wikid_utils;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * Created by IntelliJ IDEA.
 * User: stguitar
 * Date: 4/5/12
 * Time: 12:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProxyAuthenticator extends Authenticator {
        //-----------------------------------------------------------------------
    // Instance variables
    //-----------------------------------------------------------------------

    private ProxySettings _settings;
    private String _username;
    private char[] _password;
    private boolean _returnedCredentials = false;

    //-----------------------------------------------------------------------
    // Constructors
    //-----------------------------------------------------------------------

    public ProxyAuthenticator() {
    }

    public ProxyAuthenticator(ProxySettings settings) {
        _settings = settings;
    }

    //-----------------------------------------------------------------------
    // Authenticator methods
    //-----------------------------------------------------------------------

    protected synchronized PasswordAuthentication getPasswordAuthentication() {
       initialize();
        return new PasswordAuthentication(_username, _password);
    }

    public String getUsername() {
        return _username;
    }

    public String getPassword() {
        return (_password != null) ? new String(_password) : null;
    }

    //-----------------------------------------------------------------------
    // Private methods
    //-----------------------------------------------------------------------

    private void initialize() {
        if (_settings != null && _settings.isProxyEnabled() && _settings.isProxyAuthenticationEnabled()) {
            _username = _settings.getProxyUsername();
            _password = _settings.getProxyPassword().toCharArray();
        } else {
            if (_username == null || _password == null) {
//                ProxyAuthenticationDialog dialog = new ProxyAuthenticationDialog();
//                dialog.setModal(true);
//                dialog.setVisible(true);
//                _username = dialog.getUsername();
//                _password = dialog.getPassword();
//                if (_settings != null) {
//                    _settings.setProxyAuthenticationEnabled(true);
//                    _settings.setProxyUsername(getUsername());
//                    _settings.setProxyPassword(getPassword());
//                }
            }
        }
    }
}
