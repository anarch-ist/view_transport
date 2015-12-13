package ru.logist.sbat.cmd;

import java.util.Arrays;
import java.util.List;

public class Option {
    private String name;
    private boolean isNoArgs;
    // parameter and corresponding type
    private List<String> parameters;

    public Option(String name, String... parameters) {
        this.name = name;
        this.isNoArgs = false;
        this.parameters = Arrays.asList(parameters);
    }

    public Option(String name) {
        this.name = name;
        this.isNoArgs = true;
    }

    public String getName() {
        return name;
    }

    public boolean isNoArgs() {
        return isNoArgs;
    }

    public List<String> getParameters() {
        return parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Option option = (Option) o;

        if (isNoArgs != option.isNoArgs) return false;
        if (name != null ? !name.equals(option.name) : option.name != null) return false;
        return !(parameters != null ? !parameters.equals(option.parameters) : option.parameters != null);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (isNoArgs ? 1 : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name + ": " + ((parameters == null) ? "no parameters" : "parameters = " + parameters.toString());
    }
}
