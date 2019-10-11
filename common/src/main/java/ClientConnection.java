public class ClientConnection extends AbstractMessage {
    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public ClientConnection(String login, String pass) {
        this.login = login;
        this.password = pass;
    }

}
