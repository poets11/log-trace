package fury.marvel.trinity.writer.converter.simple;

import fury.marvel.trinity.stack.info.ResultSetStackInfo;
import fury.marvel.trinity.stack.info.SqlStackInfo;
import fury.marvel.trinity.stack.info.StackInfo;
import fury.marvel.trinity.stack.info.marshall.StringObject;

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
        builder.append(String.format(lineTitle, indentString, sqlStackInfo.getSql()));

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
}
