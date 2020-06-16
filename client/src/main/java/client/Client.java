package client;

import client.controllers.MainWindowController;
import client.utility.OutputerUI;
import client.utility.ScriptHandler;
import common.data.SpaceMarine;
import common.exceptions.ConnectionErrorException;
import common.exceptions.NotInDeclaredLimitsException;
import common.interaction.Request;
import common.interaction.Response;
import common.interaction.ResponseCode;
import common.interaction.User;
import common.utility.Outputer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.NavigableSet;

/**
 * Runs the client.
 */
public class Client implements Runnable {
    private String host;
    private int port;
    private SocketChannel socketChannel;
    private ObjectOutputStream serverWriter;
    private ObjectInputStream serverReader;
    private User user;
    private boolean isConnected;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            connectToServer();
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("Клиент не может быть запущен!");
            System.exit(0);
        } catch (ConnectionErrorException exception) { /* ? */ }
    }

    public void stop() {
        try {
            processRequestToServer(MainWindowController.EXIT_COMMAND_NAME, "", null);
            socketChannel.close();
            Outputer.println("Работа клиента завершена.");
        } catch (IOException | NullPointerException exception) {
            Outputer.printerror("Произошла ошибка при попытке завершить соединение с сервером!");
            if (socketChannel == null) Outputer.printerror("Невозможно завершить еще не установленное соединение!");
        }
    }

    /**
     * Server request process.
     */
    public NavigableSet<SpaceMarine> processRequestToServer(String commandName, String commandStringArgument,
                                                            Serializable commandObjectArgument) {
        Request requestToServer = null;
        Response serverResponse = null;
        try {
            requestToServer = new Request(commandName, commandStringArgument, commandObjectArgument, user);
            serverWriter.writeObject(requestToServer);
            serverResponse = (Response) serverReader.readObject();
            if (!serverResponse.getResponseBody().isEmpty()) OutputerUI.tryError(serverResponse.getResponseBody());
        } catch (InvalidClassException | NotSerializableException exception) {
            OutputerUI.error("Произошла ошибка при отправке данных на сервер!");
        } catch (ClassNotFoundException exception) {
            OutputerUI.error("Произошла ошибка при чтении полученных данных!");
        } catch (IOException exception) {
            if (requestToServer.getCommandName().equals(MainWindowController.EXIT_COMMAND_NAME)) return null;
            OutputerUI.error("Соединение с сервером разорвано!");
            try {
                connectToServer();
                OutputerUI.info("Соединение с сервером установлено.");
            } catch (ConnectionErrorException | NotInDeclaredLimitsException reconnectionException) {
                OutputerUI.info("Попробуйте повторить команду позднее.");
            }
        }
        return serverResponse == null ? null : serverResponse.getMarinesCollection();
    }

    /**
     * Server script process.
     */
    public boolean processScriptToServer(File scriptFile) {
        Request requestToServer = null;
        Response serverResponse = null;
        ScriptHandler scriptHandler = new ScriptHandler(scriptFile);
        do {
            try {
                requestToServer = serverResponse != null ? scriptHandler.handle(serverResponse.getResponseCode(), user) :
                        scriptHandler.handle(null, user);
                if (requestToServer == null) return false;
                if (requestToServer.isEmpty()) continue;
                serverWriter.writeObject(requestToServer);
                serverResponse = (Response) serverReader.readObject();
                if (!serverResponse.getResponseBody().isEmpty()) OutputerUI.tryError(serverResponse.getResponseBody());
            } catch (InvalidClassException | NotSerializableException exception) {
                OutputerUI.error("Произошла ошибка при отправке данных на сервер!");
            } catch (ClassNotFoundException exception) {
                OutputerUI.error("Произошла ошибка при чтении полученных данных!");
            } catch (IOException exception) {
                Outputer.printerror("Соединение с сервером разорвано!");
                try {
                    connectToServer();
                    OutputerUI.info("Соединение с сервером установлено.");
                } catch (ConnectionErrorException | NotInDeclaredLimitsException reconnectionException) {
                    OutputerUI.info("Попробуйте повторить команду позднее.");
                }
            }
        } while (!requestToServer.getCommandName().equals("exit"));
        return true;
    }

    /**
     * Handle process authentication.
     */
    public boolean processAuthentication(String username, String password, boolean register) {
        // TODO: Переместить все в один метод (?)
        Request requestToServer = null;
        Response serverResponse = null;
        String command;
        try {
            command = register ? MainWindowController.REGISTER_COMMAND_NAME : MainWindowController.LOGIN_COMMAND_NAME;
            requestToServer = new Request(command, "", new User(username, password));
            if (serverWriter == null) throw new IOException();
            serverWriter.writeObject(requestToServer);
            serverResponse = (Response) serverReader.readObject();
            OutputerUI.tryError(serverResponse.getResponseBody());
        } catch (InvalidClassException | NotSerializableException exception) {
            OutputerUI.error("Произошла ошибка при отправке данных на сервер!");
        } catch (ClassNotFoundException exception) {
            OutputerUI.error("Произошла ошибка при чтении полученных данных!");
        } catch (IOException exception) {
            OutputerUI.error("Соединение с сервером разорвано!");
            try {
                connectToServer();
                OutputerUI.info("Соединение с сервером установлено.");
            } catch (ConnectionErrorException | NotInDeclaredLimitsException reconnectionException) {
                OutputerUI.info("Попробуйте повторить авторизацию позднее.");
            }
        }
        if (serverResponse != null && serverResponse.getResponseCode().equals(ResponseCode.OK)) {
            user = requestToServer.getUser();
            return true;
        }
        return false;
    }

    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Connecting to server.
     */
    private void connectToServer() throws ConnectionErrorException, NotInDeclaredLimitsException {
        try {
            Outputer.println("Соединение с сервером...");
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            serverWriter = new ObjectOutputStream(socketChannel.socket().getOutputStream());
            serverReader = new ObjectInputStream(socketChannel.socket().getInputStream());
            isConnected = true;
            Outputer.println("Соединение с сервером установлено.");
        } catch (IllegalArgumentException exception) {
            Outputer.printerror("Адрес сервера введен некорректно!");
            isConnected = false;
            throw new NotInDeclaredLimitsException();
        } catch (IOException exception) {
            Outputer.printerror("Произошла ошибка при соединении с сервером!");
            isConnected = false;
            throw new ConnectionErrorException();
        }
    }
}
