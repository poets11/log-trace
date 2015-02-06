package fury.marvel.shiva.stack.info.impl;

import fury.marvel.shiva.stack.info.ResultSetStackInfo;
import fury.marvel.shiva.stack.info.marshall.DefaultMarshaller;
import fury.marvel.shiva.stack.info.marshall.StringObject;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by poets11 on 15. 2. 4..
 */
public class ResultSetStackInfoImpl implements ResultSetStackInfo {
    private List<String> columnNames;
    private List<Map<String, StringObject>> datas;

    @Override
    public List<String> getColumnNames() {
        return columnNames;
    }

    @Override
    public List<Map<String, StringObject>> getDatas() {
        return datas;
    }

    public void initColumnNames(java.sql.ResultSet rs) {
        columnNames = new ArrayList<String>();

        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                columnNames.add(metaData.getColumnLabel(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void next(boolean hasNext) {
        if (hasNext == false) return;

        if (datas == null) datas = new ArrayList<Map<String, StringObject>>();

        HashMap<String, StringObject> row = new HashMap<String, StringObject>();
        datas.add(row);
    }

    public void addResultSetValue(String columnNam, Object value) {
        Map<String, StringObject> currentRow = datas.get(datas.size() - 1);
        currentRow.put(columnNam, DefaultMarshaller.marshall(value));
    }

    @Override
    public String toString() {
        return "ResultSetStackInfoImpl{" +
                "columnNames=" + columnNames +
                ", datas=" + datas +
                '}';
    }
}
