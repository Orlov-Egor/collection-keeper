package client.utility;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Class for outputting something to user.
 */
public class OutputerUI {
    private static final String INFO_DEFAULT_TITLE = "Collection Keeper";
    private static final String ERROR_DEFAULT_TITLE = "Collection Keeper";

    /**
     * Prints toOut.toString() + \n to user.
     *
     * @param title Title of alert.
     * @param toOut Object to print.
     */
    public static void info(String title, Object toOut) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(toOut.toString());

        alert.showAndWait();
    }

    /**
     * Prints toOut.toString() + \n to user.
     *
     * @param toOut Object to print.
     */
    public static void info(Object toOut) {
        info(INFO_DEFAULT_TITLE, toOut);
    }

    /**
     * Prints error: toOut.toString() to user.
     *
     * @param title Title of alert.
     * @param toOut Error to print.
     */
    public static void error(String title, Object toOut) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(toOut.toString());

        alert.showAndWait();
    }

    /**
     * Prints error: toOut.toString() to user.
     *
     * @param toOut Error to print.
     */
    public static void error(Object toOut) {
        error(ERROR_DEFAULT_TITLE, toOut);
    }

    /**
     * Prints error if object starts with 'error: ' or info in other case.
     *
     * @param title Title of alert.
     * @param toOut Message to print.
     */
    public static void tryError(String title, Object toOut) {
        if (toOut.toString().startsWith("error: "))
            error(title, toOut.toString().substring(7));
        else info(title, toOut.toString());
    }

    /**
     * Prints error if object starts with 'error: ' or info in other case.
     *
     * @param toOut Message to print.
     */
    public static void tryError(Object toOut) {
        if (toOut.toString().startsWith("error: "))
            error(ERROR_DEFAULT_TITLE, toOut.toString().substring(7));
        else info(INFO_DEFAULT_TITLE, toOut.toString());
    }
}
