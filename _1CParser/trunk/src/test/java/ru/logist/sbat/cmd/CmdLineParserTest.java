package ru.logist.sbat.cmd;


import org.testng.*;
import org.testng.annotations.Test;
import java.util.List;

public class CmdLineParserTest {

    @Test
    public void testSplit() throws Exception {
        CmdLineParser cmdLineParser = new CmdLineParser(new Options());
        List<String> stringList = cmdLineParser.split("wedwe ewdwe");
        Assert.assertEquals(stringList.toArray(), new String[]{"wedwe", "ewdwe"});
        stringList = cmdLineParser.split("wedwe");
        Assert.assertEquals(stringList.toArray(), new String[]{"wedwe"});
        stringList = cmdLineParser.split("str1 \"st  r2\"");
        Assert.assertEquals(stringList.toArray(), new String[]{"str1", "st  r2"});
        stringList = cmdLineParser.split("rollback -date \"12.11.12 11:11:11\"");
        Assert.assertEquals(stringList.toArray(), new String[]{"rollback", "-date", "12.11.12 11:11:11"});
    }
}