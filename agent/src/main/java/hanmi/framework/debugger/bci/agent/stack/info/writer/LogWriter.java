package hanmi.framework.debugger.bci.agent.stack.info.writer;

import hanmi.framework.debugger.bci.agent.assist.CtClassUtil;
import hanmi.framework.debugger.bci.agent.config.AgentConfig;
import hanmi.framework.debugger.bci.agent.config.TraceLevel;
import hanmi.framework.debugger.bci.agent.stack.info.*;
import hanmi.framework.debugger.bci.agent.stack.info.marshall.StringObject;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by poets11 on 15. 5. 20..
 */
public class LogWriter implements StackInfoWriter {
    private static final String LOGGER_NAME = "hanmi.framework.FrameworkDebugger";
    private static final String LOGGER_FACTORY = "org.slf4j.LoggerFactory";
    private static final String GET_LOGGER = "getLogger";

    private TraceLevel currentLevel = TraceLevel.DEBUG;

    private Object loggerInstance;
    private StringBuilder buffer;
    private String currentSql;
    private int currentDepth;

    public LogWriter() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ClassLoader latestUrlClassLoader = CtClassUtil.getInstance().getClassPool().getClassLoader();
        Class<?> loggerFactory = latestUrlClassLoader.loadClass(LOGGER_FACTORY);
        Method getLoggerMethod = loggerFactory.getMethod(GET_LOGGER, new Class[]{String.class});

        loggerInstance = getLoggerMethod.invoke(loggerFactory, LOGGER_NAME);
        buffer = new StringBuilder();
    }

    private boolean isEnabled(String level) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method enabled = loggerInstance.getClass().getMethod("is" + level + "Enabled", null);
        return (Boolean) enabled.invoke(loggerInstance, null);
    }

    @Override
    public void write(Stack<StackInfo> currentStack) {
        write(currentStack, false);
    }

    @Override
    public void write(Stack<StackInfo> currentStack, boolean hasException) {
        try {
            if (isValidateStack(currentStack) == false) {
                return;
            }

            StackInfo stackInfo = currentStack.pop();
            initCurrentLevel(hasException, stackInfo);

            List<StackInfo> innerStacks = stackInfo.getInnerStacks();
            if (innerStacks == null || innerStacks.size() == 0) {
                return;
            }

            doWrite(stackInfo, 0);
            writeLog();

        } catch (Exception e) {
            AgentConfig.error(e);
        }
    }

    protected void initCurrentLevel(boolean hasException, StackInfo stackInfo) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (hasException) {
            currentLevel = TraceLevel.ERROR;
        } else if (isWarnLevel(stackInfo)) {
            currentLevel = TraceLevel.WARN;
        } else if (isEnabled("Debug") == false) {
            currentLevel = TraceLevel.INFO;
        }
    }

    protected boolean isWarnLevel(StackInfo stackInfo) {
        long elapsedTime = stackInfo.getElapsedTime();
        long conditionTimeout = AgentConfig.getConditionTimeout();

        AgentConfig.log(String.format("EndTime(%s) - StartTime(%s) = ElapsedTime(%s) / Condition Time Out(%s)",
                stackInfo.getEndTime(), stackInfo.getStartTime(), stackInfo.getElapsedTime(), AgentConfig.getConditionTimeout()));

        return elapsedTime >= conditionTimeout;
    }

    protected boolean isValidateStack(Stack<StackInfo> currentStack) {
        if (currentStack == null || currentStack.size() != 1) {
            if (AgentConfig.isPrintAgentLog()) {
                AgentConfig.log("currentStack is wrong.");
            }

            return false;
        }
        return true;
    }

    private void writeLog() {
        try {
            Method info = loggerInstance.getClass().getMethod(currentLevel.name().toLowerCase(), new Class[]{String.class});
            info.invoke(loggerInstance, buffer.toString());

        } catch (Exception e) {
            AgentConfig.error(e);
        }

    }

    private void doWrite(StackInfo stackInfo, int depth) {
        if (stackInfo instanceof HandlerAdapterStackInfo) {
            doWriteHandlerAdapterStackInfo(stackInfo, depth);
        } else if (stackInfo instanceof PackageStackInfo) {
            doWritePackageStackInfo(stackInfo, depth);
        } else if (stackInfo instanceof ConnectionStackInfo) {
            doWriteConnectionStackInfo(stackInfo, depth);
        } else if (stackInfo instanceof PreparedStatementStackInfo) {
            doWritePreparedStatementStackInfo(stackInfo, depth);
        } else if (stackInfo instanceof ResultSetStackInfo) {
            doWriteResultSetStackInfo(stackInfo, depth);
        } else if (stackInfo instanceof ExceptionStackInfo) {
            doWriteExceptionStackInfo(stackInfo, depth);
        } else {
            doWriteDefaultStackInfo(stackInfo, depth);
        }
    }

    private void doWriteExceptionStackInfo(StackInfo stackInfo, int depth) {
        ExceptionStackInfo info = (ExceptionStackInfo) stackInfo;

        Exception exception = info.getException();

        if (exception != null) {
            try {

                fillIndent(depth);

                String className = stackInfo.getClassName();
                className = className.substring(className.lastIndexOf(".") + 1);
                buffer.append(className + "." + stackInfo.getMethodName() + "();\n");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintWriter writer = new PrintWriter(baos);
                exception.printStackTrace(writer);
                writer.flush();

                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                InputStreamReader reader = new InputStreamReader(bais);
                BufferedReader bufferedReader = new BufferedReader(reader);

                StringBuilder sb = new StringBuilder();
                String line = bufferedReader.readLine();
                while (line != null) {
                    line = line.trim();

                    if (line.isEmpty() == false) {
                        fillIndent(sb, depth, "  ");
                        sb.append(line);
                        sb.append("\n");
                    }

                    line = bufferedReader.readLine();
                }

                buffer.append(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void doWriteResultSetStackInfo(StackInfo stackInfo, int depth) {
        ResultSetStackInfo info = (ResultSetStackInfo) stackInfo;

        if (currentLevel != TraceLevel.INFO) {
            if (info.getMethodName().equals("next")) {
                if (info.hasNext()) {
                    fillIndent(depth);
                    buffer.append("[SQL - READ ROW] - " + info.getElapsedTime() + "ms\n");
                }
            } else {
                fillIndent(depth);
                String methodName = info.getMethodName().substring(3);
                buffer.append("[SQL - GET " + methodName + "] " + info.getColumnName() + " : " + info.getValue() + " - " + info.getElapsedTime() + "ms\n");
            }
        }

        List<StackInfo> innerStacks = info.getInnerStacks();
        doWriteInnerStacks(depth + 1, innerStacks);
    }

    private void doWritePreparedStatementStackInfo(StackInfo stackInfo, int depth) {
        PreparedStatementStackInfo info = (PreparedStatementStackInfo) stackInfo;

        String methodName = info.getMethodName();

        if (currentLevel == TraceLevel.INFO) {
            boolean execute = methodName.startsWith("execute");
            if (execute) {
                fillIndent(currentDepth);
                buffer.append(currentSql);
                buffer.append("\n");
            } else {
                if (currentSql != null) {
                    StringObject param = info.getParam();
                    Class paramType = param.getType();

                    if (Number.class.isAssignableFrom(paramType)) {
                        currentSql = currentSql.replaceFirst("\\?", param.getValue());
                    } else if (Date.class.isAssignableFrom(paramType)) {
                        currentSql = currentSql.replaceFirst("\\?", param.getValue());
                    } else {
                        currentSql = currentSql.replaceFirst("\\?", "'" + param.getValue() + "'");
                    }
                }
            }
        } else {
            fillIndent(depth);

            boolean execute = methodName.startsWith("execute");
            if (execute) {
                buffer.append("[SQL - EXCUTE] - " + info.getElapsedTime() + "ms\n");
            } else {
                methodName = methodName.substring(3);
                buffer.append("[SQL - SET " + methodName + "] - " + info.getIndex() + " : " + info.getParam() + " - " + info.getElapsedTime() + "ms\n");
            }
        }

        List<StackInfo> innerStacks = info.getInnerStacks();
        doWriteInnerStacks(depth + 1, innerStacks);
    }

    private void doWriteConnectionStackInfo(StackInfo stackInfo, int depth) {
        ConnectionStackInfo info = (ConnectionStackInfo) stackInfo;

        if (currentLevel == TraceLevel.INFO) {
            currentSql = info.getSql();
            currentDepth = depth;
        } else {
            fillIndent(depth);
            buffer.append("[SQL - INIT] " + info.getSql() + " - " + info.getElapsedTime() + "ms\n");
        }

        List<StackInfo> innerStacks = info.getInnerStacks();
        doWriteInnerStacks(depth + 1, innerStacks);
    }

    private void doWriteHandlerAdapterStackInfo(StackInfo stackInfo, int depth) {
        HandlerAdapterStackInfo info = (HandlerAdapterStackInfo) stackInfo;

        fillIndent(depth, "|");

        if (currentLevel == TraceLevel.INFO) {
            buffer.append(info.getUrl() + " (" + info.getMethod() + ")\n");

            if (info.getViewName() != null) {
                fillIndent(depth, "|");
                buffer.append(info.getViewName() + "\n");
            }
        } else {
            buffer.append(info.getUrl() + " (" + info.getMethod() + ") - " + info.getThreadId() + " - " + info.getElapsedTime() + "ms\n");

            Map<String, String> headers = info.getHeaders();
            if (headers != null) {
                fillIndent(depth, "|\n");
                fillIndent(depth, "|");
                buffer.append("-- Request Headers\n");

                Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();

                    String key = entry.getKey();
                    String value = entry.getValue();
                    fillIndent(depth, "|");
                    buffer.append(key + " : " + value + "\n");
                }
            }

            Map<String, List<String>> params = info.getParams();
            if (params != null) {
                fillIndent(depth, "|\n");
                fillIndent(depth, "|");
                buffer.append("-- Request Params\n");

                Iterator<Map.Entry<String, List<String>>> iterator = params.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, List<String>> entry = iterator.next();

                    String key = entry.getKey();
                    fillIndent(depth, "|");
                    buffer.append(key + " : ");

                    List<String> values = entry.getValue();
                    for (int i = 0; i < values.size(); i++) {
                        buffer.append(values.get(i));
                        if ((i + 1) < values.size()) {
                            buffer.append(", ");
                        }
                    }

                    buffer.append("\n");
                }
            }

            Map<String, StringObject> models = info.getModels();
            if (models != null) {
                fillIndent(depth, "|\n");
                fillIndent(depth, "|");
                buffer.append("-- Return Models\n");

                Iterator<Map.Entry<String, StringObject>> iterator = models.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, StringObject> entry = iterator.next();

                    String key = entry.getKey();
                    fillIndent(depth, "|");
                    buffer.append(key + " : ");

                    StringObject value = entry.getValue();
                    buffer.append(value);

                    if (iterator.hasNext()) {
                        buffer.append("\n");
                    }
                }

                buffer.append("\n");
            }

            if (info.getViewName() != null) {
                fillIndent(depth, "|\n");
                fillIndent(depth, "|");
                buffer.append("-- View Path\n");
                fillIndent(depth, "|");
                buffer.append(info.getViewName() + "\n");
            }
        }

        doWriteInnerStacks(depth + 1, stackInfo.getInnerStacks());
    }

    private void doWritePackageStackInfo(StackInfo stackInfo, int depth) {
        PackageStackInfo info = (PackageStackInfo) stackInfo;

        if (currentLevel == TraceLevel.INFO) {
            String className = info.getClassName();

            if (className.indexOf(".model.") < 0) {
                fillIndent(depth);

                className = className.substring(className.lastIndexOf(".") + 1);
                buffer.append(className + "." + info.getMethodName() + "();\n");
            }
        } else {
            fillIndent(depth);
            buffer.append(info.getClassName() + "." + info.getMethodName() + "(); - " + info.getElapsedTime() + "ms\n");

            List<StringObject> argumentStrings = info.getArgumentStringObjects();
            if (argumentStrings != null && argumentStrings.size() > 0) {

                for (int i = 0; i < argumentStrings.size(); i++) {
                    fillIndent(depth, "  P" + (i + 1) + " : ");

                    StringObject argStringObject = argumentStrings.get(i);

                    buffer.append(argStringObject);

                    if ((i + 1) < argumentStrings.size()) {
                        buffer.append("\n");
                    }
                }

                buffer.append("\n");
            }

            StringObject resultValue = info.getResultValue();
            if (resultValue != null) {
                fillIndent(depth, "  R  : ");
                buffer.append(resultValue);
                buffer.append("\n");
            }

        }

        List<StackInfo> innerStacks = info.getInnerStacks();
        doWriteInnerStacks(depth + 1, innerStacks);

    }

    private void doWriteDefaultStackInfo(StackInfo stackInfo, int depth) {
        fillIndent(depth);

        String className = stackInfo.getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);
        buffer.append(className + "." + stackInfo.getMethodName() + "();\n");

        List<StackInfo> innerStacks = stackInfo.getInnerStacks();
        doWriteInnerStacks(depth + 1, innerStacks);

    }

    private void doWriteInnerStacks(int depth, List<StackInfo> innerStacks) {
        if (innerStacks != null) {
            for (int i = 0; i < innerStacks.size(); i++) {
                StackInfo info = innerStacks.get(i);
                doWrite(info, depth);
            }
        }
    }

    private void fillIndent(int depth) {
        fillIndent(buffer, depth, "└ ");
    }

    private void fillIndent(int depth, String suffix) {
        fillIndent(buffer, depth, suffix);
    }

    private void fillIndent(StringBuilder buffer, int depth, String suffix) {
        for (int i = 0; i < depth; i++) {
            buffer.append("  ");
        }

        buffer.append(suffix);
    }
}
