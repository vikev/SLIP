package net.vikev.android.plates.exceptions;

public class PlatesException extends RuntimeException {
    private static final long serialVersionUID = 3152717054871398821L;

    public PlatesException(){
        super();
    }
    
    public PlatesException(String detailMessage) {
        super(detailMessage);
    }
}
