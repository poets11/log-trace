package fury.marvel.trinity.stack.info.impl;


import fury.marvel.trinity.stack.info.RequestStackInfo;
import fury.marvel.trinity.stack.info.marshall.DefaultMarshaller;
import fury.marvel.trinity.stack.info.marshall.StringObject;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by poets11 on 15. 2. 4..
 */
public class RequestStackInfoImpl extends AbstractStackInfo implements RequestStackInfo {
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

    public void setRequest(HttpServletRequest request) {
//        ClassLoader reqClassLoader = request.getClass().getClassLoader();
//        ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();

        threadId = Thread.currentThread().getId();

        url = request.getRequestURL().toString();
        method = request.getMethod();

        initHeaders(request);
        initParams(request);
    }

    private void initParams(HttpServletRequest request) {
        params = new HashMap<String, List<String>>();

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String[] parameterValues = request.getParameterValues(paramName);

            params.put(paramName, Arrays.asList(parameterValues));
        }
    }

    private void initHeaders(HttpServletRequest request) {
        headers = new HashMap<String, String>();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);

            headers.put(headerName, headerValue);
        }
    }

    public void setModelAndView(ModelAndView modelAndView) {
        initModels(modelAndView);
    }

    private void initModels(ModelAndView modelAndView) {
        if(modelAndView == null) return;
        
        models = new HashMap<String, StringObject>();

        Iterator<Map.Entry<String, Object>> iterator = modelAndView.getModel().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            models.put(entry.getKey(), DefaultMarshaller.marshall(entry.getValue()));
        }
    }

    @Override
    public String toString() {
        return url + "(" + method + ")";
    }
}
