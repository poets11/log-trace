package fury.marvel.trinity.transformer;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.util.Arrays;

/**
 * Created by poets11 on 15. 2. 13..
 */
public class TargetMethod {
    private String name;
    private String[] paramTypeNames;

    public TargetMethod() {

    }

    public TargetMethod(String name, String[] paramTypeNames) {
        this.name = name;
        this.paramTypeNames = paramTypeNames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getParamTypeNames() {
        return paramTypeNames;
    }

    public void setParamTypeNames(String[] paramTypeNames) {
        this.paramTypeNames = paramTypeNames;
    }

    public boolean isEqualCtMethod(CtMethod method) throws NotFoundException {
        if (method == null) return false;
        if (method.getName().equals(name) == false) return false;
        
        CtClass[] parameterTypes = method.getParameterTypes();
        if(paramTypeNames != null && parameterTypes != null) {
            if(paramTypeNames.length != parameterTypes.length) return false;

            for (int i = 0; i < paramTypeNames.length; i++) {
                String expectedTypeName = paramTypeNames[i];
                String realTypeName     = parameterTypes[i].getName();
                
                if(!expectedTypeName.equals(realTypeName)) return false;
            }
        }
        
        if(paramTypeNames == null) {
            if(parameterTypes != null && parameterTypes.length != 0) return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "TargetMethod{" +
                "name='" + name + '\'' +
                ", paramTypeNames=" + Arrays.toString(paramTypeNames) +
                '}';
    }
}
