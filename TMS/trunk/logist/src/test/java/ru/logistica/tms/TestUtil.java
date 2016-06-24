package ru.logistica.tms;

import ru.logistica.tms.dao.ScriptRunner;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestUtil {




    public static void splitSqlFile() throws URISyntaxException, IOException {
        Path pathToSql = Paths.get(TestUtil.class.getResource("ddl.sql").toURI());
        String sqlFileAsString = new String(Files.readAllBytes(pathToSql), StandardCharsets.UTF_8);
        Pattern patternStr = Pattern.compile("--.*\\n");
        Matcher matcherStr = patternStr.matcher(sqlFileAsString);
        StringBuffer sb = new StringBuffer();
        int endIndex = 0;
        while (matcherStr.find()){
            matcherStr.appendReplacement(sb, "");
            endIndex = matcherStr.start();
        }
        sb.append(sqlFileAsString.substring(endIndex));
        String sqlFileAsStringWithoutComments = sb.toString();
//        System.out.println(sqlFileAsStringWithoutComments);

//        System.out.println(sqlFileAsString);
        int index = 0;
        Pattern pattern = Pattern.compile(";\\s*\\n*(DROP|CREATE|INSERT|SELECT|REVOKE|GRANT)");
        Matcher matcher = pattern.matcher(sqlFileAsStringWithoutComments);
        List<Integer> indexes = new ArrayList<>();
        indexes.add(index);
        while (matcher.find()){
            index = matcher.start() + 1;
            indexes.add(index);
        }
        for(int i = 0; i < indexes.size() - 1; i++){
//            System.out.println(sqlFileAsStringWithoutComments.substring(indexes.get(i), indexes.get(i + 1)));
//            System.out.println("_______________________________________________");
        }

        System.out.println(indexes);
    }



















    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
        return connection;
    }
    public static void jdbcRecreateDb() throws SQLException, URISyntaxException, IOException, ClassNotFoundException {
        ScriptRunner scriptRunner = new ScriptRunner(getConnection(), true, true);
        Path pathToSql = Paths.get(TestUtil.class.getResource("ddl.sql").toURI());
        scriptRunner.setErrorLogWriter(new PrintWriter(new OutputStreamWriter(System.out)));
        scriptRunner.runScript(new FileReader(pathToSql.toFile()));
    }

    public static void recreateDatabase() throws URISyntaxException {
        executeFiles(false, "ddl.sql");
    }
    public static void fillWithSampleData() throws URISyntaxException {
        executeFiles(true, "test_inserts.sql");
    }
    public static void recreateDatabaseAndFillWithSampleData() {
        executeFiles(false, "ddl.sql", "test_inserts.sql");
    }
    private static String executeCommand(String command) {

        StringBuilder output = new StringBuilder();

        Process p;
        try {
            String[] paths = System.getenv("Path").split(";");

            String postgresPath = null;
            for (String path : paths) {
                if (path.contains("PostgreSQL")) {
                    postgresPath = path;
                    break;
                }
            }
            Objects.requireNonNull(postgresPath,"add psql.exe location into Path variable and restart Intellige Idea");

            p = Runtime.getRuntime().exec(command, null, new File(postgresPath));

            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine())!= null) {
                output.append(line).append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();
    }


    // TODO FIX IT!
    private static void executeFiles(boolean showOutput, String... fileNames) {

        for (String fileName : fileNames) {
            try {
                Path pathToSql = Paths.get(TestUtil.class.getResource(fileName).toURI());
                executeCommand("cmd /c taskkill -f /IM psql.exe");
                String output = executeCommand("cmd /c psql.exe -U postgres -d postgres -h localhost -f " + pathToSql);

                if (showOutput) System.out.println(output);
            }
            catch (Exception err) {
                err.printStackTrace();
            }
        }
    }
}
