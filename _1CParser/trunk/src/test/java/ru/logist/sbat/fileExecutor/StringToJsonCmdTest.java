package ru.logist.sbat.fileExecutor;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StringToJsonCmdTest {

    @BeforeMethod
    public void setUp() throws Exception {

    }

    @Test
    public void testFixString() throws Exception {
        String string = read(this.getClass().getResource("LOG.pkg").openStream());
        StringToJsonCmd stringToJsonCmd = new StringToJsonCmd(string);
        String fixedString = stringToJsonCmd.fixString(string);
        System.out.println(fixedString);
        stringToJsonCmd.fixString(fixedString);
    }

    public static String read(InputStream input) throws IOException {
        byte[] bytes = IOUtils.toByteArray(input);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}