package net.vikev.android.plates.exceptions;

public class CouldNotReachWebServiceException extends PlatesException {
    private static final long serialVersionUID = 3818989987577541686L;

    public CouldNotReachWebServiceException() {
        super();
    }

    public CouldNotReachWebServiceException(String detailMessage) {
        super(detailMessage);
    }
}
