package ru.logist.sbat.db.transactionParts;

import ru.logist.sbat.db.DBUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class TransactionExecutor extends TreeMap<Integer, TransactionPart> {
    private List<PreparedStatement> preparedStatements = new ArrayList<>();

    private List<PreparedStatement> getPreparedStatements() {
        return preparedStatements;
    }

    public void setPreparedStatements(List<PreparedStatement> preparedStatements) {
        this.preparedStatements = preparedStatements;
    }

    private TransactionExecutor(Comparator<? super Integer> comparator) {
        super(comparator);
    }

    public TransactionExecutor() {
        this(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
    }

    public void executeAll() throws SQLException {
        this.entrySet();
        for (Map.Entry<Integer, TransactionPart> entry: this.entrySet()) {
            PreparedStatement preparedStatement = entry.getValue().executePart();
            preparedStatements.add(preparedStatement);
        }
    }

    public void close() {
        this.getPreparedStatements().forEach(DBUtils::closeStatementQuietly);
    }
}
