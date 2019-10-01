public class ClientConnection extends AbstractMessage {
    String login;

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    String pass;

    public ClientConnection(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

}
