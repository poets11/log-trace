package fury.marvel.trinity.stack.info.impl;


import fury.marvel.trinity.reflect.Method;
import fury.marvel.trinity.stack.info.RequestStackInfo;
import fury.marvel.trinity.stack.info.marshall.JacksonMarshaller;
import fury.marvel.trinity.stack.info.marshall.MarshallerFactory;
import fury.marvel.trinity.stack.info.marshall.StringObject;

import java.util.*;

/**
 * Created by poets11 on 15. 2. 4..
 */
public class RequestStackInfoImpl extends AbstractStackInfo implements RequestStackInfo {
    private static Method<StringBuffer> getRequestURL = new Method<StringBuffer>("getRequestURL", null);
    private static Method<String> getMethod = new Method<String>("getMethod", null);
    private static Method<Enumeration<String>> getParameterNames = new Method<Enumeration<String>>("getParameterNames", null);
    private static Method<Enumeration<String>> getHeaderNames = new Method<Enumeration<String>>("getHeaderNames", null);
    private static Method<String[]> getParameterValues = new Method<String[]>("getParameterValues", "java.lang.String");
    private static Method<String> getHeader = new Method<String>("getHeader", "java.lang.String");
    private static Method<Map<String, Object>> getModel = new Method<Map<String, Object>>("getModel", null);

    private long threadId;
    private String url;
    private String method;
    private Map<String, String> headers;
    private Map<String, List<String>> params;
    private Map<String, StringObject> models;

    @Override
    public long getThreadId() {
        return threadId;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public Map<String, List<String>> getParams() {
        return params;
    }

    @Override
    public Map<String, StringObject> getModels() {
        return models;
    }

    public void setRequest(Object request) {
        try {
            threadId = Thread.currentThread().getId();
            url = getRequestURL.invoke(request, null).toString();
            method = getMethod.invoke(request, null);

            initHeaders(request);
            initParams(request);
        } catch (Exception e) {
            System.out.println("// TODO Exception in RequestStackInfoImpl.setRequest()");
        }
    }

    private void initParams(Object request) throws Exception {
        params = new HashMap<String, List<String>>();

        Enumeration<String> parameterNames = getParameterNames.invoke(request, null);
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String[] parameterValues = getParameterValues.invoke(request, paramName);

            params.put(paramName, Arrays.asList(parameterValues));
        }
    }

    private void initHeaders(Object request) throws Exception {
        headers = new HashMap<String, String>();

        Enumeration<String> headerNames = getHeaderNames.invoke(request, null);
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = getHeader.invoke(request, headerName);

            headers.put(headerName, headerValue);
        }
    }

    public void setModelAndView(Object modelAndView) {
        if (modelAndView != null) {
            try {
                initModels(modelAndView);
            } catch (Exception e) {
                System.out.println("// TODO Exception in RequestStackInfoImpl.setModelAndView()");
            }
        }
    }

    private void initModels(Object modelAndView) throws Exception {
        models = new HashMap<String, StringObject>();

        Iterator<Map.Entry<String, Object>> iterator = getModel.invoke(modelAndView, null).entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            models.put(entry.getKey(), MarshallerFactory.getMarshaller().marshall(entry.getValue()));
        }
    }

    @Override
    public String toString() {
        return "RequestStackInfoImpl{" +
                "threadId=" + threadId +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", headers=" + headers +
                ", params=" + params +
                ", models=" + models +
                "} " + super.toString();
    }
}
