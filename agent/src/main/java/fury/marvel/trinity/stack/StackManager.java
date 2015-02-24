package fury.marvel.trinity.stack;


import fury.marvel.trinity.stack.info.SqlStackInfo;
import fury.marvel.trinity.stack.info.StackInfo;
import fury.marvel.trinity.stack.info.impl.AbstractStackInfo;
import fury.marvel.trinity.writer.Writer;
import fury.marvel.trinity.writer.WriterFactory;

import java.util.Stack;

/**
 * Created by poets11 on 15. 1. 28..
 */
public class StackManager {
    private static ThreadLocal<Stack<StackInfo>> threadStack;
    private static ThreadLocal<Stack<SqlStackInfo>> threadSqlStack;

    public static boolean isInitialized() {
        return threadStack != null && threadSqlStack != null;
    }

    public static void init() {
        System.out.println("// TODO StackManager.init()");

        threadStack = new ThreadLocal<Stack<StackInfo>>();
        threadSqlStack = new ThreadLocal<Stack<SqlStackInfo>>();

        threadStack.set(new Stack<StackInfo>());
        threadSqlStack.set(new Stack<SqlStackInfo>());
    }

    public static void clear() {
        System.out.println("// TODO StackManager.clear()");

        if (isInitialized()) {
            Stack<StackInfo> stack = getStack();
            stack.clear();

            Stack<SqlStackInfo> sqlStack = getSqlStack();
            sqlStack.clear();

            threadStack.remove();
            threadSqlStack.remove();

            threadStack = null;
            threadSqlStack = null;
        }
    }

    public static int size() {
        if (isInitialized() == false) {
            System.out.println("// TODO Exception in StackManager.size() : StackManager가 초기화 되어있지 않음.");
            return 0;
        }

        return getStack().size();
    }

    public static void push(StackInfo stackInfo) {
        if (isInitialized() == false) {
            System.out.println("// TODO Exception in StackManager.push() : StackManager가 초기화 되어있지 않음.");
            return;
        }

        Stack<StackInfo> stack = getStack();

        if (stack.empty() == false) {
            StackInfo parent = stack.peek();
            parent.appendChild(stackInfo);
        }

        ((AbstractStackInfo) stackInfo).setStartTime(System.currentTimeMillis());
        ((AbstractStackInfo) stackInfo).setDepth(stack.size());

        stack.push(stackInfo);

        postPush(stackInfo);
    }

    public static void pop(StackInfo stackInfo) {
        if (isInitialized() == false) {
            System.out.println("// TODO Exception in StackManager.pop() : StackManager가 초기화 되어있지 않음.");
            return;
        }

        ((AbstractStackInfo) stackInfo).setEndTime(System.currentTimeMillis());

        Stack<StackInfo> stack = getStack();
        if (stack.empty()) {
            System.out.println("// TODO Exception in StackManager.pop() : 스택이 비어있어서 POP 불가.");
            return;
        }

        StackInfo top = stack.peek();
        if (top != stackInfo) {
            System.out.println("// TODO Exception in StackManager.pop() : POP 대상이 실제와 다름.");
            return;
        }

        stack.pop();

        postPop(stackInfo);
    }

    public static StackInfo peek() {
        if (isInitialized() == false) {
            System.out.println("// TODO Exception in StackManager.peek() : StackManager가 초기화 되어있지 않음.");
            return null;
        }

        Stack<StackInfo> sqlStack = getStack();
        return sqlStack.peek();
    }

    public static void popSql(SqlStackInfo sqlStackInfo) {
        if (isInitialized() == false) {
            System.out.println("// TODO Exception in StackManager.popSql() : StackManager가 초기화 되어있지 않음.");
            return;
        }

        Stack<SqlStackInfo> sqlStack = getSqlStack();
        if (sqlStack.empty()) {
            System.out.println("// TODO Exception in StackManager.popSql() : 스택이 비어있어서 POP 불가.");
            return;
        }

        SqlStackInfo top = sqlStack.peek();
        if (top != sqlStackInfo) {
            System.out.println("// TODO Exception in StackManager.pop() : POP 대상이 실제와 다름.");
            return;
        }

        sqlStack.pop();
    }

    public static SqlStackInfo peekSql() {
        if (isInitialized() == false) {
            System.out.println("// TODO Exception in StackManager.peekSql() : StackManager가 초기화 되어있지 않음.");
            return null;
        }

        Stack<SqlStackInfo> sqlStack = getSqlStack();
        return sqlStack.peek();
    }

    public static void catchException(Exception e) {
        if (isInitialized() == false) {
            System.out.println("// TODO Exception in StackManager.catchException() : StackManager가 초기화 되어있지 않음.");
            return;
        }

        Stack<StackInfo> stack = getStack();

        AbstractStackInfo top = (AbstractStackInfo) stack.peek();
        top.setException(e);

        popAllStackInfo();
    }

    private static Stack<StackInfo> getStack() {
        if (threadStack == null) return null;
        else return threadStack.get();
    }

    private static Stack<SqlStackInfo> getSqlStack() {
        if (threadSqlStack == null) return null;
        else return threadSqlStack.get();
    }

    private static void postPush(StackInfo stackInfo) {
        if (stackInfo instanceof SqlStackInfo) {
            Stack<SqlStackInfo> sqlStack = getSqlStack();
            sqlStack.push((SqlStackInfo) stackInfo);
        }
    }

    private static void postPop(StackInfo stackInfo) {
        Stack<StackInfo> stack = getStack();

        if (stack.empty()) {
            Writer writer = WriterFactory.getWriter();
            writer.write(stackInfo);
        }
    }

    private static void popAllStackInfo() {
        Stack<StackInfo> stack = getStack();

        while (stack.empty() == false) {
            AbstractStackInfo top = (AbstractStackInfo) stack.peek();
            pop(top);
        }
    }
}
