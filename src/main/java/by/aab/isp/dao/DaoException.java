package by.aab.isp.dao;

public class DaoException extends RuntimeException {

    private static final long serialVersionUID = 2584484380309959443L;

    public DaoException() {
        super();
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }

}
