package fury.marvel.shiva.trace.stack.init;

import fury.marvel.shiva.trace.ObjectConverter;
import fury.marvel.shiva.writer.converter.ConverterFactory;
import fury.marvel.shiva.trace.stack.StackInfo;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by poets11 on 15. 1. 28..
 */
public class RequestStackInfo implements StackInfo {
    private long threadId;
    private long start;
    private long end;
    private long elapsedTime;
    private String deviceInfo = "PC";
    private String contentType;
    private String remoteUser;
    private String url;
    private String method;
    private Map<String, String> headers;
    private Map<String, String> params;
    private String viewPath;
    private Map<String, String> model;
    private List<StackInfo> childStack;

    public RequestStackInfo() {
        init();
    }

    public String getRemoteUser() {
        return remoteUser;
    }

    public void setRemoteUser(String remoteUser) {
        this.remoteUser = remoteUser;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    private void init() {
        threadId = Thread.currentThread().getId();
        headers = new HashMap<String, String>();
        params = new HashMap<String, String>();
        childStack = new ArrayList<StackInfo>();
        model = new HashMap<String, String>();
    }

    public long getThreadId() {
        return threadId;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
        setElapsedTime(end - start);
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
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

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getViewPath() {
        return viewPath;
    }

    public Map<String, String> getModel() {
        return model;
    }

    public void setRequest(HttpServletRequest request) {
        url = request.getRequestURL().toString();
        method = request.getMethod();
        contentType = request.getContentType();
        remoteUser = request.getRemoteUser();

        Enumeration<String> em = request.getHeaderNames();
        while (em.hasMoreElements()) {
            String name = em.nextElement();
            String value = request.getHeader(name);

            headers.put(name, value);
        }

        em = request.getParameterNames();
        while (em.hasMoreElements()) {
            String name = em.nextElement();
            String value = request.getParameter(name);

            params.put(name, value);
        }
    }

    public void setModelAndView(ModelAndView modelAndView) {
        viewPath = modelAndView.getViewName();

        Iterator<Map.Entry<String, Object>> iterator = modelAndView.getModel().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            Object value = entry.getValue();

            model.put(key, ObjectConverter.convertString(value));
        }
    }

    @Override
    public StackInfo getParent() {
        return null;
    }

    @Override
    public void setParent(StackInfo stackInfo) {

    }

    @Override
    public void appendChild(StackInfo stackInfo) {
        childStack.add(stackInfo);
    }

    @Override
    public List<StackInfo> getChild() {
        return childStack;
    }
}
