
public class Authorization extends AbstractMessage{

    boolean isAuthorized;

    public Authorization(boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
    }
}
