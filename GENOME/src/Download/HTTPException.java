package Download;

public class HTTPException extends Exception {
    private int statusCode;

    public HTTPException(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getMessage()
    {
        return " Request returned status code : " + this.statusCode;
    }
}
