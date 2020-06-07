package client.controllers;

import client.App;
import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

public class LoginWindowController {
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private CheckBox registerCheckBox;
    @FXML
    private Label isConnectedLabel;

    private final String CONNECTED_TEXT = "Connected";
    private final Color CONNECTED_COLOR = Color.GREEN;
    private final String NOT_CONNECTED_TEXT = "Not Connected";
    private final Color NOT_CONNECTED_COLOR = Color.RED;

    App app;
    Client client;

    @FXML
    private void signInButtonOnAction(ActionEvent event) {
        if (client.processAuthentication(usernameField.getText(),
                passwordField.getText(),
                registerCheckBox.isSelected())) app.setMainWindow();
        else if (!client.isConnected()) {
            isConnectedLabel.setText(NOT_CONNECTED_TEXT);
            isConnectedLabel.setTextFill(NOT_CONNECTED_COLOR);
        } else {
            isConnectedLabel.setText(CONNECTED_TEXT);
            isConnectedLabel.setTextFill(CONNECTED_COLOR);
        }
    }

    public void initializeConnection() {
        if (client.isConnected()) {
            isConnectedLabel.setText(CONNECTED_TEXT);
            isConnectedLabel.setTextFill(CONNECTED_COLOR);
        }
    }

    public void setApp(App app) {
        this.app = app;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
