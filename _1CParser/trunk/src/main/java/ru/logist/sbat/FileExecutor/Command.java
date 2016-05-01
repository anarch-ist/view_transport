package ru.logist.sbat.FileExecutor;

public interface Command<T> {
    T execute() throws CommandException;
}
