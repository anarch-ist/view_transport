package ru.logist.sbat.cmd;

import java.util.ArrayList;

public class Options extends ArrayList<Option>{
    @Override
    public String toString() {
        String result="";
        for(Option option: this ) {
            result+=option + System.getProperty("line.separator");
        }
        return result;
    }
}
