package fury.marvel.shiva.trace;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by poets11 on 15. 1. 30..
 */
public class ObjectConverter {
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setVisibilityChecker(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static Object convert(Object obj) {
        try {
            if(obj == null) return null;

            String asString = mapper.writeValueAsString(obj);

            if(obj.getClass().getName().startsWith("ces")) {
                return mapper.readValue(asString, HashMap.class);
            } else {
                return asString;
            }
        } catch (Exception e) {
            return obj.toString();
        }
    }
}
