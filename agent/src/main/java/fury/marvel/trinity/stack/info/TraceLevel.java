package fury.marvel.trinity.stack.info;

/**
 * Created by poets11 on 15. 2. 22..
 */
public enum TraceLevel {
    ALL(0), TIMEOUT(10), EXCEPTION(20), NONE(100);
    private int value;
    private TraceLevel(int value) {
        this.value = value;
    }
}
