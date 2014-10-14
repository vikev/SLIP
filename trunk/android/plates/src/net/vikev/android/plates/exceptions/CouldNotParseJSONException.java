package net.vikev.android.plates.exceptions;

public class CouldNotParseJSONException extends PlatesException {
    private static final long serialVersionUID = 3818989987577541686L;

    public CouldNotParseJSONException() {
        super();
    }

    public CouldNotParseJSONException(String detailMessage) {
        super(detailMessage);
    }
}
