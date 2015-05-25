package hanmi.framework.debugger.bci.agent.config;

/**
 * Created by poets11 on 15. 2. 22..
 */
public enum TraceLevel {
    DEBUG(0), INFO(10), WARN(30), ERROR(40), NONE(100);
    private int value;
    private TraceLevel(int value) {
        this.value = value;
    }
}
