package ru.logistica.tms.dto;

import ru.logistica.tms.util.UtcSimpleDateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DocDateSelectorData {
    public static final SimpleDateFormat dateFormat = new UtcSimpleDateFormat("yyyy-MM-dd");
    public final Date utcDate;
    public final Integer warehouseId;
    public final Integer docId;

    public DocDateSelectorData(String utcDate, String warehouseId, String docId) throws ParseException {
        this.utcDate = dateFormat.parse(utcDate);
        this.warehouseId = Integer.parseInt(warehouseId);
        this.docId = Integer.parseInt(docId);
    }
}
