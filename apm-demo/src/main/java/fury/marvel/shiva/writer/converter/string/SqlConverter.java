package fury.marvel.shiva.writer.converter.string;

import fury.marvel.shiva.trace.stack.StackInfo;
import fury.marvel.shiva.trace.stack.sql.SqlStackInfoImpl;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by poets11 on 15. 2. 2..
 */
public class SqlConverter implements IndentConverter {
    @Override
    public void convert(StackInfo stackInfo, Writer writer, int depth) throws IOException {
        SqlStackInfoImpl info = (SqlStackInfoImpl) stackInfo;

        writer.write(IndentConverterFactory.getIndentString(depth));
        writer.write("Execute SQL");
        writer.write("\n");

        writer.write(IndentConverterFactory.getIndentString(depth));
        writer.write("Elapsed Time : " + info.getElapsedTime());
        writer.write("\n");

        writer.write(IndentConverterFactory.getIndentString(depth));
        writer.write(info.getSql());
        writer.write("\n");

        writer.write(IndentConverterFactory.getIndentString(depth));
        writer.write("Parameters");
        writer.write("\n");
        Map<Object, Object> params = info.getParams();
        if (params != null) {
            Iterator<Map.Entry<Object, Object>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, Object> entry = iterator.next();
                writer.write(IndentConverterFactory.getIndentString(depth));
                writer.write("Param " + entry.getKey() + " : " + entry.getValue());
                writer.write("\n");
            }
        }

        writer.write(IndentConverterFactory.getIndentString(depth));
        writer.write("Result Set");
        writer.write("\n");
        List<Map<String, Object>> resultSet = info.getResultSet();
        if (resultSet != null) {
            writer.write(IndentConverterFactory.getIndentString(depth));
            List<String> columnNames = info.getColumnNames();
            for (int i = 0; i < columnNames.size(); i++) {
                writer.write(columnNames.get(i) + "\t");
            }
            writer.write("\n");

            for (int i = 0; i < resultSet.size(); i++) {
                Map<String, Object> rs = resultSet.get(i);
                if (rs != null) {
                    writer.write(IndentConverterFactory.getIndentString(depth));
                    for (int j = 0; j < columnNames.size(); j++) {
                        String name = columnNames.get(j);
                        String value = rs.get(name) == null ? "" : rs.get(name).toString();
                        writer.write(value + "\t");
                    }
                    writer.write("\n");
                }
            }
        }
    }
}
