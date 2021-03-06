package ru.logist.sbat.testUtils;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonFileFixer {

//    public static void main(String[] args) throws URISyntaxException, IOException {
//        URL resource = JSONReadFromFileTest.class.getResource("EKA_first.pkg");
//        Path path = Paths.get(resource.toURI());
//        fix(path);
//    }

    public static void fix(Path path) throws IOException {

        StringBuilder builder = new StringBuilder();
        try (BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8)){
            int symbol = -10;
            while (symbol != -1) {
                symbol = bufferedReader.read();
                builder.append((char) symbol);
            }
        }

        String jsonFileAsString = builder.toString();
        Pattern pattern = Pattern.compile("\".*\":\\s+\"(.*\"+.*)\"|\\,\\n", Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(jsonFileAsString);
        ArrayList<Integer> indexes = new ArrayList<>();

        while (matcher.find()) {
            String forReplace = matcher.group(1);
            char[] chars = forReplace.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '"'){
                    indexes.add(matcher.start(1) + i);
                }
            }
        }

        String fileAsString = new String(Files.readAllBytes(path));
        StringBuilder sb = new StringBuilder(fileAsString);

        int offset = 0;
        for (Integer index : indexes) {
            sb.deleteCharAt(index - offset);
            offset++;
        }

        FileUtils.writeStringToFile(path.getParent().resolve("EKA_fixed.pkg").toFile(),  sb.toString(), StandardCharsets.UTF_8);
    }
}
