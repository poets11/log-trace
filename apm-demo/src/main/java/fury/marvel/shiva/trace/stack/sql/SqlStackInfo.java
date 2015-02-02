package fury.marvel.shiva.trace.stack.sql;

import fury.marvel.shiva.trace.stack.StackInfo;

import java.sql.ResultSet;

/**
 * Created by poets11 on 15. 2. 2..
 */
public interface SqlStackInfo extends StackInfo {
    void setSql(String sql);
    String getSql();
    void addParam(Object key, Object value);
    void setReturnValue(Object returnValue);
    void initResultSet(ResultSet resultSet);
    void next(boolean hasNext);
    void addResultValue(Object key, Object value);
}
