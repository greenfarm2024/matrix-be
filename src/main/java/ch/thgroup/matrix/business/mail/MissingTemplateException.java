package ch.thgroup.matrix.business.mail;

public class MissingTemplateException extends RuntimeException {
    public MissingTemplateException(String message) {
        super(message);
    }
}
