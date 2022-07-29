/**
 *
 * <h1> Игра "Морской бой"</h1>
 *
 * <p>Правила игры - общепринятые.
 * <p>Право первого хода у игрока №1.
 * <p>При промахе противник сообщает игроку: Мимо.
 * <p>При не смертельном попадании противник сообщает игроку: Ранен.
 * <p>При смертельном попадании противник сообщает игроку: Убит!
 * <p>При удачном выстреле, игрок стреляет еще раз и т.д.
 * <p>При промахе очередь переходит к другому игроку.
 * <p>Выигрывает игрок, первым потопивший все корабли соперника.
 *
 * @version 3.3
 */

public class Main {

    public static final char EMPTY = '-';

    public static void main(String[] args) throws Exception {

        Player playerFirst;
        Player playerSecond;
        System.out.println("\n          ИГРА\" Морской бой\".");

        /* Выбор режима игры: (1,2,3, 4-выход) и инициализация игроков. */
        int select = Menu.selectGameMode();

        switch (select) {
            case 1:

                playerFirst = Menu.addPlayer(1);
                playerFirst.setTurn(true);
                playerSecond = Menu.addPlayer(2);

                Point.fillField(playerFirst.getPlayerField(), EMPTY);
                Point.fillField(playerSecond.getPlayerField(), EMPTY);

                /* Выбор режима расстановки кораблей и режима стрельбы: (1-вручную,2-авто,3-выход) */

                if (!playerFirst.selectModeShipsPlace()
                        || !playerSecond.selectModeShipsPlace()
                        || !playerFirst.selectShootingMode()
                        || !playerSecond.selectShootingMode()) {

                    endProgramme();
                    return;
                }

                System.out.println("\nПоехали!");
                Thread.sleep(1000);
                break;
            case 2:

                playerFirst = Menu.addPlayer(1);
                playerFirst.setTurn(true);
                playerSecond = new Player(Menu.getComputerNames()[0]);

                Point.fillField(playerFirst.getPlayerField(), EMPTY);
                Point.fillField(playerSecond.getPlayerField(), EMPTY);

                if (!playerFirst.selectModeShipsPlace() || !playerFirst.selectShootingMode()) {
                    endProgramme();
                    return;
                }

                System.out.println("\n" + playerSecond.getName() + " расставит корабли самостоятельно.");

                playerSecond.placeShipsAutoMode();
                System.out.println("\nПоехали!");
                Thread.sleep(1000);
                break;
            case 3:

                String[] computerNames = Menu.getComputerNames();

                playerFirst = new Player(computerNames[0]);
                playerFirst.setTurn(true);

                playerSecond = new Player(computerNames[1]);

                Point.fillField(playerFirst.getPlayerField(), EMPTY);
                Point.fillField(playerSecond.getPlayerField(), EMPTY);

                playerFirst.placeShipsAutoMode();
                Thread.sleep(1000);

                playerSecond.placeShipsAutoMode();
                System.out.println("\nПоехали!");
                Thread.sleep(1000);
                break;
            default:
                endProgramme();
                return;
        }

        

        Point.fillField(playerFirst.getBattleField(), EMPTY);
        Point.fillField(playerSecond.getBattleField(), EMPTY);



        /* Блок стрельбы. Непосредственно игра.*/

        while (!(playerFirst.isGameOver() || playerSecond.isGameOver())) {

            playerFirst.openFire(playerSecond);
            playerSecond.openFire(playerFirst);
        }
        /* Блок завершения игры. Вывод результатов. */

        if (playerFirst.getTurn()) {
            playerFirst.showGameReport(playerSecond);
        } else {
            playerSecond.showGameReport(playerFirst);
        }

        endProgramme();
    }

    public static void endProgramme() {
        System.out.println("\n\nДо свиданья! Возвращайтесь снова!");
        System.out.println("\n          GAME OVER.");
    }
}