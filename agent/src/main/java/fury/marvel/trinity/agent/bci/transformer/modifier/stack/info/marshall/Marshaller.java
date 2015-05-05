package fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.marshall;

/**
 * Created by poets11 on 15. 2. 24..
 */
public interface Marshaller {
    StringObject marshall(Object object);

    StringObject marshall(Object object, boolean useToString);
}
