package com.sgsoft.servicer.db.exception;

/**
 * Created by Viktor Rotar on 04.04.14.
 */
public class DBException extends Exception {

    public DBException()
    {
        super();
    }

    public DBException(Throwable cause)
    {
        super(cause);
    }

    public DBException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
