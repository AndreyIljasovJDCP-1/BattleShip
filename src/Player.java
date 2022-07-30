import java.util.Random;

/**
 * Реализует основную логику игры,
 * от расстановки кораблей до выдачи статистики по итогам.
 */
public class Player {
    public static final int SIZE = 10;
    public static final char SHIP = 'O';
    public static final char OFF_TARGET = '*';
    public static final char INJURED = 'X';
    public static final char EMPTY = '-';
    public static final Random RANDOM = new Random();

    private final String name;
    private boolean turn;
    /**
     * Режим стрельбы.
     */
    private int shootingMode;
    /**
     * AutoPoint или UserPoint.
     * Режим обработки координат.
     */
    protected Point pointMode;
    /**
     * Список кораблей игрока.
     */

    private final Ship[] ships;
    /**
     * Поле боя игрока
     * (здесь отмечаются выстрелы игрока и обнаруженные корабли соперника).
     */

    private final char[][] battleField;
    /**
     * Поле игрока
     * (здесь отмечаются корабли игрока и выстрелы соперника).
     */

    private final char[][] playerField;

    /**
     * Конструктор игрока.
     *
     * @param name имя игрока.
     */
    public Player(String name) {
        this.name = name;
        this.ships = new Ship[SIZE];
        this.battleField = new char[SIZE][SIZE];
        this.playerField = new char[SIZE][SIZE];
        this.shootingMode = 2;// по умолчанию;
        this.pointMode = new AutoPoint();// по умолчанию;
    }

    /**
     * Установка режима обработки координат.
     *
     * @param select код режима: 1 - UserPoint, 2 - AutoPoint
     */
    public void setPointMode(int select) {
        if (select == 1) {
            this.pointMode = new UserPoint();
        } else if (select == 2) {
            this.pointMode = new AutoPoint();
        }

    }

    public String getName() {
        return name;
    }

    public char[][] getBattleField() {
        return battleField;
    }

    public char[][] getPlayerField() {
        return playerField;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public boolean getTurn() {
        return turn;
    }

    public void showBattleField() {
        showField(battleField);
    }

    public void showPlayerField() {
        showField(playerField);
    }

    public int getShootingMode() {
        return shootingMode;
    }

    /**
     * Вывод в консоль поля игрока.
     *
     * @param field тип поля
     */
    public void showField(char[][] field) {
        int i = 0;
        char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

        System.out.print("   ");
        for (char s : letters) {
            System.out.print(s + " ");
        }
        System.out.println();

        for (char[] rows : field) {
            i++;
            System.out.print((i < 10) ? " " + i + "|" : i + "|");
            for (char cell : rows) {
                System.out.print(cell + "|");
            }
            System.out.println();
        }
        System.out.println("-----------------------");
    }

    /**
     * Выбор, установка режима и расстановка кораблей, согласно выбранному режиму.
     *
     * @return true - корабли расставлены, false - выбран п.3 выход
     */

    public boolean selectModeShipsPlace() {
        int placeMode = Menu.selectMode(name, "режим расстановки кораблей");

        if (placeMode == 1) {
            setPointMode(1);
            return placeShipsHandMode();
        } else if (placeMode == 2) {
            placeShipsAutoMode();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Выбор и установка режима стрельбы.
     *
     * @return true-выбор сделан, false - выбран п.3 выход
     */
    public boolean selectShootingMode() {
        shootingMode = Menu.selectMode(name, "режим стрельбы");
        setPointMode(shootingMode);
        return shootingMode != 3;
    }

    /**
     * Ручной режим расстановки кораблей.
     * <p>Получение от пользователя координат и ориентации корабля.
     * Проверка на корректность, указанного места постановки корабля.
     * <p><b>Результат:</b> поле игрока с 10 кораблями, расставленными по указанию игрока,
     * с соблюдением расстояния между ними не менее 1 клетки.
     *
     * @return true - корабли расставлены, false - выбран п.3 выход
     */

    public boolean placeShipsHandMode() {

        System.out.println("Начинаем расстановку кораблей игрока: " + name);

        for (int shipLong = 4; shipLong >= 1; shipLong--) {
            for (int shipCount = 1; shipCount <= 5 - shipLong; shipCount++) {

                System.out.println("Выберите место для " + shipLong + "-палубного корабля.");

                boolean isBusy = true;
                int orientation = 1;

                while (isBusy) {
                    isBusy = false;
                    pointMode.setNewXY(playerField);
                    orientation = Menu.selectOrientation();

                    if (orientation == 3) {
                        return false;
                    }

                    if (pointMode.isLongNotEnough(orientation, shipLong)) {
                        isBusy = true;
                        System.out.println("Ошибка! Не хватит длины!");
                        continue;
                    }

                    if (pointMode.isBusyAround(playerField, orientation, shipLong)) {
                        isBusy = true;
                        System.out.println("Ошибка! Не хватит места вокруг!");
                    }
                }

                pointMode.drawShip(playerField, shipLong, orientation);
                addShipToList(new Ship(pointMode, orientation, shipLong));
                System.out.println("Осталось разместить " + (5 - shipLong - shipCount) + " шт. " + shipLong + "-палубных.");
                showPlayerField();
            }
        }
        System.out.println("\n" + name + "! Ваши корабли расставлены.");
        return true;
    }

    /**
     * АвтоРежим расстановки кораблей.
     * <p><b>Результат:</b> поле игрока с 10 кораблями, расставленными в случайном порядке,
     * с соблюдением расстояния между ними не менее 1 клетки.
     */
    public void placeShipsAutoMode() {
        int[][] busyCells = new int[144][2];
        int sizeBusyCells = 0;

        System.out.println("\nНачинаем расстановку кораблей игрока: " + name);

        for (int shipLong = 4; shipLong >= 1; shipLong--) {
            for (int shipCount = 1; shipCount <= 5 - shipLong; shipCount++) {
                boolean isBusy = true;
                int orientation = 1;

                while (isBusy) {
                    isBusy = false;
                    pointMode.setRandomXY();
                    orientation = RANDOM.nextBoolean() ? 1 : 2;

                    if (pointMode.isBusyField(playerField)) {
                        isBusy = true;
                        continue;
                    }

                    if (pointMode.isLongNotEnough(orientation, shipLong)) {
                        isBusy = true;
                        continue;
                    }

                    if (sizeBusyCells != 0) {
                        for (int b = 0; b < sizeBusyCells; b++) {
                            if (pointMode.getX() == busyCells[b][0] && pointMode.getY() == busyCells[b][1]) {
                                isBusy = true;
                                break;
                            }
                        }
                        if (isBusy) continue;
                    }

                    if (pointMode.isBusyAround(playerField, orientation, shipLong)) {
                        isBusy = true;
                    }
                }
                pointMode.drawShip(playerField, shipLong, orientation);
                addShipToList(new Ship(pointMode, orientation, shipLong));
                sizeBusyCells = fillBusy(busyCells, pointMode, shipLong, orientation, sizeBusyCells);
            }
        }
        System.out.println("\n" + name + "! Ваши корабли расставлены.");
        showPlayerField();

    }

    /**
     * Служебный метод для режима АвтоРасстановки кораблей.
     * Заполнение массива-карты координат корабля с окружением из *.
     *
     * @param busyCells     массив с координатами расставленных кораблей с окружением из *
     * @param placePoint    координаты первой палубы (клетки)
     * @param shipLong      длина корабля
     * @param orientation   ориентация корабля
     * @param sizeBusyCells номер последней ячейки массива
     * @return sizeBusyCells номер последней ячейки массива
     */
    public static int fillBusy(int[][] busyCells, Point placePoint, int shipLong, int orientation, int sizeBusyCells) {
        int x = placePoint.getX();
        int y = placePoint.getY();

        if (orientation == 1) {
            for (int column = y - 1; column < y + shipLong + 1; column++) {
                for (int row = x - 1; row <= x + 1; row++) {
                    busyCells[sizeBusyCells][0] = row;//координата x
                    busyCells[sizeBusyCells][1] = column;//координата y
                    sizeBusyCells++;
                }
            }
        } else {
            for (int row = x - 1; row < x + shipLong + 1; row++) {
                for (int column = y - 1; column <= y + 1; column++) {
                    busyCells[sizeBusyCells][0] = row;//координата x
                    busyCells[sizeBusyCells][1] = column;//координата y
                    sizeBusyCells++;
                }
            }
        }
        return sizeBusyCells;
    }

    /**
     * Метод добавления корабля в список кораблей игрока, после установки на поле игрока.
     *
     * @param ship новый корабль
     */
    public void addShipToList(Ship ship) {
        for (int i = 0; i < ships.length; i++) {

            if (ships[i] == null) {
                ships[i] = ship;
                return;
            }
        }
    }

    /**
     * Метод открыть огонь,т.е. сделать выстрел.
     * Метод {@link Point#setNewXY(char[][])} генерирует координаты выстрела.
     * Обороняющийся игрок обрабатывает выстрел, и сообщает результат.
     * Оба игрока отмечают у себя на поле выстрел.
     * При промахе меняется очередность.
     *
     * @param defender обороняющийся игрок.
     * @see #handleShot(Point, Player)
     */
    public void openFire(Player defender) throws InterruptedException {
        if (turn) {
            System.out.println("Стреляет: " + name);

            if (shootingMode == 1) {
                showBattleField();
            }

            pointMode.setNewXY(battleField);
            System.out.println("Координаты: " + pointMode);
            defender.handleShot(pointMode, this);
            showBattleField();
            Thread.sleep(1000L * Main.delay);
        }
    }

    /**
     * Метод обработки выстрела. Обороняющийся игрок проверяет выстрел по координатам.
     * Сообщает результат выстрела. Игроки отмечают попадание или промах на своих полях.
     * Если корабль "убит", область вокруг корабля на поле атакующего отмечается *****
     * {@link  #markKilledShip(int[][])}
     * <p>При промахе меняется очередность.
     *
     * @param shot    координаты выстрела
     * @param shooter атакующий игрок
     * @see #openFire(Player)
     */
    public void handleShot(Point shot, Player shooter) {
        for (Ship ship : ships) {

            if (ship.isShipInjured(shot)) {
                playerField[shot.getX()][shot.getY()] = INJURED;
                shooter.getBattleField()[shot.getX()][shot.getY()] = INJURED;

                if (ship.isShipKilled()) {
                    System.out.println("Убит!");
                    shooter.markKilledShip(ship.getShipCoordinates());
                } else {
                    System.out.println("Ранен.");
                }
                return;
            }
        }

        playerField[shot.getX()][shot.getY()] = OFF_TARGET;
        shooter.getBattleField()[shot.getX()][shot.getY()] = OFF_TARGET;
        shooter.setTurn(false);
        turn = true;
        System.out.println("Мимо! Переход хода.");
    }

    /**
     * Проверяет закончена ли игра.
     *
     * @return true- закончена (все корабли игрока уничтожены),
     * false - не закончена (есть корабли игрока в игре).
     */
    public boolean isGameOver() {
        boolean isLost = true;
        for (Ship s : ships) {
            isLost &= s.isShipKilled();
        }
        return isLost;
    }

    /**
     * Метод отмечает поля вокруг убитого корабля промахами * на поле битвы атакующего игрока.
     *
     * @param ship "убитый" корабль.
     */
    public void markKilledShip(int[][] ship) {
        int size = (ship.length + 2) * 3;
        int[][] roundShip = new int[size][2];
        int x = ship[0][0];
        int y = ship[0][1];
        int orientation;

        if (ship.length != 1) {
            orientation = ship[0][0] == ship[1][0] ? 1 : 2;
        } else {
            orientation = 1;
        }

        size = 0;

        if (orientation == 1) {
            for (int column = y - 1; column < y + ship.length + 1; column++) {
                for (int row = x - 1; row <= x + 1; row++) {
                    roundShip[size][0] = row;//координата x
                    roundShip[size][1] = column;//координата y
                    size++;
                }
            }
        } else {
            for (int row = x - 1; row < x + ship.length + 1; row++) {
                for (int column = y - 1; column <= y + 1; column++) {
                    roundShip[size][0] = row;//координата x
                    roundShip[size][1] = column;//координата y
                    size++;
                }
            }
        }

        for (int[] coupleXY : roundShip) {
            AutoPoint checkPoint = new AutoPoint();
            checkPoint.setX(coupleXY[0]);
            checkPoint.setY(coupleXY[1]);

            if (checkPoint.isInField()) {
                if (battleField[checkPoint.getX()][checkPoint.getY()] != INJURED) {
                    battleField[checkPoint.getX()][checkPoint.getY()] = OFF_TARGET;
                }
            }
        }
    }

    /**
     * Отчет по игре.
     *
     * @param playerLost проигравший игрок.
     */
    public void showGameReport(Player playerLost) {
        System.out.println("\nПобедил: " + name + "!!!");
        System.out.println("Расстановка победителя:");
        showPlayerField();
        System.out.println("\nПроиграл: " + playerLost.getName() + ".");
        System.out.println("Расстановка проигравшего:");
        playerLost.showPlayerField();

        int shot1 = countPercent(playerField, SHIP);
        int shot2 = 100 - (shot1 + countPercent(playerField, EMPTY));
        int shot3 = 100 - countPercent(playerLost.getPlayerField(), EMPTY);

        System.out.printf("\nИгрок: %s Сделано выстрелов: %d Точность: %.2f%%",
                name, shot3, 2000.0 / shot3);
        System.out.printf("\nИгрок: %s Сделано выстрелов: %d Точность: %.2f%%",
                playerLost.getName(), shot2, (2000.0 - 100 * shot1) / shot2);
    }

    /**
     * Подсчет статистики.
     *
     * @param field     поле подсчета.
     * @param countCell тип символа.
     * @return Кол-во символов данного типа на поле.
     */
    public int countPercent(char[][] field, char countCell) {
        int amount = 0;

        for (char[] rows : field) {
            for (char cell : rows) {

                if (cell == countCell) {
                    amount++;
                }
            }
        }
        return amount;
    }

    @Override
    public String toString() {
        return name + " ";
    }
}