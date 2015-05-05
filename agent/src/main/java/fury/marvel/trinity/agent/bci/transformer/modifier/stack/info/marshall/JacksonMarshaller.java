package fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.marshall;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;

/**
 * Created by poets11 on 15. 2. 5..
 */
@Deprecated
public class JacksonMarshaller implements Marshaller {
    private ObjectMapper mapper = new ObjectMapper();

    public JacksonMarshaller() {
        VisibilityChecker<?> visibilityChecker = mapper.getSerializationConfig().getDefaultVisibilityChecker();
        mapper.setVisibilityChecker(visibilityChecker
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public StringObject marshall(Object obj, boolean useToString) {
        if (obj == null) {
            MapperObject nullObj = new MapperObject();
            return nullObj;
        }

        try {
            MapperObject object = new MapperObject();

            object.setType(obj.getClass());

            if (useToString) object.setValue(obj.toString());
            else object.setValue(mapper.writeValueAsString(obj));

            return object;
        } catch (Exception e) {
            return marshall(obj, true);
        }
    }

    @Override
    public StringObject marshall(Object obj) {
        return marshall(obj, false);
    }
}
