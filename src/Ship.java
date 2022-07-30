/**
 * Создает, хранит и обрабатывает информацию о корабле.
 */
public class Ship {
    /**
     * Статус состояния корабля, содержит палубы (клетки)
     * корабля со значениями: true - ранен, false - не было попаданий.
     */
    private final boolean[] shipStatus;
    /**
     * Полные координаты корабля.
     */
    private final int[][] shipCoordinates;

    /**
     * Конструктор нового корабля.
     *
     * @param placePoint  координаты первой палубы (клетки).
     * @param orientation ориентация корабля.
     * @param longShip    длина корабля.
     */
    public Ship(Point placePoint, int orientation, int longShip) {
        this.shipStatus = new boolean[longShip];
        this.shipCoordinates = new int[longShip][2];

        if (orientation == 1) {
            for (int i = 0; i < longShip; i++) {
                this.shipCoordinates[i][0] = placePoint.getX();
                this.shipCoordinates[i][1] = placePoint.getY() + i;
            }
        } else {
            for (int i = 0; i < longShip; i++) {
                this.shipCoordinates[i][0] = placePoint.getX() + i;
                this.shipCoordinates[i][1] = placePoint.getY();
            }
        }
    }

    /**
     * Метод проверки попадания по кораблю по координатам.
     *
     * @param shot координаты выстрела.
     * @return true-есть попадание, false-мимо.
     */
    public boolean isShipInjured(Point shot) {
        for (int i = 0; i < shipCoordinates.length; i++) {

            if ((shipCoordinates[i][0] == shot.getX()) && (shipCoordinates[i][1] == shot.getY())) {
                shipStatus[i] = true;
                return true;
            }
        }
        return false;
    }

    /**
     * Метод проверки статуса корабля.
     *
     * @return true - все палубы имеют ранения
     * false - есть палубы без ранений
     */
    public boolean isShipKilled() {
        boolean isKilled = true;
        for (Boolean shipDeck : shipStatus) {
            isKilled &= shipDeck;
        }
        return isKilled;
    }

    public int[][] getShipCoordinates() {
        return shipCoordinates;
    }

}
