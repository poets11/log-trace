package fury.marvel.trinity.writer.converter.simple;

import fury.marvel.trinity.stack.info.ResultSetStackInfo;
import fury.marvel.trinity.stack.info.SqlStackInfo;
import fury.marvel.trinity.stack.info.StackInfo;
import fury.marvel.trinity.stack.info.marshall.StringObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

/**
 * Created by poets11 on 15. 2. 12..
 */
public class SimpleSqlInfoConverter extends SimpleConverter {
    private final String lineTitleMs = "%s %s - %sms\n";

    @Override
    public String convert(StackInfo stackInfo) {
        if (stackInfo == null) return "";

        SqlStackInfo sqlStackInfo = (SqlStackInfo) stackInfo;
        StringBuilder builder = new StringBuilder();
        String indentString = getIndentString(sqlStackInfo.getDepth());

        builder.append(String.format(lineNew, indentString));
        builder.append(String.format(lineTitleMs, indentString, "Execute SQL", sqlStackInfo.getElapsedTime()));
        builder.append(String.format(lineTitle, "", formatSql(indentString, sqlStackInfo.getSql())));

        List<StringObject> params = sqlStackInfo.getParams();
        if (params != null && params.size() > 0) {
            builder.append(String.format(lineKeyValue, indentString, "Params", params));
        }

        ResultSetStackInfo resultSetStackInfo = sqlStackInfo.getResultSetStackInfo();
        if (resultSetStackInfo != null) {
            List<Map<String, StringObject>> datas = resultSetStackInfo.getDatas();
            builder.append(String.format(lineKeyValue, indentString, "ResultSet", datas));
        }

        return builder.toString();
    }
    
    private String formatSql(String indentString, String sql) {
        try {
            StringBuilder builder = new StringBuilder();
            String indent = getEmptyIndent(indentString.length());
    
            StringReader reader = new StringReader(sql);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if(line.length() == 0) continue;
                
                builder.append(indent);
                builder.append(line);
                builder.append("\n");
            }
            
            return builder.toString();
        } catch (IOException e) {
            return sql;
        }
    }
    
    private String getEmptyIndent(int length) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append(" ");
        }
        
        return builder.toString();
    }
}
