package client.controllers;

import client.Client;
import client.utility.OutputerUI;
import common.data.*;
import common.interaction.MarineRaw;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.table.TableFilter;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class MainWindowController {
    public static final String LOGIN_COMMAND_NAME = "login";
    public static final String REGISTER_COMMAND_NAME = "register";
    public static final String REFRESH_COMMAND_NAME = "refresh";
    public static final String INFO_COMMAND_NAME = "info";
    public static final String ADD_COMMAND_NAME = "add";
    public static final String UPDATE_COMMAND_NAME = "update";
    public static final String REMOVE_COMMAND_NAME = "remove_by_id";
    public static final String CLEAR_COMMAND_NAME = "clear";
    public static final String EXIT_COMMAND_NAME = "exit";
    public static final String ADD_IF_MIN_COMMAND_NAME = "add_if_min";
    public static final String REMOVE_GREATER_COMMAND_NAME = "remove_greater";
    public static final String HISTORY_COMMAND_NAME = "history";
    public static final String SUM_OF_HEALTH_COMMAND_NAME = "sum_of_health";

    @FXML
    private TableView<SpaceMarine> spaceMarineTable;
    @FXML
    private TableColumn<SpaceMarine, Long> idColumn;
    @FXML
    private TableColumn<SpaceMarine, String> ownerColumn;
    @FXML
    private TableColumn<SpaceMarine, LocalDateTime> creationDateColumn;
    @FXML
    private TableColumn<SpaceMarine, String> nameColumn;
    @FXML
    private TableColumn<SpaceMarine, Double> healthColumn;
    @FXML
    private TableColumn<SpaceMarine, Double> coordinatesXColumn;
    @FXML
    private TableColumn<SpaceMarine, Float> coordinatesYColumn;
    @FXML
    private TableColumn<SpaceMarine, AstartesCategory> categoryColumn;
    @FXML
    private TableColumn<SpaceMarine, Weapon> weaponTypeColumn;
    @FXML
    private TableColumn<SpaceMarine, MeleeWeapon> meleeWeaponColumn;
    @FXML
    private TableColumn<SpaceMarine, String> chapterNameColumn;
    @FXML
    private TableColumn<SpaceMarine, Long> chapterSizeColumn;
    @FXML
    private AnchorPane canvasPane;

    private Client client;
    private Stage askStage;
    private Stage primaryStage;
    private FileChooser fileChooser;
    private AskWindowController askWindowController;
    private Map<String, Color> userColorMap;
    private Map<Shape, Long> shapeMap;
    private double canvasPanePreWidth = 1000;
    private double canvasPanePreHeight = 518;
    private Shape prevClicked;
    private Color prevColor;

    @FXML
    private void initialize() {
        initializeTable();
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        userColorMap = new HashMap<>();
        shapeMap = new HashMap<>();
    }

    private void initializeTable() {
        idColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        ownerColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getOwner().getUsername()));
        creationDateColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCreationDate()));
        nameColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getName()));
        healthColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getHealth()));
        coordinatesXColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCoordinates().getX()));
        coordinatesYColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCoordinates().getY()));
        categoryColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCategory()));
        weaponTypeColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getWeaponType()));
        meleeWeaponColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getMeleeWeapon()));
        chapterNameColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getChapter().getName()));
        chapterSizeColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getChapter().getMarinesCount()));
    }

    @FXML
    public void refreshButtonOnAction() {
        requestAction(REFRESH_COMMAND_NAME);
    }

    @FXML
    private void infoButtonOnAction() {
        requestAction(INFO_COMMAND_NAME);
    }

    @FXML
    private void addButtonOnAction() {
        askWindowController.clearMarine();
        askStage.showAndWait();
        MarineRaw marineRaw = askWindowController.getAndClear();
        if (marineRaw != null) requestAction(ADD_COMMAND_NAME, "", marineRaw);
    }

    @FXML
    private void updateButtonOnAction() {
        if (!spaceMarineTable.getSelectionModel().isEmpty()) {
            long id = spaceMarineTable.getSelectionModel().getSelectedItem().getId();
            askWindowController.setMarine(spaceMarineTable.getSelectionModel().getSelectedItem());
            askStage.showAndWait();
            MarineRaw marineRaw = askWindowController.getAndClear();
            if (marineRaw != null) requestAction(UPDATE_COMMAND_NAME, id + "", marineRaw);
        }
        else OutputerUI.error("Select the marine to update!");

    }

    @FXML
    private void removeButtonOnAction() {
        if (!spaceMarineTable.getSelectionModel().isEmpty())
            requestAction(REMOVE_COMMAND_NAME,
                    spaceMarineTable.getSelectionModel().getSelectedItem().getId().toString(), null);
        else OutputerUI.error("Select the marine to remove!");
    }

    @FXML
    private void clearButtonOnAction() {
        requestAction(CLEAR_COMMAND_NAME);
    }

    @FXML
    private void executeScriptButtonOnAction() {
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (client.processScriptToServer(selectedFile)) Platform.exit();
        else refreshButtonOnAction();
    }

    @FXML
    private void addIfMinButtonOnAction() {
        askWindowController.clearMarine();
        askStage.showAndWait();
        MarineRaw marineRaw = askWindowController.getAndClear();
        if (marineRaw != null) requestAction(ADD_IF_MIN_COMMAND_NAME, "", marineRaw);
    }

    @FXML
    private void removeGreaterButtonOnAction() {
        if (!spaceMarineTable.getSelectionModel().isEmpty())
        {
            SpaceMarine marineFromTable = spaceMarineTable.getSelectionModel().getSelectedItem();
            MarineRaw marineRaw = new MarineRaw(
                    marineFromTable.getName(),
                    marineFromTable.getCoordinates(),
                    marineFromTable.getHealth(),
                    marineFromTable.getCategory(),
                    marineFromTable.getWeaponType(),
                    marineFromTable.getMeleeWeapon(),
                    marineFromTable.getChapter()
            );
            requestAction(REMOVE_GREATER_COMMAND_NAME, "", marineRaw);
        }

        else OutputerUI.error("Select the marine to remove greater!");
    }

    @FXML
    private void historyButtonOnAction() {
        requestAction(HISTORY_COMMAND_NAME);
    }

    @FXML
    private void sumOfHealthButtonOnAction() {
        requestAction(SUM_OF_HEALTH_COMMAND_NAME);
    }

    private void requestAction(String commandName, String commandStringArgument, Serializable commandObjectArgument) {
        NavigableSet<SpaceMarine> responsedMarines = client.processRequestToServer(commandName, commandStringArgument,
                commandObjectArgument);
        if (responsedMarines != null)
        {
            ObservableList<SpaceMarine> marinesList = FXCollections.observableArrayList(responsedMarines);
            spaceMarineTable.setItems(marinesList);
            TableFilter.forTableView(spaceMarineTable).apply();
            refreshCanvas();
        }
    }

    private void requestAction(String commandName) {
        requestAction(commandName, "", null);
    }

    private void refreshCanvas() {
        shapeMap.keySet().forEach(s -> canvasPane.getChildren().remove(s));
        shapeMap.clear();
        for (SpaceMarine marine : spaceMarineTable.getItems())
        {
            if (!userColorMap.containsKey(marine.getOwner().getUsername()))
                userColorMap.put(marine.getOwner().getUsername(),
                        Color.color(Math.random(), Math.random(), Math.random()));
            double x = marine.getCoordinates().getX() * (canvasPane.getWidth() / canvasPanePreWidth) + canvasPane.getWidth() / 2;
            double y = marine.getCoordinates().getY() * (canvasPane.getHeight() / canvasPanePreHeight) + canvasPane.getHeight() / 2;
            Shape circleMarine = new Circle(x, y, marine.getHealth(), userColorMap.get(marine.getOwner().getUsername()));
            circleMarine.setOnMouseClicked(this::shapeOnMouseClicked);
            canvasPane.getChildren().add(circleMarine);
            shapeMap.put(circleMarine, marine.getId());
        }
    }

    private void shapeOnMouseClicked(MouseEvent event) {
        Shape shape = (Shape) event.getSource();
        long id = shapeMap.get(shape);
        for (SpaceMarine marine : spaceMarineTable.getItems()) {
            if (marine.getId() == id)
            {
                spaceMarineTable.getSelectionModel().select(marine);
                break;
            }
        }
        if (prevClicked != null) {
            prevClicked.setFill(prevColor);
        }
        prevClicked = shape;
        prevColor = (Color) shape.getFill();
        shape.setFill(prevColor.brighter());
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setAskStage(Stage askStage) {
        this.askStage = askStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> refreshCanvas());
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> refreshCanvas());
    }

    public void setAskWindowController(AskWindowController askWindowController) {
        this.askWindowController = askWindowController;
    }
}
