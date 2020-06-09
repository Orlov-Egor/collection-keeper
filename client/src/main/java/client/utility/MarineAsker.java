package client.utility;

import client.App;
import common.data.*;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.MustBeNotEmptyException;
import common.exceptions.NotInDeclaredLimitsException;
import common.utility.Outputer;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Asks user a marine's value.
 */
public class MarineAsker {
    private Scanner userScanner;

    public MarineAsker(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    /**
     * Asks a user the marine's name.
     *
     * @return Marine's name.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public String askName() throws IncorrectInputInScriptException {
        String name;
        try {
            Outputer.println("Введите имя:");
            Outputer.print(App.PS2);
            name = userScanner.nextLine().trim();
            Outputer.println(name);
            if (name.equals("")) throw new MustBeNotEmptyException();
            return name;
        } catch (NoSuchElementException exception) {
            Outputer.printerror("Имя не распознано!");
            OutputerUI.error("Имя не распознано!");
        } catch (MustBeNotEmptyException exception) {
            Outputer.printerror("Имя не может быть пустым!");
            OutputerUI.error("Имя не может быть пустым!");
        } catch (IllegalStateException exception) {
            Outputer.printerror("Непредвиденная ошибка!");
            OutputerUI.error("Непредвиденная ошибка!");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    /**
     * Asks a user the marine's X coordinate.
     *
     * @return Marine's X coordinate.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public double askX() throws IncorrectInputInScriptException {
        String strX;
        double x;
        try {
            Outputer.println("Введите координату X:");
            Outputer.print(App.PS2);
            strX = userScanner.nextLine().trim();
            Outputer.println(strX);
            x = Double.parseDouble(strX);
            return x;
        } catch (NoSuchElementException exception) {
            Outputer.printerror("Координата X не распознана!");
            OutputerUI.error("Координата X не распознана!");
        } catch (NumberFormatException exception) {
            Outputer.printerror("Координата X должна быть представлена числом!");
            OutputerUI.error("Координата X должна быть представлена числом!");
        } catch (NullPointerException | IllegalStateException exception) {
            Outputer.printerror("Непредвиденная ошибка!");
            OutputerUI.error("Непредвиденная ошибка!");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    /**
     * Asks a user the marine's Y coordinate.
     *
     * @return Marine's Y coordinate.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public Float askY() throws IncorrectInputInScriptException {
        String strY;
        Float y;
        try {
            Outputer.println("Введите координату Y < " + (SpaceMarine.MAX_Y + 1) + ":");
            Outputer.print(App.PS2);
            strY = userScanner.nextLine().trim();
            Outputer.println(strY);
            y = Float.parseFloat(strY);
            if (y > SpaceMarine.MAX_Y) throw new NotInDeclaredLimitsException();
            return y;
        } catch (NoSuchElementException exception) {
            Outputer.printerror("Координата Y не распознана!");
            OutputerUI.error("Координата Y не распознана!");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("Координата Y не может превышать " + SpaceMarine.MAX_Y + "!");
            OutputerUI.error("Координата Y не может превышать " + SpaceMarine.MAX_Y + "!");
        } catch (NumberFormatException exception) {
            Outputer.printerror("Координата Y должна быть представлена числом!");
        } catch (NullPointerException | IllegalStateException exception) {
            Outputer.printerror("Непредвиденная ошибка!");
            OutputerUI.error("Непредвиденная ошибка!");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    /**
     * Asks a user the marine's coordinates.
     *
     * @return Marine's coordinates.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public Coordinates askCoordinates() throws IncorrectInputInScriptException {
        double x;
        Float y;
        x = askX();
        y = askY();
        return new Coordinates(x, y);
    }

    /**
     * Asks a user the marine's health.
     *
     * @return Marine's health.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public double askHealth() throws IncorrectInputInScriptException {
        String strHealth;
        double health;
        try {
            Outputer.println("Введите здоровье:");
            Outputer.print(App.PS2);
            strHealth = userScanner.nextLine().trim();
            Outputer.println(strHealth);
            health = Double.parseDouble(strHealth);
            if (health <= SpaceMarine.MIN_HEALTH) throw new NotInDeclaredLimitsException();
            return health;
        } catch (NoSuchElementException exception) {
            Outputer.printerror("Здоровье не распознано!");
            OutputerUI.error("Здоровье не распознано!");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("Здоровье должно быть больше нуля!");
            OutputerUI.error("Здоровье должно быть больше нуля!");
        } catch (NumberFormatException exception) {
            Outputer.printerror("Здоровье должно быть представлено числом!");
        } catch (NullPointerException | IllegalStateException exception) {
            Outputer.printerror("Непредвиденная ошибка!");
            OutputerUI.error("Непредвиденная ошибка!");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    /**
     * Asks a user the marine's category.
     *
     * @return Marine's category.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public AstartesCategory askCategory() throws IncorrectInputInScriptException {
        String strCategory;
        AstartesCategory category;
        try {
            Outputer.println("Список категорий - " + AstartesCategory.nameList());
            Outputer.println("Введите категорию:");
            Outputer.print(App.PS2);
            strCategory = userScanner.nextLine().trim();
            Outputer.println(strCategory);
            category = AstartesCategory.valueOf(strCategory.toUpperCase());
            return category;
        } catch (NoSuchElementException exception) {
            Outputer.printerror("Категория не распознана!");
            OutputerUI.error("Категория не распознана!");
        } catch (IllegalArgumentException exception) {
            Outputer.printerror("Категории нет в списке!");
            OutputerUI.error("Категории нет в списке!");
        } catch (IllegalStateException exception) {
            Outputer.printerror("Непредвиденная ошибка!");
            OutputerUI.error("Непредвиденная ошибка!");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    /**
     * Asks a user the marine's weapon type.
     *
     * @return Marine's weapon type.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public Weapon askWeaponType() throws IncorrectInputInScriptException {
        String strWeaponType;
        Weapon weaponType;
        try {
            Outputer.println("Список оружия дальнего боя - " + Weapon.nameList());
            Outputer.println("Введите оружие дальнего боя:");
            Outputer.print(App.PS2);
            strWeaponType = userScanner.nextLine().trim();
            Outputer.println(strWeaponType);
            weaponType = Weapon.valueOf(strWeaponType.toUpperCase());
            return weaponType;
        } catch (NoSuchElementException exception) {
            Outputer.printerror("Оружие не распознано!");
            OutputerUI.error("Оружие не распознано!");
        } catch (IllegalArgumentException exception) {
            Outputer.printerror("Оружия нет в списке!");
            OutputerUI.error("Оружия нет в списке!");
        } catch (IllegalStateException exception) {
            Outputer.printerror("Непредвиденная ошибка!");
            OutputerUI.error("Непредвиденная ошибка!");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    /**
     * Asks a user the marine's melee weapon.
     *
     * @return Marine's melee weapon.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public MeleeWeapon askMeleeWeapon() throws IncorrectInputInScriptException {
        String strMeleeWeapon;
        MeleeWeapon meleeWeapon;
        try {
            Outputer.println("Список оружия ближнего боя - " + MeleeWeapon.nameList());
            Outputer.println("Введите оружие ближнего боя:");
            Outputer.print(App.PS2);
            strMeleeWeapon = userScanner.nextLine().trim();
            Outputer.println(strMeleeWeapon);
            meleeWeapon = MeleeWeapon.valueOf(strMeleeWeapon.toUpperCase());
            return meleeWeapon;
        } catch (NoSuchElementException exception) {
            Outputer.printerror("Оружие не распознано!");
            OutputerUI.error("Оружие не распознано!");
        } catch (IllegalArgumentException exception) {
            Outputer.printerror("Оружия нет в списке!");
            OutputerUI.error("Оружия нет в списке!");
        } catch (IllegalStateException exception) {
            Outputer.printerror("Непредвиденная ошибка!");
            OutputerUI.error("Непредвиденная ошибка!");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    /**
     * Asks a user the marine chapter's name.
     *
     * @return Chapter's name.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public String askChapterName() throws IncorrectInputInScriptException {
        String chapterName;
        try {
            Outputer.println("Введите имя ордена:");
            Outputer.print(App.PS2);
            chapterName = userScanner.nextLine().trim();
            Outputer.println(chapterName);
            if (chapterName.equals("")) throw new MustBeNotEmptyException();
            return chapterName;
        } catch (NoSuchElementException exception) {
            Outputer.printerror("Имя ордена не распознано!");
            OutputerUI.error("Имя ордена не распознано!");
        } catch (MustBeNotEmptyException exception) {
            Outputer.printerror("Имя ордена не может быть пустым!");
            OutputerUI.error("Имя ордена не может быть пустым!");
        } catch (IllegalStateException exception) {
            Outputer.printerror("Непредвиденная ошибка!");
            OutputerUI.error("Непредвиденная ошибка!");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    /**
     * Asks a user the marine chapter's number of soldiers.
     *
     * @return Number of soldiers.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public long askChapterMarinesCount() throws IncorrectInputInScriptException {
        String strMarinesCount;
        long marinesCount;
        try {
            Outputer.println("Введите количество солдат в ордене < " + (SpaceMarine.MAX_MARINES + 1) + ":");
            Outputer.print(App.PS2);
            strMarinesCount = userScanner.nextLine().trim();
            Outputer.println(strMarinesCount);
            marinesCount = Long.parseLong(strMarinesCount);
            if (marinesCount < SpaceMarine.MIN_MARINES || marinesCount > SpaceMarine.MAX_MARINES)
                throw new NotInDeclaredLimitsException();
            return marinesCount;
        } catch (NoSuchElementException exception) {
            Outputer.printerror("Количество солдат в ордене не распознано!");
            OutputerUI.error("Количество солдат в ордене не распознано!");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("Количество солдат в ордене должно быть положительным и не превышать " + SpaceMarine.MAX_MARINES + "!");
            OutputerUI.error("Количество солдат в ордене должно быть положительным и не превышать " + SpaceMarine.MAX_MARINES + "!");
        } catch (NumberFormatException exception) {
            Outputer.printerror("Количество солдат в ордене должно быть представлено числом!");
            OutputerUI.error("Количество солдат в ордене должно быть представлено числом!");
        } catch (NullPointerException | IllegalStateException exception) {
            Outputer.printerror("Непредвиденная ошибка!");
            OutputerUI.error("Непредвиденная ошибка!");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    /**
     * Asks a user the marine's chapter.
     *
     * @return Marine's chapter.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public Chapter askChapter() throws IncorrectInputInScriptException {
        String name;
        long marinesCount;
        name = askChapterName();
        marinesCount = askChapterMarinesCount();
        return new Chapter(name, marinesCount);
    }

    /**
     * Asks a user a question.
     *
     * @param question A question.
     * @return Answer (true/false).
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public boolean askQuestion(String question) throws IncorrectInputInScriptException {
        String finalQuestion = question + " (+/-):";
        String answer;
        try {
            Outputer.println(finalQuestion);
            Outputer.print(App.PS2);
            answer = userScanner.nextLine().trim();
            Outputer.println(answer);
            if (!answer.equals("+") && !answer.equals("-")) throw new NotInDeclaredLimitsException();
            return answer.equals("+");
        } catch (NoSuchElementException exception) {
            Outputer.printerror("Ответ не распознан!");
            OutputerUI.error("Ответ не распознан!");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("Ответ должен быть представлен знаками '+' или '-'!");
            OutputerUI.error("Ответ должен быть представлен знаками '+' или '-'!");
        } catch (IllegalStateException exception) {
            Outputer.printerror("Непредвиденная ошибка!");
            OutputerUI.error("Непредвиденная ошибка!");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }
}
