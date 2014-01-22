// $Id: ProxySettings.java,v 1.3 2007-10-09 11:37:59 jblum Exp $
package com.obs.mobile_tablet.wikid_utils;

import java.io.Serializable;

/**
 * Class ProxySettings
 *
 * @author Jason Blumenkrantz
 * @author Copyright(c) 2006 Online Banking Solutions. All Rights Reserved
 * @version $Revision: 1.3 $
 */
public class ProxySettings implements Serializable {

    //-----------------------------------------------------------------------
    // Instance variables.
    //-----------------------------------------------------------------------

    private boolean _isProxyEnabled;
    private String _proxyHost;
    private int _proxyPort;
    private boolean _isProxyAuthenticationEnabled;
    private String _proxyUsername;
    private String _proxyPassword;
    
    //-----------------------------------------------------------------------
    // Constructors.
    //-----------------------------------------------------------------------

    public ProxySettings() {
    }

    //-----------------------------------------------------------------------
    // Public methods.
    //-----------------------------------------------------------------------

    public boolean isProxyEnabled() {
        return _isProxyEnabled;
    }

    public void setProxyEnabled(boolean proxyEnabled) {
        _isProxyEnabled = proxyEnabled;
    }

    public String getProxyHost() {
        return _proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        _proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return _proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        _proxyPort = proxyPort;
    }

    public boolean isProxyAuthenticationEnabled() {
        return _isProxyAuthenticationEnabled;
    }

    public void setProxyAuthenticationEnabled(boolean proxyAuthenticationEnabled) {
        _isProxyAuthenticationEnabled = proxyAuthenticationEnabled;
    }

    public String getProxyUsername() {
        return _proxyUsername;
    }

    public void setProxyUsername(String proxyUsername) {
        _proxyUsername = proxyUsername;
    }

    public String getProxyPassword() {
        return _proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        _proxyPassword = proxyPassword;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("Proxy[");
        if (_isProxyEnabled) {
            if (_isProxyAuthenticationEnabled) {
                sb.append(_proxyUsername).append("@");
            }
            sb.append(_proxyHost).append(":").append(_proxyPort);
        } else {
            sb.append("NONE");
        }
        sb.append("]");
        return sb.toString();
    }
}

