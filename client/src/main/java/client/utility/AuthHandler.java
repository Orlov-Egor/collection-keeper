package client.utility;

import common.interaction.Request;
import common.interaction.User;

/**
 * Handle user login and password.
 */
public class AuthHandler {
    private static final String loginCommand = "login";
    private static final String registerCommand = "register";

    /**
     * Handle user authentication.
     *
     * @return Request of user.
     */
    public static Request handle(String username, String password, boolean register) {
        String command = register ? registerCommand : loginCommand;
        User user = new User(username, password);
        return new Request(command, "", user);
    }
}
