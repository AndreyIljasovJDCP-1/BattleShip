import java.util.Arrays;
import java.util.Scanner;

/**
 * Абстрактный класс для обработки координат.
 * <p>Реализует типовые методы обработки координат.
 * <p>2 класса-наследника: AutoPoint и UserPoint.
 */
public abstract class Point {
    public static final Scanner scanner = new Scanner(System.in);
    public static final int SIZE = 10;
    public static final char EMPTY = '-';
    public static final String INJURED_SHIP = "XXXX";
    public static final char OFF_TARGET = '*';
    public static final char SHIP = 'O';
    protected int x;
    protected int y;

    public Point() {
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return возвращает случайное число от 0 до 9
     */
    public int getRandomXY() {
        return (int) (Math.random() * 10);
    }

    /**
     * Установка рандомных координат.
     * Реализуется в классе AutoPoint для расстановки кораблей.
     */
    public abstract void setRandomXY();

    /**
     * Установка новых координат.
     * <p>Реализуется в классах UserPoint и AutoPoint.
     * <p>AutoPoint: - авто-генерация координат для выстрела.
     * <p>UserPoint: - получение координат от пользователя
     * в формате A1-J10 для выстрела или расстановки кораблей.
     *
     * @param field поле боя игрока для выстрела или поле игрока для расстановки кораблей
     */
    public abstract void setNewXY(char[][] field);

    /**
     * Проверяет, НЕ хватит ли места в поле для корабля по длине от координаты (х,у).
     *
     * @return true - места не хватит, false - места хватит
     */
    public boolean isLongNotEnough(int orientation, int shipLong) {
        return orientation == 1 ? (y + shipLong > SIZE) : (x + shipLong > SIZE);
    }


    /**
     * Проверяет, в пределах ли поля координаты (х,у).
     *
     * @return true - в пределах, false - нет
     */
    public boolean isInField() {
        return x > -1 && x < SIZE && y > -1 && y < SIZE;
    }

    /**
     * Проверяет, занято ли поле по координатам(х,у).
     *
     * @return true - занято, false - свободно
     */
    public boolean isBusyField(char[][] playerField) {
        return playerField[x][y] != EMPTY;
    }

    /**
     * Проверяет, НЕ хватит ли места вокруг для установки корабля по координатам(х,у).
     *
     * @return true - места не хватит, false - места хватит.
     */
    public boolean isBusyAround(char[][] currentField, int orientation, int shipLong) {

        boolean freeCell = true;
        char[][] helpArr = new char[SIZE + 2][SIZE + 2];
        fillField(helpArr, EMPTY);

        for (int j = 1; j < SIZE + 1; j++) {
            for (int i = 1; i < SIZE + 1; i++) {
                helpArr[i][j] = currentField[i - 1][j - 1];
            }
        }

        if (orientation == 1) {
            for (int j = y; j <= y + shipLong + 1; j++) {
                for (int i = x; i <= x + 2; i++) {
                    freeCell &= (helpArr[i][j] == EMPTY);
                }

                if (!freeCell) {
                    break;
                }
            }
        } else {
            for (int j = x; j <= x + shipLong + 1; j++) {
                for (int i = y; i <= y + 2; i++) {
                    freeCell &= (helpArr[j][i] == EMPTY);
                }

                if (!freeCell) {
                    break;
                }
            }
        }
        return !freeCell;
    }

    /**
     * Метод отрисовки корабля на поле игрока.
     *
     * @param playerField поле игрока
     * @param shipLong    длина корабля
     * @param orientation ориентация
     */

    public void drawShip(char[][] playerField, int shipLong, int orientation) {

        if (orientation == 1) {
            for (int shipSection = 0; shipSection < shipLong; shipSection++) {
                playerField[x][y + shipSection] = SHIP;
            }
        } else {
            for (int shipSection = 0; shipSection < shipLong; shipSection++) {
                playerField[x + shipSection][y] = SHIP;
            }
        }
    }

    /**
     * Заполнить поле игрока заданным символом.
     *
     * @param field  поле игрока.
     * @param symbol символ.
     */
    public static void fillField(char[][] field, char symbol) {
        for (char[] row : field) {
            Arrays.fill(row, symbol);
        }
    }

    public String transformXY() {
        return (char) (y + 65) + "-" + (x + 1);
    }

    @Override
    public String toString() {
        return transformXY();
    }

}