package fury.marvel.trinity.writer;


/**
 * Created by poets11 on 15. 2. 6..
 */
public class WriterFactory {
    public static Writer getWriter() {
        return new ConsoleWriter();
    }
}
