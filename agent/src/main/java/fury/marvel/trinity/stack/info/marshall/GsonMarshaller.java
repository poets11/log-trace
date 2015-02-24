package fury.marvel.trinity.stack.info.marshall;

import com.google.gson.Gson;

/**
 * Created by poets11 on 15. 2. 24..
 */
public class GsonMarshaller implements Marshaller {
    private Gson gson;

    public GsonMarshaller() {
        gson = new Gson();
    }

    @Override
    public StringObject marshall(Object obj) {
//        MapperObject stringObject = new MapperObject();
//        if (obj == null) return stringObject;
//
//        stringObject.setType(obj.getClass());
//        stringObject.setValue(gson.toJson(obj));
//        
//        return stringObject;
        return marshall(obj, true);
    }

    @Override
    public StringObject marshall(Object obj, boolean useToString) {
        MapperObject stringObject = new MapperObject();
        if (obj == null) return stringObject;

        stringObject.setType(obj.getClass());
        if(useToString) stringObject.setValue(obj.toString());
        else stringObject.setValue(gson.toJson(obj));

        return stringObject;
    }
}
