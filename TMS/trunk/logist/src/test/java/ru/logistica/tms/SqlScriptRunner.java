package ru.logistica.tms;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlScriptRunner {

    private final Connection connection;
    private final Path filePath;
    private final boolean showOutput;

    public SqlScriptRunner(Connection connection, Path filePath, boolean showOutput) {
        this.connection = connection;
        this.filePath = filePath;
        this.showOutput = showOutput;
    }

    public void run() throws IOException, SQLException {
        List<String> sqlStatements = splitSqlScript();
        for (String sqlStatement : sqlStatements) {
            Statement statement = connection.createStatement();
            statement.execute(sqlStatement);
            statement.close();
            if (showOutput) {
                System.out.println(sqlStatement + "\n");
            }
        }
    }

    private List<String> splitSqlScript() throws IOException {
        List<String> result = new ArrayList<>();
        String sqlFileAsString = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
        Pattern patternStr = Pattern.compile("--.*\\n");
        Matcher matcherStr = patternStr.matcher(sqlFileAsString);
        StringBuffer sb = new StringBuffer();
        while (matcherStr.find()){
            matcherStr.appendReplacement(sb, "");
        }
        matcherStr.appendTail(sb);
        String sqlFileAsStringWithoutComments = sb.toString();

        int index = 0;
        Pattern pattern = Pattern.compile(";\\s*\\n*(DROP|CREATE|INSERT|SELECT|REVOKE|GRANT|SET|TRUNCATE|ALTER)");
        Matcher matcher = pattern.matcher(sqlFileAsStringWithoutComments);
        List<Integer> indexes = new ArrayList<>();
        indexes.add(index);
        while (matcher.find()){
            index = matcher.start() + 1;
            indexes.add(index);
        }
        for(int i = 0; i < indexes.size() - 1; i++){
            result.add(sqlFileAsStringWithoutComments.substring(indexes.get(i), indexes.get(i + 1)));
        }
        return result;
    }

}
