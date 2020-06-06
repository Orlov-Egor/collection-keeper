package client;

import client.utility.AuthHandler;
import client.utility.UserHandler;
import client.view.MainWindowController;
import common.exceptions.NotInDeclaredLimitsException;
import common.exceptions.WrongAmountOfElementsException;
import common.utility.Outputer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Scanner;

/**
 * Main client class. Creates all client instances.
 *
 * @author Sviridov Dmitry and Orlov Egor.
 */
public class App extends Application {
    public static final String PS1 = "$ ";
    public static final String PS2 = "> ";

    // TODO: Убрать переподключение 5 раз (Сделать 1 попытку подключения, потом сразу окно логина, если неудача - еще попытка)
    private static final int RECONNECTION_TIMEOUT = 5 * 1000;
    private static final int MAX_RECONNECTION_ATTEMPTS = 5;
    private static final String APP_TITLE = "Collection Keeper";

    private static String host;
    private static int port;
    private static Scanner userScanner;
    private static AuthHandler authHandler;
    private static UserHandler userHandler;
    private static Client client;

    public static void main(String[] args) {
        if (initialize(args)) launch(args);
    }

    /**
     * Controls initialization.
     */
    private static boolean initialize(String[] args) {
        try {
            if (args.length != 2) throw new WrongAmountOfElementsException();
            host = args[0];
            port = Integer.parseInt(args[1]);
            if (port < 0) throw new NotInDeclaredLimitsException();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            String jarName = new java.io.File(App.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            Outputer.println("Использование: 'java -jar " + jarName + " <host> <port>'");
        } catch (NumberFormatException exception) {
            Outputer.printerror("Порт должен быть представлен числом!");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("Порт не может быть отрицательным!");
        }
        return false;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/MainWindow.fxml"));
            Parent rootNode = loader.load();
            Scene scene = new Scene(rootNode);
            MainWindowController mainWindowController = loader.getController();

            primaryStage.setTitle(APP_TITLE);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception exception) {
            // TODO: Обработать ошибки
            System.out.println(exception);
            exception.printStackTrace();
        }
    }

    @Override
    public void init() {
        userScanner = new Scanner(System.in);
        authHandler = new AuthHandler(userScanner);
        userHandler = new UserHandler(userScanner);
        client = new Client(host, port, RECONNECTION_TIMEOUT, MAX_RECONNECTION_ATTEMPTS, userHandler, authHandler);

        Thread clientThread = new Thread(client);
        clientThread.setDaemon(true);
        clientThread.start();
    }

    @Override
    public void stop() {
        client.stop();
        userScanner.close();
    }
}
