package net.vikev.android.plates.exceptions;

public class CouldNotGetItemException extends PlatesException {
    private static final long serialVersionUID = 3818989987577541686L;

    public CouldNotGetItemException() {
        super();
    }

    public CouldNotGetItemException(String detailMessage) {
        super(detailMessage);
    }
}
