package ru.logist.sbat;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.cmd.CmdLineParser;
import ru.logist.sbat.cmd.Option;
import ru.logist.sbat.cmd.Options;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final Logger logger = LogManager.getLogger(App.class);
    public static final String IRT = "IRT"; // insert into request table
    public static final String IRLT = "IRLT"; // insert into routeListTable
    public static final String IIT = "IIT"; // insert into invoices table
    public static final String UIT = "UIT"; // update invoice statuses

    public static void main(String[] args) {

        Controller controller = new Controller();
        Scanner scanner = new Scanner(System.in);
        Options options = new Options();
        Option exit = new Option("exit");
        Option start = new Option("start");
        Option help = new Option("help");
        Option setActions = new Option("setActions", IRT, IRLT, IIT, UIT);
        options.addAll(Arrays.asList(exit, start, setActions, help));

        CmdLineParser cmdLineParser = new CmdLineParser(options);

        while (true) {
            String nextLine = scanner.nextLine();
            Pair<Option, List<String>> optionListPair;
            try {
                optionListPair = cmdLineParser.parse(nextLine);
            } catch (ParseException e) {
                logger.debug(e.getMessage());
                continue;
            }
            Option option = optionListPair.getKey();
            List<String> parameters = optionListPair.getValue();

            if (option.equals(exit)) {
                controller.close();
                System.exit(0);
            } else if (option.equals(start)) {
                controller.startGeneration();
            } else if (option.equals(setActions)) {
                controller.setGenerateInsertIntoRequestTable(parameters.contains(IRT));
                controller.setGenerateInsertIntoRouteListsTable(parameters.contains(IRLT));
                controller.setGenerateInsertIntoInvoicesTable(parameters.contains(IIT));
                controller.setGenerateUpdateInvoiceStatuses(parameters.contains(UIT));
            } else if (option.equals(help)) {
                System.out.println(options);
            }

        }

    }
}


