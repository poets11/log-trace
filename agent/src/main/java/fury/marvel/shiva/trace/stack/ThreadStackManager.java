package fury.marvel.shiva.trace.stack;


import fury.marvel.shiva.trace.stack.init.RequestStackInfo;
import fury.marvel.shiva.trace.stack.sql.SqlStackInfo;
import fury.marvel.shiva.writer.ConsoleWriter;

/**
 * Created by poets11 on 15. 1. 28..
 */
public class ThreadStackManager {
    private static ThreadLocal<StackInfo> root = new ThreadLocal<StackInfo>();
    private static ThreadLocal<StackInfo> current = new ThreadLocal<StackInfo>();
    private static ThreadLocal<StackInfo> sql = new ThreadLocal<StackInfo>();

    public static void init() {
        root.set(null);
        current.set(null);
        sql.set(null);
    }

    public static void push(StackInfo stackInfo) {
        StackInfo rootStack = getRoot();
        StackInfo currentStack = getCurrent();
        SqlStackInfo currentSqlStack = getSql() == null ? null : (SqlStackInfo) getSql();

        boolean sqlStackInfoParam = stackInfo instanceof SqlStackInfo;

        if (rootStack == null) {
            root.set(stackInfo);
            current.set(stackInfo);

            if (sqlStackInfoParam) {
                sql.set(stackInfo);
            }
        } else {
            if (sqlStackInfoParam && currentSqlStack != null) {
                String paramSql = ((SqlStackInfo) stackInfo).getSql();
                String currSql = currentSqlStack.getSql();

                if (paramSql.equals(currSql)) {
                    return;
                } else {
                    sql.set(stackInfo);
                }
            }

            if (sqlStackInfoParam) {
                sql.set(stackInfo);
            }

            currentStack.appendChild(stackInfo);
            stackInfo.setParent(currentStack);
            current.set(stackInfo);
        }
    }

    public static void pop(StackInfo stackInfo) {
        StackInfo rootStack = getRoot();
        StackInfo parent = stackInfo.getParent();

        if (parent == null) {
            if (rootStack instanceof RequestStackInfo) {
                new ConsoleWriter().write(rootStack);
            }
            init();
        } else {
            current.set(parent);
        }
    }

    public static StackInfo getRoot() {
        return root.get();
    }

    public static StackInfo getCurrent() {
        return current.get();
    }

    public static StackInfo getSql() {
        return sql.get();
    }
}
