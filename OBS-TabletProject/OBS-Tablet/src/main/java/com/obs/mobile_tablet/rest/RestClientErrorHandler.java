package com.obs.mobile_tablet.rest;

import com.obs.mobile_tablet.exceptions.BadRequestException;

import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * Created by stevenguitar on 1/14/14.
 */
public class RestClientErrorHandler implements ResponseErrorHandler {

    private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return errorHandler.hasError(clientHttpResponse);
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        System.out.println("HERE");
        String responseBody = IOUtils.toString(clientHttpResponse.getBody());
        BadRequestException bre = new BadRequestException();
        bre.addEntry("headers", clientHttpResponse.getHeaders());
        bre.addEntry("status", clientHttpResponse.getStatusCode().toString());
        bre.addEntry("body", responseBody);
        throw bre;
    }
}
