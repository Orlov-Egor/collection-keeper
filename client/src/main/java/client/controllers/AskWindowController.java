package client.controllers;

import client.controllers.tools.ObservableResourceFactory;
import client.utility.OutputerUI;
import common.data.*;
import common.exceptions.MustBeNotEmptyException;
import common.exceptions.NotInDeclaredLimitsException;
import common.interaction.MarineRaw;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AskWindowController implements Initializable {
    @FXML
    private Label nameLabel;
    @FXML
    private Label coordinatesXLabel;
    @FXML
    private Label coordinatesYLabel;
    @FXML
    private Label healthLabel;
    @FXML
    private Label categoryLabel;
    @FXML
    private Label weaponLabel;
    @FXML
    private Label meleeWeaponLabel;
    @FXML
    private Label chapterNameLabel;
    @FXML
    private Label chapterSizeLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField coordinatesXField;
    @FXML
    private TextField coordinatesYField;
    @FXML
    private TextField healthField;
    @FXML
    private TextField chapterNameField;
    @FXML
    private TextField chapterSizeField;
    @FXML
    private ComboBox<AstartesCategory> categoryBox;
    @FXML
    private ComboBox<Weapon> weaponBox;
    @FXML
    private ComboBox<MeleeWeapon> meleeWeaponBox;
    @FXML
    private Button enterButton;

    private Stage askStage;
    private MarineRaw resultMarine;
    private ObservableResourceFactory resourceFactory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoryBox.setItems(FXCollections.observableArrayList(AstartesCategory.values()));
        weaponBox.setItems(FXCollections.observableArrayList(Weapon.values()));
        meleeWeaponBox.setItems(FXCollections.observableArrayList(MeleeWeapon.values()));
    }

    @FXML
    private void enterButtonOnAction() {
        try {
            resultMarine = new MarineRaw(
                    convertName(),
                    new Coordinates(
                            convertCoordinatesxX(),
                            convertCoordinatesxY()
                    ),
                    convertHealth(),
                    categoryBox.getValue(),
                    weaponBox.getValue(),
                    meleeWeaponBox.getValue(),
                    new Chapter(
                            convertChapterName(),
                            convertChapterSize()
                    )
            );
            askStage.close();
        } catch (IllegalArgumentException exception) { /* ? */ }
    }

    private void bindGuiLanguage() {
        nameLabel.textProperty().bind(resourceFactory.getStringBinding("NameColumn"));
        coordinatesXLabel.textProperty().bind(resourceFactory.getStringBinding("CoordinatesXColumn"));
        coordinatesYLabel.textProperty().bind(resourceFactory.getStringBinding("CoordinatesYColumn"));
        healthLabel.textProperty().bind(resourceFactory.getStringBinding("HealthColumn"));
        categoryLabel.textProperty().bind(resourceFactory.getStringBinding("CategoryColumn"));
        weaponLabel.textProperty().bind(resourceFactory.getStringBinding("WeaponColumn"));
        meleeWeaponLabel.textProperty().bind(resourceFactory.getStringBinding("MeleeWeaponColumn"));
        chapterNameLabel.textProperty().bind(resourceFactory.getStringBinding("ChapterNameColumn"));
        chapterSizeLabel.textProperty().bind(resourceFactory.getStringBinding("ChapterSizeColumn"));
        enterButton.textProperty().bind(resourceFactory.getStringBinding("EnterButton"));
    }

    private String convertName() throws IllegalArgumentException {
        String name;
        try {
            name = nameField.getText();
            if (name.equals("")) throw new MustBeNotEmptyException();
        } catch (MustBeNotEmptyException exception) {
            OutputerUI.error("NameEmptyException");
            throw new IllegalArgumentException();
        }
        return name;
    }

    private double convertCoordinatesxX() throws IllegalArgumentException {
        String strX;
        double x;
        try {
            strX = coordinatesXField.getText();
            x = Double.parseDouble(strX);
        } catch (NumberFormatException exception) {
            OutputerUI.error("CoordinatesXFormatException");
            throw new IllegalArgumentException();
        }
        return x;
    }

    private Float convertCoordinatesxY() throws IllegalArgumentException {
        String strY;
        Float y;
        try {
            strY = coordinatesYField.getText();
            y = Float.parseFloat(strY);
            if (y > SpaceMarine.MAX_Y) throw new NotInDeclaredLimitsException();
        } catch (NumberFormatException exception) {
            OutputerUI.error("CoordinatesYFormatException");
            throw new IllegalArgumentException();
        } catch (NotInDeclaredLimitsException exception) {
            OutputerUI.error("CoordinatesYLimitsException", new String[]{String.valueOf(SpaceMarine.MAX_Y)});
            throw new IllegalArgumentException();
        }
        return y;
    }

    private double convertHealth() throws IllegalArgumentException {
        String strHealth;
        double health;
        try {
            strHealth = healthField.getText();
            health = Double.parseDouble(strHealth);
            if (health <= SpaceMarine.MIN_HEALTH) throw new NotInDeclaredLimitsException();
        } catch (NumberFormatException exception) {
            OutputerUI.error("HealthFormatException");
            throw new IllegalArgumentException();
        } catch (NotInDeclaredLimitsException exception) {
            OutputerUI.error("HealthLimitsException");
            throw new IllegalArgumentException();
        }
        return health;
    }

    private String convertChapterName() throws IllegalArgumentException {
        String chapterName;
        try {
            chapterName = chapterNameField.getText();
            if (chapterName.equals("")) throw new MustBeNotEmptyException();
        } catch (MustBeNotEmptyException exception) {
            OutputerUI.error("ChapterNameEmptyException");
            throw new IllegalArgumentException();
        }
        return chapterName;
    }

    private long convertChapterSize() throws IllegalArgumentException {
        String strMarinesCount;
        long marinesCount;
        try {
            strMarinesCount = chapterSizeField.getText();
            marinesCount = Long.parseLong(strMarinesCount);
            if (marinesCount < SpaceMarine.MIN_MARINES || marinesCount > SpaceMarine.MAX_MARINES)
                throw new NotInDeclaredLimitsException();
        } catch (NotInDeclaredLimitsException exception) {
            OutputerUI.error("ChapterSizeLimitsException", new String[]{String.valueOf(SpaceMarine.MAX_MARINES + 1)});
            throw new IllegalArgumentException();
        } catch (NumberFormatException exception) {
            OutputerUI.error("ChapterSizeFormatException");
            throw new IllegalArgumentException();
        }
        return marinesCount;
    }

    public void setMarine(SpaceMarine marine) {
        nameField.setText(marine.getName());
        coordinatesXField.setText(marine.getCoordinates().getX() + "");
        coordinatesYField.setText(marine.getCoordinates().getY() + "");
        healthField.setText(marine.getHealth() + "");
        chapterNameField.setText(marine.getChapter().getName());
        chapterSizeField.setText(marine.getChapter().getMarinesCount() + "");
        categoryBox.setValue(marine.getCategory());
        weaponBox.setValue(marine.getWeaponType());
        meleeWeaponBox.setValue(marine.getMeleeWeapon());
    }

    public void clearMarine() {
        nameField.clear();
        coordinatesXField.clear();
        coordinatesYField.clear();
        healthField.clear();
        chapterNameField.clear();
        chapterSizeField.clear();
        categoryBox.setValue(AstartesCategory.DREADNOUGHT);
        weaponBox.setValue(Weapon.HEAVY_BOLTGUN);
        meleeWeaponBox.setValue(MeleeWeapon.CHAIN_SWORD);
    }

    public MarineRaw getAndClear() {
        MarineRaw marineToReturn = resultMarine;
        resultMarine = null;
        return marineToReturn;
    }

    public void setAskStage(Stage askStage) {
        this.askStage = askStage;
    }


    public void initLangs(ObservableResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        bindGuiLanguage();
    }
}
