package net.vikev.android.plates.exceptions;

public class UnsuportedOperationException extends PlatesException {
    private static final long serialVersionUID = -958837652594309170L;

    public UnsuportedOperationException() {
        super();
    }

    public UnsuportedOperationException(String detailMessage) {
        super(detailMessage);
    }
}
