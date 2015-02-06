package fury.marvel.shiva.stack;


import fury.marvel.shiva.stack.info.SqlStackInfo;
import fury.marvel.shiva.stack.info.StackInfo;
import fury.marvel.shiva.stack.info.impl.AbstractStackInfo;
import fury.marvel.shiva.writer.Writer;
import fury.marvel.shiva.writer.WriterFactory;

import java.util.Stack;

/**
 * Created by poets11 on 15. 1. 28..
 */
public class StackManager {
    private static ThreadLocal<Stack<StackInfo>> threadStack = new ThreadLocal<Stack<StackInfo>>();
    private static ThreadLocal<Stack<SqlStackInfo>> threadSqlStack = new ThreadLocal<Stack<SqlStackInfo>>();

    private static Stack<StackInfo> getStack() {
        Stack<StackInfo> stack = threadStack.get();

        if (stack == null) {
            stack = new Stack<StackInfo>();
            threadStack.set(stack);
        }

        return stack;
    }

    private static Stack<SqlStackInfo> getSqlStack() {
        Stack<SqlStackInfo> sqlStack = threadSqlStack.get();

        if (sqlStack == null) {
            sqlStack = new Stack<SqlStackInfo>();
            threadSqlStack.set(sqlStack);
        }

        return sqlStack;
    }

    public static void push(StackInfo stackInfo) {
        ((AbstractStackInfo) stackInfo).setStartTime(System.currentTimeMillis());

        Stack<StackInfo> stack = getStack();

        if (stack.empty() == false) {
            StackInfo parent = stack.peek();
            parent.appendChild(stackInfo);
        }

        stack.push(stackInfo);

        postPush(stackInfo);
    }

    public static void pop(StackInfo stackInfo) {
        ((AbstractStackInfo) stackInfo).setEndTime(System.currentTimeMillis());

        Stack<StackInfo> stack = getStack();
        if (stack.empty()) throw new IllegalStateException("stack is empty");

        StackInfo top = stack.peek();
        if (stackInfo.equals(top) == false) throw new IllegalStateException("current stack is different. top : "
                + top.getClass().getName() + " / pop : " + stackInfo.getClass().getName());

        stack.pop();

        postPop(stackInfo);
    }

    private static void postPush(StackInfo stackInfo) {
        if (stackInfo instanceof SqlStackInfo) {
            Stack<SqlStackInfo> sqlStack = getSqlStack();
            sqlStack.push((SqlStackInfo) stackInfo);
        }
    }

    private static void postPop(StackInfo stackInfo) {
        StackInfo parent = peek();

        if (parent == null) {
            Writer writer = WriterFactory.getInstance();
            writer.write(stackInfo);

            getStack().clear();
        }
    }

    public static void popSql(SqlStackInfo sqlStackInfo) {
        Stack<SqlStackInfo> sqlStack = getSqlStack();
        if (sqlStack.empty()) throw new IllegalStateException("sql stack is empty");

        SqlStackInfo top = sqlStack.peek();
        if (sqlStackInfo.equals(top) == false) {
            throw new IllegalStateException("current sql stack is different. top : "
                    + top.hashCode() + " / pop : " + sqlStackInfo.hashCode());
        }

        sqlStack.pop();
    }

    public static SqlStackInfo peekSql() {
        Stack<SqlStackInfo> sqlStack = getSqlStack();
        SqlStackInfo sqlStackInfo = null;

        if (sqlStack.empty() == false) sqlStackInfo = sqlStack.peek();

        return sqlStackInfo;
    }

    public static StackInfo peek() {
        Stack<StackInfo> stack = getStack();

        if (stack.empty()) return null;
        else return stack.peek();
    }
}
