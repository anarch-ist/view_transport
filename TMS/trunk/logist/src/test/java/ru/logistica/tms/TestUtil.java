package ru.logistica.tms;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class TestUtil {
    public static void recreateDatabase() throws URISyntaxException {
        executeFiles(false, "ddl.sql");
    }
    public static void fillWithSampleData() throws URISyntaxException {
        executeFiles(false, "test_inserts.sql");
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

    private static void executeFiles(boolean showOutput, String... fileNames) {
        executeCommand("cmd /c taskkill -f /IM psql.exe");
        for (String fileName : fileNames) {
            try {
                Path pathToSql = Paths.get(TestUtil.class.getResource(fileName).toURI());
                String output = executeCommand("cmd /c psql.exe -U postgres -d postgres -h localhost -f " + pathToSql);
                if (showOutput) System.out.println(output);
            }
            catch (Exception err) {
                err.printStackTrace();
            }
        }
    }
}
