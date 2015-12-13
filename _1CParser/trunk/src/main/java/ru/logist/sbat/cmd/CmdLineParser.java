package ru.logist.sbat.cmd;

import javafx.util.Pair;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class CmdLineParser {
    private Options options;

    public CmdLineParser(Options options) {
        this.options = options;
    }

    /**
     *
     * @param line, must not be empty or null
     * @return Option and parameters
     * @throws ParseException
     */
    public Pair<Option, List<String>> parse(String line) throws ParseException {

        String[] strings = line.split("\\s");
        String optionName = strings[0];
        try {
            Option option = options.stream()
                    .filter(opt -> opt.getName().equals(optionName))
                    .findFirst()
                    .get();
            if (option.isNoArgs()) {
                return new Pair<>(option, null);
            } else {
                List<String> params = new ArrayList<>();
                for (int i = 1; i < strings.length; i++) {
                    params.add(strings[i]);
                    if (!option.getParameters().contains(strings[i]))
                        throw new ParseException("option argument [" + strings[i] + "] does not exist.", 0);
                }
                return new Pair<>(option, params);
            }

        } catch (NoSuchElementException e) {
            throw new ParseException("option name [" + optionName + "] does not exist.", 0);
        }

    }
}
