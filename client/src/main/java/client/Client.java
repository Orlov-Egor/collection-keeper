package client;

import client.utility.AuthHandler;
import client.utility.OutputerUI;
import client.utility.UserHandler;
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

/**
 * Runs the client.
 */
public class Client implements Runnable {
    private String host;
    private int port;
    private UserHandler userHandler;
    private SocketChannel socketChannel;
    private ObjectOutputStream serverWriter;
    private ObjectInputStream serverReader;
    private User user;
    private boolean isConnected;

    public Client(String host, int port, UserHandler userHandler) {
        this.host = host;
        this.port = port;
        this.userHandler = userHandler;
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
    public void processRequestToServer() {
        Request requestToServer = null;
        Response serverResponse = null;
        do {
            try {
                requestToServer = serverResponse != null ? userHandler.handle(serverResponse.getResponseCode(), user) :
                        userHandler.handle(null, user);
                if (requestToServer.isEmpty()) continue;
                serverWriter.writeObject(requestToServer);
                serverResponse = (Response) serverReader.readObject();
                OutputerUI.info(serverResponse.getResponseBody());
            } catch (InvalidClassException | NotSerializableException exception) {
                OutputerUI.error("Произошла ошибка при отправке данных на сервер!");
            } catch (ClassNotFoundException exception) {
                OutputerUI.error("Произошла ошибка при чтении полученных данных!");
            } catch (IOException exception) {
                OutputerUI.error("Соединение с сервером разорвано!");
                try {
                    connectToServer();
                    OutputerUI.info("Соединение с сервером установлено!");
                } catch (ConnectionErrorException | NotInDeclaredLimitsException reconnectionException) {
                    if (requestToServer.getCommandName().equals("exit"))
                        OutputerUI.info("Команда не будет зарегистрирована на сервере.");
                    else OutputerUI.info("Попробуйте повторить команду позднее.");
                }
            }
        } while (!requestToServer.getCommandName().equals("exit"));
    }

    /**
     * Handle process authentication.
     */
    public boolean processAuthentication(String username, String password, boolean register) {
        Request requestToServer = null;
        Response serverResponse = null;
        try {
            requestToServer = AuthHandler.handle(username, password, register);
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
                OutputerUI.info("Соединение с сервером установлено!");
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
