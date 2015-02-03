package fury.marvel.shiva.writer.converter.string;

import fury.marvel.shiva.trace.ObjectConverter;
import fury.marvel.shiva.trace.stack.StackInfo;
import fury.marvel.shiva.trace.stack.init.RequestStackInfo;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by poets11 on 15. 2. 2..
 */
public class RequestConverter implements IndentConverter {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:SSS");

    @Override
    public void convert(StackInfo stackInfo, Writer writer, int depth) throws IOException {
        RequestStackInfo info = (RequestStackInfo) stackInfo;

        writer.write("URL : " + info.getUrl());
        writer.write("\n");
        writer.write("Thread Id : " + info.getThreadId());
        writer.write("\n");
        writer.write("Start Time : " + parseDate(info.getStart()));
        writer.write("\n");
        writer.write("End Time : " + parseDate(info.getEnd()));
        writer.write("\n");
        writer.write("Elapsed Time : " + info.getElapsedTime() + "ms");
        writer.write("\n");
        writer.write("Method : " + info.getMethod());
        writer.write("\n");
        writer.write("IP : " + info.getRemoteUser());
        writer.write("\n");
        writer.write("ContentType : " + info.getContentType());
        writer.write("\n");
        writer.write("Device Type : " + info.getDeviceInfo());
        writer.write("\n");
        writer.write("View File Path : " + info.getViewPath());
        writer.write("\n");

        writer.write("Request Headers ------------------------------------------------");
        writer.write("\n");
        Map<String, String> headers = info.getHeaders();
        if (headers != null) {
            Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                writer.write(entry.getKey() + " : " + entry.getValue());
                writer.write("\n");
            }
        }

        writer.write("Request Parameters ---------------------------------------------");
        writer.write("\n");
        Map<String, String> params = info.getParams();
        if (params != null) {
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                writer.write(entry.getKey() + " : " + entry.getValue());
                writer.write("\n");
            }
        }

        writer.write("Model Datas ----------------------------------------------------");
        writer.write("\n");
        Map<String, Object> model = info.getModel();
        if (model != null) {
            Iterator<Map.Entry<String, Object>> iterator = model.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                writer.write(entry.getKey() + " : " + ObjectConverter.convert(entry.getValue()));
                writer.write("\n");
            }
        }

        writer.write("Call Stack ----------------------------------------------------");
        writer.write("\n");
    }

    private String parseDate(long time) {
        return format.format(new Date(time));
    }


}
