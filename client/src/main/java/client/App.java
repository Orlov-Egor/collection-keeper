package client;

import client.controllers.AskWindowController;
import client.controllers.LoginWindowController;
import client.controllers.MainWindowController;
import client.controllers.tools.ObservableResourceFactory;
import common.exceptions.NotInDeclaredLimitsException;
import common.exceptions.WrongAmountOfElementsException;
import common.utility.Outputer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
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

    private static final String APP_TITLE = "Collection Keeper";

    private static String host;
    private static int port;
    private static Scanner userScanner;
    private static Client client;

    private Stage primaryStage;

    public static void main(String[] args) {
        if (initialize(args)) launch(args);
        else System.exit(0);
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
    public void start(Stage stage) {
        try {
            this.primaryStage = stage;

            FXMLLoader loginWindowLoader = new FXMLLoader();
            loginWindowLoader.setLocation(getClass().getResource("/view/LoginWindow.fxml"));
            Parent loginWindowRootNode = loginWindowLoader.load();
            Scene loginWindowScene = new Scene(loginWindowRootNode);
            LoginWindowController loginWindowController = loginWindowLoader.getController();
            loginWindowController.setApp(this);
            loginWindowController.setClient(client);
            loginWindowController.initializeConnection();

            primaryStage.setTitle(APP_TITLE);

            primaryStage.setScene(loginWindowScene);
            primaryStage.setResizable(false);
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
        client = new Client(host, port);

        client.run();
    }

    @Override
    public void stop() {
        client.stop();
        userScanner.close();
    }

    public void setMainWindow() {
        try {
            ObservableResourceFactory resourceFactory = new ObservableResourceFactory();

            FXMLLoader mainWindowLoader = new FXMLLoader();
            mainWindowLoader.setLocation(getClass().getResource("/view/MainWindow.fxml"));
            Parent mainWindowRootNode = mainWindowLoader.load();
            Scene mainWindowScene = new Scene(mainWindowRootNode);
            MainWindowController mainWindowController = mainWindowLoader.getController();
            mainWindowController.initLangs(resourceFactory);

            FXMLLoader askWindowLoader = new FXMLLoader();
            askWindowLoader.setLocation(getClass().getResource("/view/AskWindow.fxml"));
            Parent askWindowRootNode = askWindowLoader.load();
            Scene askWindowScene = new Scene(askWindowRootNode);
            Stage askStage = new Stage();
            askStage.setTitle(APP_TITLE);
            askStage.setScene(askWindowScene);
            askStage.setResizable(false);
            askStage.initModality(Modality.WINDOW_MODAL);
            askStage.initOwner(primaryStage);
            AskWindowController askWindowController = askWindowLoader.getController();
            askWindowController.setAskStage(askStage);
            askWindowController.setResourceFactory(resourceFactory);

            mainWindowController.setClient(client);
            mainWindowController.setAskStage(askStage);
            mainWindowController.setPrimaryStage(primaryStage);
            mainWindowController.setAskWindowController(askWindowController);
            mainWindowController.refreshButtonOnAction();

            primaryStage.setScene(mainWindowScene);
            primaryStage.setMinWidth(mainWindowScene.getWidth());
            primaryStage.setMinHeight(mainWindowScene.getHeight());
            primaryStage.setResizable(true);
        } catch (Exception exception) {
            // TODO: Обработать ошибки
            System.out.println(exception);
            exception.printStackTrace();
        }
    }
}
