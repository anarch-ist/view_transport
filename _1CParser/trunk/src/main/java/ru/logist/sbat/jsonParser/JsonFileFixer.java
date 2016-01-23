package ru.logist.sbat.jsonParser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class JsonFileFixer {

    public static void fix(Path path) throws IOException {

        String jsonFileAsString = new String(Files.readAllBytes(path));
        String fixed = jsonFileAsString.replaceAll("(\\s)*([a-zA-Z0-9_]+?):", "\"$2\":");
        FileUtils.write(path.toFile(), fixed, StandardCharsets.UTF_8);



    }
}
