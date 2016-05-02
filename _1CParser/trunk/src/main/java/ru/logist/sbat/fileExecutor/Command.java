package ru.logist.sbat.fileExecutor;

public interface Command<T> {
    T execute() throws CommandException;
}
