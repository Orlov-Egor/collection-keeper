package client.controllers;

import client.Client;
import common.data.*;
import common.interaction.User;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.table.TableFilter;

import java.time.LocalDateTime;

public class MainWindowController {
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
    private TableColumn<SpaceMarine, Chapter> chapterColumn;
    @FXML
    private Canvas canvas;
    @FXML
    private AnchorPane canvasPane;

    private Client client;

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
        chapterColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getChapter()));

        spaceMarineTable.setItems(testFill());

        TableFilter.forTableView(spaceMarineTable).apply();
    }

    private void initializeCanvas() {
        canvas.widthProperty().bind(canvasPane.widthProperty());
        canvas.heightProperty().bind(canvasPane.heightProperty());
    }

    @FXML
    public void refreshData() {
        System.out.println("SOSI, NE GOTOVO");
    }

    public void setClient(Client client) {
        this.client = client;
    }

    private ObservableList<SpaceMarine> testFill() {
        // TODO: Это для тестов, удалить
        ObservableList<SpaceMarine> testList = FXCollections.observableArrayList();
        testList.add(new SpaceMarine(1L,
                "Dmitry",
                new Coordinates(0, 0f),
                LocalDateTime.now(),
                100,
                AstartesCategory.ASSAULT,
                Weapon.BOLT_PISTOL,
                MeleeWeapon.CHAIN_AXE,
                new Chapter("AlcoGang", 200),
                new User("admin", "admin")));
        testList.add(new SpaceMarine(2L,
                "Egor",
                new Coordinates(1, 0f),
                LocalDateTime.now().plusDays(3),
                117,
                AstartesCategory.APOTHECARY,
                Weapon.GRAV_GUN,
                MeleeWeapon.CHAIN_SWORD,
                new Chapter("VeloGang", 150),
                new User("test", "test")));
        testList.add(new SpaceMarine(3L,
                "Alex",
                new Coordinates(2, 0f),
                LocalDateTime.now().plusYears(1),
                25,
                AstartesCategory.ASSAULT,
                Weapon.BOLT_PISTOL,
                MeleeWeapon.CHAIN_AXE,
                new Chapter("BuddaGang", 250),
                new User("admin", "admin")));
        testList.add(new SpaceMarine(4L,
                "Tima",
                new Coordinates(0, 1f),
                LocalDateTime.now().plusMonths(2),
                112,
                AstartesCategory.APOTHECARY,
                Weapon.GRAV_GUN,
                MeleeWeapon.POWER_FIST,
                new Chapter("VeloGang", 150),
                new User("test", "test")));
        testList.add(new SpaceMarine(5L,
                "Diana",
                new Coordinates(1, 2f),
                LocalDateTime.now().plusMinutes(45),
                10,
                AstartesCategory.ASSAULT,
                Weapon.HEAVY_BOLTGUN,
                MeleeWeapon.CHAIN_AXE,
                new Chapter("AlcoGang", 200),
                new User("admin", "admin")));
        testList.add(new SpaceMarine(6L,
                "Alina",
                new Coordinates(1, 3f),
                LocalDateTime.now().minusSeconds(2),
                154,
                AstartesCategory.APOTHECARY,
                Weapon.GRAV_GUN,
                MeleeWeapon.LIGHTING_CLAW,
                new Chapter("BuddaGang", 250),
                new User("morda", "morda")));

        return testList;
    }
}
