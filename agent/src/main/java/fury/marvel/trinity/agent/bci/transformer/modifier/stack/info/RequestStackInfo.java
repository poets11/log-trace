package fury.marvel.trinity.agent.bci.transformer.modifier.stack.info;


import fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.marshall.StringObject;

import java.util.List;
import java.util.Map;

/**
 * Created by poets11 on 15. 2. 4..
 */
public interface RequestStackInfo extends StackInfo {
    long getThreadId();

    String getUrl();

    String getMethod();

    Map<String, String> getHeaders();

    Map<String, List<String>> getParams();

    Map<String, StringObject> getModels();
}
