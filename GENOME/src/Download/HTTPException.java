package Download;

public class HTTPException extends Exception {

    /**
     * The status code
     */
    private int statusCode;

    /**
     * Class constructor
     * @param statusCode, the status code
     */
    public HTTPException(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        return " Request returned status code : " + this.statusCode;
    }
}
