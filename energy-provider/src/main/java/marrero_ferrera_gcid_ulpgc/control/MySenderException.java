package marrero_ferrera_gcid_ulpgc.control;

public class MySenderException extends Throwable {
    public MySenderException(String message) {
        super(message);
    }
    public MySenderException(MySenderException message) {
        super(message);
    }
}
