package client.utility;

import common.interaction.Request;
import common.interaction.User;

import java.io.Serializable;

/**
 * Handle user login and password.
 */
public class UserHandler {
    /**
     * Handle user authentication.
     *
     * @return Request of user.
     */
    public static Request handle(String commandName, String commandStringArgument,
                                 Serializable commandObjectArgument, User user) {
        // TODO: Написать это
        return new Request(commandName, commandStringArgument, commandObjectArgument, user);
    }
}
