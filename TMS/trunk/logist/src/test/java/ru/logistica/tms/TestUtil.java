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

    public static void cleanDatabase(boolean showOutput) throws URISyntaxException {
        Path pathToSql = Paths.get(TestUtil.class.getResource("ddl.sql").toURI());
        try {
            executeCommand("cmd /c taskkill -f /IM psql.exe");
            String output = executeCommand("cmd /c psql.exe -U postgres -d postgres -h localhost -f " + pathToSql);
            if (showOutput) System.out.println(output);
        }
        catch (Exception err) {
            err.printStackTrace();
        }
        System.out.println("DATABASE RECREATED");
    }

    public static Connection createConnection() throws SQLException {
        String url      = "jdbc:postgresql://localhost/postgres?stringtype=unspecified";  //database specific url.
        String user     = "postgres";
        String password = "postgres";

        return DriverManager.getConnection(url, user, password);
    }
}
