package fury.marvel.shiva.stack.info.impl;


import fury.marvel.shiva.stack.info.ResultSetStackInfo;
import fury.marvel.shiva.stack.info.SqlStackInfo;
import fury.marvel.shiva.stack.info.marshall.DefaultMarshaller;
import fury.marvel.shiva.stack.info.marshall.StringObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by poets11 on 15. 2. 4..
 */
public class SqlStackInfoImpl extends AbstractStackInfo implements SqlStackInfo {
    private String sql;
    private List<StringObject> params;
    private StringObject result;
    private ResultSetStackInfoImpl resultSet;

    @Override
    public String getSql() {
        return sql;
    }

    @Override
    public List<StringObject> getParams() {
        return params;
    }

    @Override
    public StringObject getResult() {
        return result;
    }

    @Override
    public ResultSetStackInfo getResultSetStackInfo() {
        return resultSet;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void addParam(Object object) {
        if (params == null) params = new ArrayList<StringObject>();

        params.add(DefaultMarshaller.marshall(object));
    }

    public void setResult(Object result) {
        if (result != null && result instanceof java.sql.ResultSet) {
            result = DefaultMarshaller.marshall(result, true);
            initResultSet((java.sql.ResultSet) result);
        } else {
            result = DefaultMarshaller.marshall(result);
        }
    }

    public void initResultSet(java.sql.ResultSet rs) {
        resultSet = new ResultSetStackInfoImpl();
        resultSet.initColumnNames(rs);
    }

    public void setResultSet(ResultSetStackInfoImpl resultSet) {
        this.resultSet = resultSet;
    }

    @Override
    public String toString() {
        return "SqlStackInfoImpl{" +
                "sql='" + sql + '\'' +
                ", params=" + params +
                '}';
    }
}
