package net.vikev.android.plates.exceptions;

public class CouldNotGetScalesException extends PlatesException {
    private static final long serialVersionUID = 9151586786689207079L;

    public CouldNotGetScalesException() {
        super();
    }

    public CouldNotGetScalesException(String detailMessage) {
        super(detailMessage);
    }
}
