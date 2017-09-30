package fi.mika.domain.shared;

public class TechnicalErrorException extends Exception {
    public TechnicalErrorException(String message) {
        super(message);
    }

    public TechnicalErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
