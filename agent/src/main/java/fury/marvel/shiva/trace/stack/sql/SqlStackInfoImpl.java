package fury.marvel.shiva.trace.stack.sql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fury.marvel.shiva.trace.stack.StackInfo;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by poets11 on 15. 2. 2..
 */
public class SqlStackInfoImpl implements SqlStackInfo {
    private long start;
    private long end;
    private long elapsedTime;
    private String sql;
    private Map<Object, Object> params;
    private String returnValue;
    private List<String> columnNames;
    private List<Map<String, Object>> resultSet;
    @JsonIgnore
    private Map<String, Object> currentResultSet;
    @JsonIgnore
    private StackInfo parent;

    public SqlStackInfoImpl() {
        init();
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

    private void init() {
        params = new HashMap<Object, Object>();
    }

    @Override
    public void setSql(String sql) {
        this.sql = sql;    
    }

    @Override
    public String getSql() {
        return sql;
    }

    @Override
    public void addParam(Object key, Object value) {
        this.params.put(key, value);
    }

    @Override
    public void setReturnValue(Object returnValue) {
        if (returnValue != null) {
            if(returnValue instanceof ResultSet) initResultSet((ResultSet) returnValue);
            else this.returnValue = returnValue.toString();
        }
    }

    @Override
    public void initResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            this.resultSet = new ArrayList<Map<String, Object>>();
            this.columnNames = new ArrayList<String>();

            try {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    columnNames.add(metaData.getColumnLabel(i));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void next(boolean hasNext) {
        if(hasNext) {
            currentResultSet = new HashMap<String, Object>();
            resultSet.add(currentResultSet);
        }
    }

    @Override
    public void addResultValue(Object key, Object value) {
        currentResultSet.put(key.toString(), value);
    }

    @Override
    public void setParent(StackInfo stackInfo) {
        this.parent = stackInfo;
    }

    @Override
    public StackInfo getParent() {
        return parent;
    }

    @Override
    public void appendChild(StackInfo childStackInfo) {
        // Do Nothing
    }

    @Override
    public List<StackInfo> getChild() {
        return null;
    }

    public Map<Object, Object> getParams() {
        return params;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public List<Map<String, Object>> getResultSet() {
        return resultSet;
    }
}
