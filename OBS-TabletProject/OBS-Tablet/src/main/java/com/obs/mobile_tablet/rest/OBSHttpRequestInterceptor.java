package com.obs.mobile_tablet.rest;

import android.util.Log;

import com.obs.mobile_tablet.utils.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by david on 1/2/14.
 */
public class OBSHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    public AuthHelper _authHelper;
    private static final String SET_COOKIE = "set-cookie";
    private static final String COOKIE = "cookie";
    private static final String COOKIE_STORE = "cookieStore";
    private ArrayList<String> _cookies;

    public OBSHttpRequestInterceptor() {
        Logger.debug("New Request Interceptor");
        _cookies = new ArrayList<String>();
    }

    @Override

    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {

        Logger.debug(getClass().getSimpleName()+" : >>> entering intercept");

        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(httpRequest);
        HttpHeaders headers = _authHelper.getAuthHeader(httpRequest.getURI().toString(),httpRequest.getMethod().toString());
        Map headersMap = headers.toSingleValueMap();
        httpRequest.getHeaders().setAll(headersMap);

        // if the header doesn't exist, add any existing, saved cookies
            if (!_cookies.isEmpty()){
                String cookie = getCookie();
                httpRequest.getHeaders().add(COOKIE, cookie);
            }

        ClientHttpResponse response = clientHttpRequestExecution.execute(requestWrapper, bytes);
        Log.d("Uri: ", String.valueOf(requestWrapper.getURI()));
        List<String> cookies = response.getHeaders().get(SET_COOKIE);
        if (cookies != null) {
            for (String cookie : cookies) {
                Logger.debug(getClass().getSimpleName() + " >>> response cookie = " + cookie);
                addCookie(cookie);
            }
        }
        Logger.debug(getClass().getSimpleName() + ">>> leaving intercept");

        return response;

    }
    private String getCookie() {
        if (_cookies.size() == 0) {
            return null;
        }
        return _cookies.get(0);
    }
    private void addCookie(String cookie) {
        _cookies.add(cookie);
    }
}
