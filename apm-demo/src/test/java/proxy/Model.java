package proxy;

/**
 * Created by poets11 on 15. 1. 30..
 */
public class Model {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        System.out.println("model.setName()");
        this.name = name;
    }
}
