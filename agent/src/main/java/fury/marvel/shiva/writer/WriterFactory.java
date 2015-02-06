package fury.marvel.shiva.writer;


import fury.marvel.shiva.writer.converter.SimpleConverter;

/**
 * Created by poets11 on 15. 2. 6..
 */
public class WriterFactory {
    public static Writer getInstance() {
        ConsoleWriter consoleWriter = new ConsoleWriter();
        consoleWriter.setConverter(new SimpleConverter());

        return consoleWriter;
    }
}
