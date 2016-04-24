package ru.logist.sbat.cmd;

import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public Pair<Option, Map<String, String>> parse(String line) throws ParseException {

        List<String> strings = split(line);
        if (strings.size() == 0)
            throw new ParseException("empty string is not argument", 0);

        String optionName = strings.get(0);
        try {
            Option option = options.getOptionByName(optionName);
            if (option.isNoArgs()) {
                return new Pair<>(option, null);
            }

            Map<String, String> params = new HashMap<>();

            for (int i = 1; i < strings.size(); i+=2) {
                if (!option.getParameters().contains(strings.get(i)))
                    throw new ParseException("option argument [" + strings.get(i) + "] does not exist.", 0);
                if (i + 1 >= strings.size())
                    throw new ParseException("no argument for parameter[" + strings.get(i) + "]", 0);

                params.put(strings.get(i), strings.get(i + 1));
            }
            return new Pair<>(option, params);

        } catch (NoSuchElementException e) {
            throw new ParseException("option name [" + optionName + "] does not exist.", 0);
        }

    }

    protected List<String> split(String string) {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|[^\\s]+");
        Matcher m = pattern.matcher(string);
        while (m.find()) {
            String group = m.group();
            group = group.replaceAll("\"", "");
            result.add(group);
        }
        return result;
    }
}
