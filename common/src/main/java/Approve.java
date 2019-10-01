
public class Approve extends AbstractMessage{

    public String isAuthorizated() {
        return isAuthorizated;
    }

    String isAuthorizated;

    public Approve(String isAuthorizated) {
        this.isAuthorizated = isAuthorizated;
    }

}
