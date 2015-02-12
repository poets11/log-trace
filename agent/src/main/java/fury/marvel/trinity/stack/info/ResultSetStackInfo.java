package fury.marvel.trinity.stack.info;


import fury.marvel.trinity.stack.info.marshall.StringObject;

import java.util.List;
import java.util.Map;

/**
 * Created by poets11 on 15. 2. 4..
 */
public interface ResultSetStackInfo {
    List<String> getColumnNames();

    List<Map<String, StringObject>> getDatas();
}
