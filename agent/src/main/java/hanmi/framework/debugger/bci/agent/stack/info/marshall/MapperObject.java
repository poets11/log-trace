package hanmi.framework.debugger.bci.agent.stack.info.marshall;

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
        String name = type.getName();
        name = name.substring(name.lastIndexOf(".") + 1);

        return "(" + name + ") " + value;
    }

    public static class EmptyObject implements StringObject {
        @Override
        public Class getType() {
            return null;
        }

        @Override
        public String getValue() {
            return null;
        }

        @Override
        public String toString() {
            return null;
        }

        public static StringObject getInstance() {
            return new EmptyObject();
        }
    }
}
