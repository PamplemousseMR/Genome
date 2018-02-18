package Download;

public class HTTPException extends Exception {
    private int statusCode;

    public HTTPException(int statusCode) {
        this.statusCode = statusCode;
    }
    
    public String getMessage()
    {
        return " Request returned status code : " + this.statusCode;
    }
}
