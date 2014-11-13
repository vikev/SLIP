package net.vikev.android.plates.exceptions;

public class CouldNotTronsformJsonToItemException extends PlatesException {
    private static final long serialVersionUID = 2318618442973275312L;

    public CouldNotTronsformJsonToItemException() {
        super();
    }

    public CouldNotTronsformJsonToItemException(String detailMessage) {
        super(detailMessage);
    }
}
