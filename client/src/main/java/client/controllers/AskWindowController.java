package client.controllers;

import client.utility.OutputerUI;
import common.data.*;
import common.exceptions.MustBeNotEmptyException;
import common.exceptions.NotInDeclaredLimitsException;
import common.interaction.MarineRaw;
import common.utility.Outputer;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AskWindowController {
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

    private Stage askStage;
    private MarineRaw resultMarine;

    @FXML
    private void initialize() {
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

    private String convertName() throws IllegalArgumentException {
        String name;
        try {
            name = nameField.getText();
            if (name.equals("")) throw new MustBeNotEmptyException();
        } catch (MustBeNotEmptyException exception) {
            OutputerUI.error("Name can't be empty!");
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
                OutputerUI.error("X must be a number!");
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
            OutputerUI.error("Y must be a number!");
            throw new IllegalArgumentException();
        } catch (NotInDeclaredLimitsException exception) {
            OutputerUI.error("Y can't be more than " + SpaceMarine.MAX_Y + "!");
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
            OutputerUI.error("Health must be a number!");
            throw new IllegalArgumentException();
        } catch (NotInDeclaredLimitsException exception) {
            OutputerUI.error("Health must be more than zero!");
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
            OutputerUI.error("Chapter name can't be empty!");
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
                OutputerUI.error("Chapter size must be positive and less than " + (SpaceMarine.MAX_MARINES + 1) + "!");
                throw new IllegalArgumentException();
            } catch (NumberFormatException exception) {
                OutputerUI.error("Chapter size must be a number!");
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

    public void setAskStage(Stage askStage) {
        this.askStage = askStage;
    }

    public MarineRaw getAndClear() {
        MarineRaw marineToReturn = resultMarine;
        resultMarine = null;
        return marineToReturn;
    }
}
