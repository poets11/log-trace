package fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.writer.converter.simple;

import fury.marvel.trinity.agent.AgentConfig;
import fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.impl.PackageStackInfoImpl;
import fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.impl.RequestStackInfoImpl;
import fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.impl.SqlStackInfoImpl;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class SimpleConverterTest {
    
    @Test
    public void testConvert() throws IOException {
        AgentConfig.init(null);
        
        SimpleConverter simpleConverter = new SimpleConverter();

        PackageStackInfoImpl packageStackInfo = new PackageStackInfoImpl();
        packageStackInfo.setClassName(getClass().getName());
        packageStackInfo.setMethodName("testConvert");
        packageStackInfo.setParams(null);
        packageStackInfo.setResult("success");
        packageStackInfo.setStartTime(System.currentTimeMillis());
        packageStackInfo.setEndTime(System.currentTimeMillis() + 10l);
        packageStackInfo.setElapsedTime(10);
        packageStackInfo.setDepth(0);
        assertEquals("|--> " + packageStackInfo.toString(), simpleConverter.convert(packageStackInfo));


        RequestStackInfoImpl requestStackInfo = new RequestStackInfoImpl();
        requestStackInfo.setStartTime(System.currentTimeMillis());
        requestStackInfo.setEndTime(System.currentTimeMillis() + 10l);
        requestStackInfo.setElapsedTime(10);
        requestStackInfo.setDepth(0);
        assertEquals("|--> " + requestStackInfo.toString(), simpleConverter.convert(requestStackInfo));


        SqlStackInfoImpl sqlStackInfo = new SqlStackInfoImpl();
        sqlStackInfo.setStartTime(System.currentTimeMillis());
        sqlStackInfo.setEndTime(System.currentTimeMillis() + 10l);
        sqlStackInfo.setElapsedTime(10);
        sqlStackInfo.setDepth(0);
        sqlStackInfo.setSql("select * from table");
        assertEquals("|--> " + sqlStackInfo.toString(), simpleConverter.convert(sqlStackInfo));
    }

}