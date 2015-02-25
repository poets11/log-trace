package fury.marvel.trinity.writer.converter.simple;

import fury.marvel.trinity.stack.info.RequestStackInfo;
import fury.marvel.trinity.stack.info.StackInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by poets11 on 15. 2. 12..
 */
public class SimpleRequestInfoConverter extends SimpleConverter {
    private final String lineUrl = "%s %s (%s) - %s\n";
    private final String lineThreadId = "%s ThreadId : %s, ElapsedTime : %sms\n";

    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd hh:mm:sss");
    @Override
    public String convert(StackInfo stackInfo) {
        if(stackInfo == null) return "";

        RequestStackInfo reqStackInfo = (RequestStackInfo) stackInfo;
        StringBuilder builder = new StringBuilder();
        String indentString = getIndentString(reqStackInfo.getDepth());

        builder.append(String.format(lineUrl, indentString, reqStackInfo.getUrl(), reqStackInfo.getMethod(), format.format(new Date(reqStackInfo.getStartTime()))));
        builder.append(String.format(lineThreadId, indentString, reqStackInfo.getThreadId(), reqStackInfo.getElapsedTime()));

        Map<String, String> headers = reqStackInfo.getHeaders();
        if(headers != null) {
            builder.append(String.format(lineNew, indentString));
            builder.append(String.format(lineTitle, indentString, "Headers"));

            Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                builder.append(String.format(lineKeyValue, indentString, entry.getKey(), entry.getValue()));
            }
        }

        Map<String, List<String>> params = reqStackInfo.getParams();
        if(params != null) {
            builder.append(String.format(lineNew, indentString));
            builder.append(String.format(lineTitle, indentString, "Params"));

            Iterator<Map.Entry<String, List<String>>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<String>> entry = iterator.next();
                builder.append(String.format(lineKeyValue, indentString, entry.getKey(), entry.getValue()));
            }
        }

        return builder.toString();
    }
}
