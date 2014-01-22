package com.rest;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obs.mobile_tablet.datamodel.ResponseWrapper;
import com.obs.mobile_tablet.datamodel.reporting.GetAccountResponse;
import com.obs.mobile_tablet.datamodel.reporting.GetAccountTotalsResponse;
import com.obs.mobile_tablet.datamodel.reporting.GetAccountsResponse;
import com.obs.mobile_tablet.datamodel.reporting.GetTransactionGroupsResponse;
import com.obs.mobile_tablet.datamodel.reporting.GetTransactionsResponse;
import com.obs.mobile_tablet.datamodel.reporting.GraphData;
import com.obs.mobile_tablet.datamodel.reporting.GraphObj;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.util.Transcript;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.io.InputStream;

@RunWith(RobolectricGradleTestRunner.class)
public class HttpMessageConverterTest {

    Transcript transcript;
    MappingJackson2HttpMessageConverter converter;
    ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;
        transcript = new Transcript();

        converter = new MappingJackson2HttpMessageConverter();
        mapper = converter.getObjectMapper();

        Robolectric.getBackgroundScheduler().pause();
        Robolectric.getUiThreadScheduler().pause();
    }

    public <T> T objectTest(Class<T> clazz, String filename) throws IOException {

        InputStream is = HttpMessageConverterTest.class.getClassLoader().getResourceAsStream(filename);
        Assert.assertNotNull(is);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpInputMessage httpInputMessage = Mockito.mock(HttpInputMessage.class);
        Mockito.when(httpInputMessage.getBody()).thenReturn(is);
        Mockito.when(httpInputMessage.getHeaders()).thenReturn(httpHeaders);

        ResponseWrapper wr = (ResponseWrapper) converter.read(ResponseWrapper.class,httpInputMessage);
        Assert.assertNotNull(wr);
        Assert.assertNotNull(wr.data);

        return mapper.convertValue(wr.data, clazz);
    }

    @Test
    public void testGetAccounts() throws IOException{
        GetAccountsResponse response = objectTest(GetAccountsResponse.class,"getAccounts.json");
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getAccounts());
        Assert.assertNotNull(response.getAccountTypes());
        Assert.assertNotSame(0, response.getAccounts().size());
        Assert.assertNotSame(0, response.getAccountTypes().size());
    }

    @Test
    public void testGetAccount() throws IOException {

        GetAccountResponse response = objectTest(GetAccountResponse.class, "getAccount.json");
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getAccount());
    }

    @Test
    public void testGetTransactionGroups()  throws IOException {
        GetTransactionGroupsResponse response = objectTest(GetTransactionGroupsResponse.class, "getTransactionGroups.json");
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getTransactionGroups());
        Assert.assertNotSame(0, response.getTransactionGroups().size());
    }

    @Test
    public void testGetTransactions()  throws IOException {
        GetTransactionsResponse response = objectTest(GetTransactionsResponse.class, "getTransactions.json");
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getTransactionDetails());
        Assert.assertNotNull(response.getTransactionDetails().getGraphs());
        Assert.assertNotNull(response.getTransactionDetails().getTransactions());
        Assert.assertNotSame(0, response.getTransactionDetails().getGraphs().size());
        Assert.assertNotSame(0, response.getTransactionDetails().getTransactions().size());
    }

    @Test
    public void testGetAccountTotals() throws IOException {
        GetAccountTotalsResponse response = objectTest(GetAccountTotalsResponse.class, "getAccountTotals.json");
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getTotals());
        Assert.assertNotSame(0, response.getTotals().size());
    }

    @Test
    public void testGraphObj() throws IOException {

        GraphObj response = objectTest(GraphObj.class, "sampleGraphObj.json");
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.type);
        Assert.assertNotSame(0, response.dataPoints.size());
        Log.d("graphObj.type: ",response.type);
        Log.d("graphObj.displayText: ",response.displayText);

    }


}
