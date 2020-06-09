package client.controllers;

import client.Client;
import client.utility.OutputerUI;
import common.data.*;
import common.interaction.MarineRaw;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.table.TableFilter;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MainWindowController {
    private final String refreshCommandName = "refresh";
    private final String infoCommandName = "info";
    private final String addCommandName = "add";
    private final String updateCommandName = "update";
    private final String removeCommandName = "remove_by_id";
    private final String clearCommandName = "clear";
    private final String addIfMinCommandName = "add_if_min";
    private final String removeGreaterCommandName = "remove_greater";
    private final String historyCommandName = "history";
    private final String sumOfHealthCommandName = "sum_of_health";

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
    private Canvas canvas;
    @FXML
    private AnchorPane canvasPane;

    private Client client;
    private Stage askStage;
    private AskWindowController askWindowController;

    @FXML
    private void initialize() {
        initializeTable();
        initializeCanvas();
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

    private void initializeCanvas() {
        canvas.widthProperty().bind(canvasPane.widthProperty());
        canvas.heightProperty().bind(canvasPane.heightProperty());
    }

    @FXML
    public void refreshButtonOnAction() {
        requestAction(refreshCommandName, "", null);
    }

    @FXML
    private void infoButtonOnAction() {
        requestAction(infoCommandName, "", null);
    }

    @FXML
    private void addButtonOnAction() {
        askWindowController.clearMarine();
        askStage.showAndWait();
        MarineRaw marineRaw = askWindowController.getAndClear();
        if (marineRaw != null) requestAction(addCommandName, "", marineRaw);
    }

    @FXML
    private void updateButtonOnAction() {
        if (!spaceMarineTable.getSelectionModel().isEmpty()) {
            long id = spaceMarineTable.getSelectionModel().getSelectedItem().getId();
            askWindowController.setMarine(spaceMarineTable.getSelectionModel().getSelectedItem());
            askStage.showAndWait();
            MarineRaw marineRaw = askWindowController.getAndClear();
            if (marineRaw != null) requestAction(updateCommandName, id + "", marineRaw);
        }
        else OutputerUI.error("Select the marine to update!");

    }

    @FXML
    private void removeButtonOnAction() {
        if (!spaceMarineTable.getSelectionModel().isEmpty())
            requestAction(removeCommandName,
                    spaceMarineTable.getSelectionModel().getSelectedItem().getId().toString(), null);
        else OutputerUI.error("Select the marine to remove!");
    }

    @FXML
    private void clearButtonOnAction() {
        requestAction(clearCommandName, "", null);
    }

    @FXML
    private void addIfMinButtonOnAction() {
        askWindowController.clearMarine();
        askStage.showAndWait();
        MarineRaw marineRaw = askWindowController.getAndClear();
        if (marineRaw != null) requestAction(addIfMinCommandName, "", marineRaw);
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
            requestAction(removeGreaterCommandName, "", marineRaw);
        }

        else OutputerUI.error("Select the marine to remove greater!");
    }

    @FXML
    private void historyButtonOnAction() {
        requestAction(historyCommandName, "", null);
    }

    @FXML
    private void sumOfHealthButtonOnAction() {
        requestAction(sumOfHealthCommandName, "", null);
    }

    private void requestAction(String commandName, String commandStringArgument, Serializable commandObjectArgument) {
        ObservableList<SpaceMarine> marinesList =
                FXCollections.observableArrayList(client.processRequestToServer(commandName, commandStringArgument,
                        commandObjectArgument));
        spaceMarineTable.setItems(marinesList);
        TableFilter.forTableView(spaceMarineTable).apply();
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setAskStage(Stage askStage) {
        this.askStage = askStage;
    }

    public void setAskWindowController(AskWindowController askWindowController) {
        this.askWindowController = askWindowController;
    }
}
