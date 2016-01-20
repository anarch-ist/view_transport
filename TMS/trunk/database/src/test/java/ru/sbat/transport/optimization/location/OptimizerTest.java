package ru.sbat.transport.optimization.location;


import org.junit.Test;
import ru.sbat.transport.optimization.Optimizer;

import java.util.Date;

public class OptimizerTest {

    @Test
    public void getWDay(){
        int result = 0;
        Optimizer optimizer = new Optimizer();
        Date date = new Date();
        result = optimizer.getWeekDay(date);
        System.out.println(result);
    }
}
