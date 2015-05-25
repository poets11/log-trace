package hanmi.framework.debugger.bci.agent.stack.info;

import hanmi.framework.debugger.bci.agent.config.AgentConfig;
import hanmi.framework.debugger.bci.agent.assist.Method;
import hanmi.framework.debugger.bci.agent.stack.info.marshall.MarshallerFactory;
import hanmi.framework.debugger.bci.agent.stack.info.marshall.StringObject;

import java.util.*;

/**
 * Created by poets11 on 15. 5. 20..
 */
public class HandlerAdapterStackInfo extends StackInfo {
    private static Method<StringBuffer> getRequestURL = new Method<StringBuffer>("getRequestURL", null);
    private static Method<String> getMethod = new Method<String>("getMethod", null);
    private static Method<Enumeration<String>> getParameterNames = new Method<Enumeration<String>>("getParameterNames", null);
    private static Method<Enumeration<String>> getHeaderNames = new Method<Enumeration<String>>("getHeaderNames", null);
    private static Method<String[]> getParameterValues = new Method<String[]>("getParameterValues", "java.lang.String");
    private static Method<String> getHeader = new Method<String>("getHeader", "java.lang.String");
    private static Method<Map<String, Object>> getModel = new Method<Map<String, Object>>("getModel", null);
    private static Method<String> getViewName = new Method<String>("getViewName", null);

    private long threadId;
    private String url;
    private String method;
    private String viewName;
    private Map<String, String> headers;
    private Map<String, List<String>> params;
    private Map<String, StringObject> models;    

    @Override
    public void setArguments(Object[] arguments) {
        setRequest(arguments[0]);
    }

    @Override
    public void setResultValue(Object resultValue) {
        setModelAndView(resultValue);
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, StringObject> getModels() {
        return models;
    }

    public void setModels(Map<String, StringObject> models) {
        this.models = models;
    }

    public Map<String, List<String>> getParams() {
        return params;
    }

    public void setParams(Map<String, List<String>> params) {
        this.params = params;
    }

    private void setRequest(Object request) {
        try {
            threadId = Thread.currentThread().getId();
            url = getRequestURL.invoke(request, null).toString();
            method = getMethod.invoke(request, null);

            initHeaders(request);
            initParams(request);
        } catch (Exception e) {
            AgentConfig.error(e);
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

    private void setModelAndView(Object modelAndView) {
        if (modelAndView != null) {
            try {
                initModels(modelAndView);
            } catch (Exception e) {
                AgentConfig.error(e);
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
        
        viewName = getViewName.invoke(modelAndView, null);
    }
}
