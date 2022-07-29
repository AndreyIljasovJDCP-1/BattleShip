/**
 * Класс реализует методы генерации координат в АвтоРежиме.
 * Объект класса является полем <b>pointMode</b> класса Player
 * и определяет метод обработки координат этого игрока.
 */
public class AutoPoint extends Point {

    public void setRandomXY() {
        this.x = getRandomXY();
        this.y = getRandomXY();
    }
    @Override
    public void setNewXY(char[][] field) {
        int startIndex;
        int endIndex;
        StringBuilder row = new StringBuilder();
        StringBuilder column = new StringBuilder();
        String searchNow;
        int offsetRight;
        int offsetLeft;

        /* Формируем 2 строки 0-99 длина 100 с полем батл по горизонтали row, по вертикали column */

        for (int j = 0; j < SIZE; j++)
            for (int i = 0; i < SIZE; i++) {
                row.append(field[j][i]);
                column.append(field[i][j]);
            }

        /* По горизонтали окружим раненых ХХХХ, ХХХ и ХХ мнимыми * для следующих проверок. */

        for (int s = 0; s < INJURED_SHIP.length() - 1; s++) {

            searchNow = INJURED_SHIP.substring(s);

            for (int i = 0; i < SIZE; i++) {

                /* поиск вхождения ХХХХ, ХХХ и ХХ в подстроке(10 подстрок от 0 до 9) */

                String subRow = row.substring(SIZE * i, SIZE * (i + 1));
                startIndex = subRow.indexOf(searchNow);

                /* offsetRight-это смещение вправо, offsetLeft-смещение влево */

                while (startIndex > -1) {

                    endIndex = startIndex + searchNow.length();

                    if (startIndex % SIZE == 0) { // Найден корабль. Проверка впритык влево?
                        offsetRight = 1;
                        offsetLeft = 0;

                    } else if (endIndex == SIZE) { // Найден корабль. Проверка впритык вправо?
                        offsetRight = 0;
                        offsetLeft = 1;
                    } else {
                        offsetRight = 1;
                        offsetLeft = 1;
                    }

                    /* Добавляем сверху и снизу **** по длине, плюс на смещение влево и вправо. */

                    for (int r = startIndex - offsetLeft; r < endIndex + offsetRight; r++) {

                        if (i == 0) {
                            row.setCharAt(r + SIZE * (i + 1), OFF_TARGET);// 0 строка добавляем только ниже
                        } else if (i == 9) {
                            row.setCharAt(r + SIZE * (i - 1), OFF_TARGET);// 9 строка добавляем только выше
                        } else {
                            row.setCharAt(r + SIZE * (i + 1), OFF_TARGET);// если строка в серединке
                            row.setCharAt(r + SIZE * (i - 1), OFF_TARGET);// добавляем выше и ниже
                        }
                    }

                    startIndex = subRow.indexOf(searchNow, endIndex); // Поиск после найденного корабля.
                }

                /* По вертикали окружим раненых в линию ХХХХ, ХХХ и ХХ мнимыми * для следующих проверок. */

                String subColumn = column.substring(SIZE * i, SIZE * (i + 1));

                startIndex = subColumn.indexOf(searchNow);

                /* теперь offsetRight-это смещение вниз, offsetLeft-смещение вверх */

                while (startIndex > -1) {

                    endIndex = startIndex + searchNow.length();

                    if ((startIndex % SIZE == 0)) { // Найден корабль. Проверка впритык вверх?
                        offsetRight = 1;
                        offsetLeft = 0;
                    } else if (endIndex == SIZE) { // Найден корабль. Проверка впритык вниз?
                        offsetRight = 0;
                        offsetLeft = 1;
                    } else {
                        offsetRight = 1;
                        offsetLeft = 1;
                    }

                    for (int r = startIndex - offsetLeft; r < endIndex + offsetRight; r++) {
                        if (i == 0) {
                            column.setCharAt(r + SIZE * (i + 1), OFF_TARGET);
                        } else if (i == 9) {
                            column.setCharAt(r + SIZE * (i - 1), OFF_TARGET);
                        } else {
                            column.setCharAt(r + SIZE * (i + 1), OFF_TARGET);
                            column.setCharAt(r + SIZE * (i - 1), OFF_TARGET);
                        }
                    }
                    startIndex = subColumn.indexOf(searchNow, endIndex); // Поиск после найденного корабля.
                }

            }
        }

        /* Поиск раненых кораблей. */

        for (int s = 1; s < INJURED_SHIP.length(); s++) {

            searchNow = INJURED_SHIP.substring(s);

            for (int i = 0; i < SIZE; i++) {

                /* ищем по горизонтали XXX-,XX-,X- */

                String subRow = row.substring(SIZE * i, SIZE * (i + 1));
                startIndex = subRow.indexOf(searchNow + "-");
                while (startIndex > -1) {

                    endIndex = startIndex + searchNow.length();

                    if ((column.charAt(i + SIZE * (endIndex)) != OFF_TARGET)) { //можно атаковать

                        x = i;
                        y = endIndex;
                        return;
                    }
                    startIndex = subRow.indexOf(searchNow + "-", endIndex);
                }

                /* ищем по горизонтали -XXX,-XX,-X */

                startIndex = subRow.indexOf("-" + searchNow);
                while (startIndex > -1) {

                    if (column.charAt(i + SIZE * startIndex) != OFF_TARGET) { //можно атаковать

                        x = i;
                        y = startIndex;
                        return;
                    }
                    startIndex = subRow.indexOf("-" + searchNow, startIndex + searchNow.length());
                }

                /* ищем по вертикали XXX-,XX- */

                String subColumn = column.substring(SIZE * i, SIZE * (i + 1));
                startIndex = subColumn.indexOf(searchNow + "-");

                while (startIndex > -1) {
                    endIndex = startIndex + searchNow.length();
                    if ((row.charAt(i + SIZE * (endIndex)) != OFF_TARGET)) { //можно атаковать

                        x = endIndex;
                        y = i;
                        return;
                    }
                    startIndex = subColumn.indexOf(searchNow + "-", endIndex);
                }

                /* ищем по вертикали -XXX,-XX */

                startIndex = subColumn.indexOf("-" + searchNow);
                while (startIndex > -1) {

                    if ((row.charAt(startIndex * SIZE + i) != OFF_TARGET)) { //можно атаковать

                        x = startIndex;
                        y = i;
                        return;
                    }
                    startIndex = subColumn.indexOf("-" + searchNow, startIndex + searchNow.length());
                }
            }
        }

        /* Проверка рандомных координат по строкам и столбцам на свободную ячейку. */

        while (true) {
            int[] coordinates = new int[2];
            coordinates[0] = getRandomXY();
            coordinates[1] = getRandomXY();
            String horizontal = coordinates[0] + "" + coordinates[1];
            String vertical = coordinates[1] + "" + coordinates[0];
            int indexFindX = Integer.parseInt(horizontal);
            int indexFindY = Integer.parseInt(vertical);
            if (row.charAt(indexFindX) == EMPTY && column.charAt(indexFindY) == EMPTY) {
                x = coordinates[0];
                y = coordinates[1];
                return;
            }
        }
    }
}