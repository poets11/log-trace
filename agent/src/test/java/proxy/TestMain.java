package proxy;

import javassist.*;

import java.io.IOException;

/**
 * Created by poets11 on 15. 1. 30..
 */
public class TestMain {
    public static void main(String[] args) throws IOException, CannotCompileException, IllegalAccessException, InstantiationException {
        ClassPool classPool = ClassPool.getDefault();
        CtClass aClass = classPool.makeClass(Model.class.getClassLoader().getResourceAsStream("proxy/Model.class"));
        CtClass innerModel = aClass.makeNestedClass("InnerModel", true);
        innerModel.setSuperclass(aClass);

        CtField field = CtField.make("private proxy.Model model = new proxy.Model();", innerModel);
        innerModel.addField(field);

        CtMethod method = CtMethod.make("public void setName(String name) { System.out.println(\"inner.setName()\"); this.model.setName(name); }", innerModel);
        innerModel.addMethod(method);

        Class toClass = innerModel.toClass();
        Model instance = (Model) toClass.newInstance();
        instance.setName("hello");

        System.out.println("complete.");
    }
}
