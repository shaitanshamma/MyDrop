
public class Approve extends AbstractMessage{

    String isAuthorizated;

    public String isAuthorizated() {
        return isAuthorizated;
    }

    public Approve(String isAuthorizated) {
        this.isAuthorizated = isAuthorizated;
    }

}
