package ru.logist.sbat.db;

import ru.logist.sbat.db.transactionParts.DBCohesionException;
import ru.logist.sbat.db.transactionParts.Selects;
import ru.logist.sbat.db.transactionParts.TransactionPart;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class TransactionExecutor extends TreeMap<Integer, TransactionPart> {
    private List<Statement> statements = new ArrayList<>();
    private Connection connection;

    private TransactionExecutor(Comparator<? super Integer> comparator) {
        super(comparator);
    }

    public TransactionExecutor() {
        this(Integer::compareTo);
    }

    public void executeAll() throws SQLException, DBCohesionException {

        for (Map.Entry<Integer, TransactionPart> entry: this.entrySet()) {
            TransactionPart transactionPart = entry.getValue();
            transactionPart.setConnection(connection);
            Statement statement = transactionPart.executePart();
            statements.add(statement);
        }
    }

    public void closeAll() {
        this.statements.forEach(DBUtils::closeStatementQuietly);
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
        Selects.setConnection(connection);
    }
}
