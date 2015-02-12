package fury.marvel.trinity.stack.info.marshall;

/**
 * Created by poets11 on 15. 2. 5..
 */
public class MapperObject implements StringObject {
    private Class type;
    private String value;

    @Override
    public Class getType() {
        return type;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MapperObject{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}
