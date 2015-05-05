package fury.marvel.trinity.agent.bci.transformer.modifier.stack.info;


import fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.marshall.StringObject;

import java.util.List;

/**
 * Created by poets11 on 15. 2. 4..
 */
public interface PackageStackInfo extends StackInfo {
    String getClassName();

    String getMethodName();

    List<StringObject> getParams();

    StringObject getResult();
}
