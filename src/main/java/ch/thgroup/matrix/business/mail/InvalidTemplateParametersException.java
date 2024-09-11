package ch.thgroup.matrix.business.mail;

public class InvalidTemplateParametersException extends RuntimeException {
    public InvalidTemplateParametersException(String message, IllegalArgumentException e) {
        super(message, e);
    }
}
