package com.obs.mobile_tablet.rest;

import android.os.AsyncTask;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obs.mobile_tablet.OBSApplication;
import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.datamodel.ResponseWrapper;
import com.obs.mobile_tablet.datamodel.setup.ApiSession;
import com.obs.mobile_tablet.exceptions.BadRequestException;
import com.obs.mobile_tablet.utils.BannersReceivedEvent;
import com.obs.mobile_tablet.utils.ForApplication;
import com.obs.mobile_tablet.utils.Logger;

import com.obs.mobile_tablet.utils.TimeoutEvent;
import com.squareup.otto.Bus;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;

import static junit.framework.Assert.*;

/**
 * Created by david on 12/19/13.
 */

public class RestClient {
    public enum HttpMethod {
        GET,
        POST,
        PUT,
        DELETE
    }

    private String HostIP;
    public String apiversion;
    private AuthHelper _authHelper;
    private Boolean FAKE_API;

    RestTemplate restTemplate;
    ObjectMapper objectMapper;

    @Inject
    @ForApplication
    OBSApplication application;

    @Inject
    Bus bus;

    MappingJackson2HttpMessageConverter converter;


    public RestClient() {
        Logger.debug("Creating New Rest Client");
        restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter j = new MappingJackson2HttpMessageConverter();
        restTemplate.getMessageConverters().add(j);
        restTemplate.setErrorHandler(new RestClientErrorHandler());
        objectMapper = j.getObjectMapper();
        _authHelper = new AuthHelper();
        OBSHttpRequestInterceptor requestInterceptor = new OBSHttpRequestInterceptor();
        requestInterceptor._authHelper = _authHelper;
        restTemplate.setInterceptors(Collections.<ClientHttpRequestInterceptor>singletonList(requestInterceptor));
        HostIP = "https://mfcqa.onlinebankingsolutions.com";
        FAKE_API = false;
//       HostIP = "http://172.23.7.136:8080";
    }

    public void setFAKE_API(Boolean FAKE_API) {
        this.FAKE_API = FAKE_API;
    }

    public String getHostIP() {
        return HostIP;
    }

    public void get(String path, Callback callback) {
        GetHttpTask asyncTask = new GetHttpTask(path, restTemplate, callback);
        asyncTask.execute();
    }

    public void get(String path, Map<String, Object> params, Class clazz, Callback callback) {
        if (FAKE_API){
            FakeHttpTask asyncTask = new FakeHttpTask(HttpMethod.GET,path,params,restTemplate,clazz,callback);
            asyncTask.execute();
        } else {
            GetHttpTask asyncTask = new GetHttpTask(path, params, restTemplate, clazz, callback);
            asyncTask.execute();
        }
    }

    public void get(String path, Map<String, Object> params, Class clazz, Class nestedClazz, String nestedKey, Callback callback) {
        if (FAKE_API){
            FakeHttpTask asyncTask = new FakeHttpTask(HttpMethod.GET,path,params,restTemplate,clazz,callback);
            asyncTask.execute();
        } else {
            GetHttpTask asyncTask = new GetHttpTask(path, params, restTemplate, clazz, nestedClazz, nestedKey, callback);
            asyncTask.execute();
        }
    }

    public void post(String path, Object params, Class clazz, Callback callback) {
        if (FAKE_API){
            FakeHttpTask asyncTask = new FakeHttpTask(HttpMethod.POST,path,params,restTemplate,clazz,callback);
            asyncTask.execute();
        } else {
             PostHttpTask asyncTask = new PostHttpTask(path, params, restTemplate, clazz, callback);
             asyncTask.execute();
        }
    }

    public void delete(String path, Object params, Class clazz, Callback callback) {
        if (FAKE_API){
            FakeHttpTask asyncTask = new FakeHttpTask(HttpMethod.DELETE,path,params,restTemplate,clazz,callback);
            asyncTask.execute();
        } else {
             DeleteHttpTask asyncTask = new DeleteHttpTask(path, params, restTemplate, clazz, callback);
             asyncTask.execute();
        }
    }

    public void postLogin(String path, Object params, final Callback callback) {
        PostHttpTask asyncTask = new PostHttpTask(path, params, restTemplate, ApiSession.class, new Callback<ApiSession>() {

            @Override
            public void onTaskCompleted(ApiSession result) {
                _authHelper.processApiSession(result);
                callback.onTaskCompleted(result);

            }

            @Override
            public void onTaskFailure(String reason) {
                callback.onTaskFailure(reason);
            }
        });

        asyncTask.execute();
    }

    public class GetHttpTask extends AsyncTask<Void, Void, ResponseWrapper> {

        private Callback _callback;
        private String _path;
        private RestTemplate _restTemplate;
        private Class _clazz;
        private Class _nestedClazz;
        private String _nestedKey;
        private Map<String, Object> _params;

        public GetHttpTask(String path, RestTemplate restTemplate, Callback callback) {
            this._callback = callback;
            this._path = path;
            this._restTemplate = restTemplate;
        }

        public GetHttpTask(String path, Map<String, Object> params, RestTemplate restTemplate, Class clazz, Callback callback) {
            this._callback = callback;
            this._path = path;
            this._restTemplate = restTemplate;
            this._clazz = clazz;
            this._params = params;
        }

        public GetHttpTask(String path, Map<String, Object> params, RestTemplate restTemplate, Class clazz, Class nestedClazz, String nestedKey, Callback callback) {
            this._nestedKey = nestedKey;
            this._nestedClazz = nestedClazz;
            this._callback = callback;
            this._path = path;
            this._restTemplate = restTemplate;
            this._clazz = clazz;
            this._params = params;
        }

        @Override
        protected ResponseWrapper doInBackground(Void... params) {
            final String url;
            ResponseWrapper responseWrapper = null;
            try {
                if (_path.equalsIgnoreCase("info")) {
                    url = HostIP + "/api/" + _path;
                } else if (_path.contains("api/")) {

                    url = HostIP + _path;

                } else {
                    url = HostIP + "/api/v1/" + _path;

                }
                if (_params != null) {
                    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
                    for (String key:_params.keySet()){
                        builder.queryParam(key,_params.get(key));
                    }
                    String newUrl = builder.build().toUriString();
                    responseWrapper = _restTemplate.getForObject(builder.build().toUriString(), ResponseWrapper.class, _params);

                }else {
                    responseWrapper = _restTemplate.getForObject(url, ResponseWrapper.class);
                }

                if (_clazz != null) {
                    Object data = objectMapper.convertValue(responseWrapper.data, _clazz);
                    if (_nestedClazz != null  && _nestedKey != null) {
                        Object nestedData = objectMapper.convertValue(((LinkedHashMap) responseWrapper.data).get(_nestedKey), _nestedClazz);
                        try {
                            String nestedSetter ="set"+_nestedKey.substring(0,1).toUpperCase()+_nestedKey.substring(1,_nestedKey.length());
                            Class mparams[] = new Class[1];
                            mparams[0] = _nestedClazz.getSuperclass();
                            Method method = _clazz.getDeclaredMethod(nestedSetter, mparams);
                            try {
                                method.invoke(data,nestedData);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }
                    responseWrapper.data = data;
                    return responseWrapper;
                }
            } catch (Exception e) {
                Logger.warn("GET Request Exception:" + e.getMessage());
                _callback.onTaskFailure("Exception" + e.getMessage());
            }

            return responseWrapper;
        }

        @Override
        protected void onPostExecute(ResponseWrapper responseWrapper) {
            if (responseWrapper != null) {
                _callback.onTaskCompleted(responseWrapper.data);

                processResponseWrapper(responseWrapper);
            } else {
                _callback.onTaskFailure("Unknown Error");
                Logger.warn("No ResponseWrapper on GET request");

            }
        }

    }

    public class PostHttpTask extends AsyncTask<Void, Void, ResponseWrapper> {

        private Callback _callback;
        private String _path;
        private RestTemplate _restTemplate;
        private Object _params;
        private Class _clazz;

        public PostHttpTask(String path, Object params, RestTemplate restTemplate, Class clazz, Callback callback) {
            this._callback = callback;
            this._path = path;
            this._restTemplate = restTemplate;
            this._params = params;
            this._clazz = clazz;
        }

        @Override
        protected ResponseWrapper doInBackground(Void... params) {
            try {
                final String url = HostIP + "/api/v1/" + _path;
                return _restTemplate.postForObject(url, _params, ResponseWrapper.class);

            } catch (Exception e) {
                if(e.getCause() instanceof BadRequestException){
                    BadRequestException bre = (BadRequestException)e.getCause();
                    String json = (String)bre.getEntry("body");
                    ResponseWrapper rw = new ResponseWrapper();
                    try{
                        rw = objectMapper.readValue(json, ResponseWrapper.class);
                    } catch (Exception e2){
                        e2.printStackTrace();
                    }
                    rw.setError(true);
                    return rw;
                }
                Logger.warn("POST Request Exception:" + e.getMessage());
                _callback.onTaskFailure("Exception" + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(ResponseWrapper responseWrapper) {
            if (responseWrapper != null && !responseWrapper.isError) {

                Logger.warn("Reason: " + responseWrapper.reason + "\nnonces: " + responseWrapper.nonce + "\ndata: " + responseWrapper.data);
                Object data = objectMapper.convertValue(responseWrapper.data, _clazz);
                _callback.onTaskCompleted(data);
                processResponseWrapper(responseWrapper);

            } else {
                Logger.warn("Failure");
                _callback.onTaskFailure(responseWrapper.reason);
            }
        }

    }

    public class DeleteHttpTask extends AsyncTask<Void, Void, ResponseWrapper> {

        private Callback _callback;
        private String _path;
        private RestTemplate _restTemplate;
        private Object _params;
        private Class _clazz;




        public DeleteHttpTask(String path, Object params, RestTemplate restTemplate, Class clazz, Callback callback) {
            this._callback = callback;
            this._path = path;
            this._restTemplate = restTemplate;
            this._params = params;
            this._clazz = clazz;
        }

        @Override
        protected ResponseWrapper doInBackground(Void... params) {
            try {
                final String url = HostIP + "/api/v1/" + _path;
                _restTemplate.delete(url,ResponseWrapper.class,_params);
                HttpEntity<ResponseWrapper> httpEntity = new HttpEntity<ResponseWrapper>(new HttpHeaders());
                ResponseEntity<ResponseWrapper> responseEntity = restTemplate.exchange(url, org.springframework.http.HttpMethod.DELETE,httpEntity,ResponseWrapper.class,_params);
                return responseEntity.getBody();
            } catch (Exception e) {
                Logger.warn("DELETE Request Exception:" + e.getMessage());
//                _callback.onTaskFailure("Exception" + e.getMessage());

            }

            return null;
        }



        @Override
        protected void onPostExecute(ResponseWrapper responseWrapper) {
            if (responseWrapper != null) {

                Logger.warn("Reason: " + responseWrapper.reason + "\nnonces: " + responseWrapper.nonce + "\ndata: " + responseWrapper.data);
                Object data = objectMapper.convertValue(responseWrapper.data, _clazz);
                _callback.onTaskCompleted(data);
                processResponseWrapper(responseWrapper);

            } else {
                Logger.warn("Failure");
                _callback.onTaskFailure("failure in onPostExecute");


            }
        }

    }




    public class FakeHttpTask extends AsyncTask<Void, Void, ResponseWrapper> {

        private Callback _callback;
        private String _path;
        private RestTemplate _restTemplate;
        private Object _params;
        private Class _clazz;
        private HttpMethod _httpMethod;



        public FakeHttpTask(HttpMethod httpMethod, String path, Object params, RestTemplate restTemplate, Class clazz, Callback callback) {
            this._callback = callback;
            this._path = path;
            this._restTemplate = restTemplate;
            this._params = params;
            this._clazz = clazz;
            this._httpMethod = httpMethod;
            setFAKE_API(false);
        }

        @Override
        protected ResponseWrapper doInBackground(Void... params) {
            try {
                ResponseWrapper responseWrapper = mockCall(_clazz,_httpMethod,_path);
                Logger.warn("Reason: " + responseWrapper.reason + "\nnonces: " + responseWrapper.nonce + "\ndata: " + responseWrapper.data);
                responseWrapper.data = objectMapper.convertValue(responseWrapper.data, _clazz);
                return responseWrapper;

            } catch (Exception e) {
                Logger.warn("FAKE Request Exception:" + e.getMessage());
                _callback.onTaskFailure("Exception" + e.getMessage());
            }

            return null;
        }



        @Override
        protected void onPostExecute(ResponseWrapper responseWrapper) {
            if (responseWrapper != null) {
                _callback.onTaskCompleted(responseWrapper.data);
                 processResponseWrapper(responseWrapper);

            } else {
                Logger.warn("No fake data found for this request");
                _callback.onTaskFailure("No fake data found for this request");


            }
        }
    }

    public <T> ResponseWrapper mockCall(Class<T> clazz, HttpMethod httpMethod, String path) throws IOException {
        path = path.replace("/api/v1/","");
        InputStream is = application.getResources().openRawResource(R.raw.tdl);
        assertNotNull(is);
        Map<String,ResponseWrapper> wrapperMap;
        wrapperMap = objectMapper.readValue(is, new TypeReference<HashMap<String,ResponseWrapper>>(){

        });
//        wrapperMap = objectMapper.convertValue(is, new TypeReference<HashMap<String,ResponseWrapper>>(){
//
//        });

        ResponseWrapper wr = wrapperMap.get(httpMethod+" "+path);
//        ResponseWrapper wr = (ResponseWrapper) converter.read(ResponseWrapper.class,httpInputMessage);
//        assertNotNull(wr);

        return wr;


    }


    private void processResponseWrapper(ResponseWrapper responseWrapper) {

        if (responseWrapper.getTimeout() != null) {
            if (responseWrapper.getTimeout()) {
                bus.post(new TimeoutEvent());
            }
        }
        bus.post(new BannersReceivedEvent(responseWrapper.getErrorBanners(),
                responseWrapper.getInfoBanners(),
                responseWrapper.getSuccessBanners()));


    }

    /*
    public class HttpTask extends AsyncTask<Void, Void, ResponseWrapper> {

        private Callback _callback;
        private String _path;
        private RestTemplate _restTemplate;
        private Class _clazz;
        private Class _nestedClazz;
        private String _nestedKey;
        private Map<String, Object> _params;
        private HttpMethod _httpMethod;

        public HttpTask(String path, RestTemplate restTemplate, Callback callback) {
            this._callback = callback;
            this._path = path;
            this._restTemplate = restTemplate;
        }

        public HttpTask(String path, Map<String, Object> params, RestTemplate restTemplate, Class clazz, Callback callback) {
            this._callback = callback;
            this._path = path;
            this._restTemplate = restTemplate;
            this._clazz = clazz;
            this._params = params;
        }

        public HttpTask(HttpMethod httpMethod, String path, Map<String, Object> params, RestTemplate restTemplate, Class clazz, Class nestedClazz, String nestedKey, Callback callback) {
            _nestedKey = nestedKey;
            _nestedClazz = nestedClazz;
            _callback = callback;
            _path = path;
            _restTemplate = restTemplate;
            _clazz = clazz;
            _params = params;
        }

        @Override
        protected ResponseWrapper doInBackground(Void... params) {
            String url;
            try {
                if (_path.equalsIgnoreCase("info")) {
                    url = HostIP + "/api/" + _path;
                } else if (_path.contains("api/")) {

                    url = HostIP + _path;

                } else {
                    url = HostIP + "/api/v1/" + _path;

                }
                if (_params != null) {
                    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
                    for (String key:_params.keySet()){
                        builder.queryParam(key,_params.get(key));
                    }
                    url = builder.build().toUriString();
                }
                ResponseWrapper responseWrapper;
                switch (_httpMethod) {
                    case GET:
                        responseWrapper = _restTemplate.getForObject(url, ResponseWrapper.class);
                        break;
                    case POST:
                        responseWrapper = _restTemplate.postForObject(url, _params, ResponseWrapper.class);
                    break;
                    case PUT:
//                        _restTemplate.delete(url,ResponseWrapper.class,_params);

                        break;
                    case DELETE:
                        HttpEntity<ResponseWrapper> httpEntity = new HttpEntity<ResponseWrapper>(new HttpHeaders());
                        ResponseEntity<ResponseWrapper> responseEntity = restTemplate.exchange(url, org.springframework.http.HttpMethod.DELETE,httpEntity,ResponseWrapper.class,_params);
                        responseWrapper = responseEntity.getBody();
                        break;
                    case default:
                        responseWrapper = _restTemplate.getForObject(url, ResponseWrapper.class);

                        break;
                }

                return responseWrapper;

            } catch (Exception e) {
                Logger.warn("GET Request Exception:" + e.getMessage());
                _callback.onTaskFailure("Exception" + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(ResponseWrapper responseWrapper) {
            if (responseWrapper != null) {
                Logger.warn("Reason: " + responseWrapper.reason + "\nnonces: " + responseWrapper.nonce);
                if (_clazz != null) {
                    Object data = objectMapper.convertValue(responseWrapper.data, _clazz);
                    if (_nestedClazz != null  && _nestedKey != null) {
                        Object nestedData = objectMapper.convertValue(((LinkedHashMap) responseWrapper.data).get(_nestedKey), _nestedClazz);
                        try {
                            String nestedSetter ="set"+_nestedKey.substring(0,1).toUpperCase()+_nestedKey.substring(1,_nestedKey.length());
                            Class params[] = new Class[1];
                            params[0] = _nestedClazz.getSuperclass();
                            Method method = _clazz.getDeclaredMethod(nestedSetter, params);
                            try {
                                method.invoke(data,nestedData);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }
                    _callback.onTaskCompleted(data);

                } else {
                    _callback.onTaskCompleted(responseWrapper.reason != null ? responseWrapper.reason : "Success, but no reason given");
                }
                processResponseWrapper(responseWrapper);
            } else {
                _callback.onTaskFailure("Unknown Error");
                Logger.warn("No greeting :(");

            }
        }

    }
    */
}
