package fury.marvel.shiva.util;

/**
 * Created by poets11 on 15. 2. 2..
 */
public class Debug {
    public static void writeArgsInfo(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];

            if(arg == null) System.out.println("null");
            else System.out.println(arg.toString());
        }
    }
}
