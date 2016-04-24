package ru.logist.sbat.cmd;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CmdLineParserTest {

    @Test
    public void testSplit() throws Exception {
        CmdLineParser cmdLineParser = new CmdLineParser(new Options());
        List<String> stringList = cmdLineParser.split("wedwe ewdwe");
        Assert.assertArrayEquals(stringList.toArray(), new String[]{"wedwe", "ewdwe"});
        stringList = cmdLineParser.split("wedwe");
        Assert.assertArrayEquals(stringList.toArray(), new String[]{"wedwe"});
        stringList = cmdLineParser.split("str1 \"st  r2\"");
        Assert.assertArrayEquals(stringList.toArray(), new String[]{"str1", "st  r2"});
        stringList = cmdLineParser.split("rollback -date \"12.11.12 11:11:11\"");
        Assert.assertArrayEquals(stringList.toArray(), new String[]{"rollback", "-date", "12.11.12 11:11:11"});
    }
}