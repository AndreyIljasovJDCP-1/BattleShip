import java.util.Scanner;

/**
 * Абстрактный класс для диалога с пользователем.
 */
public abstract class Menu {

    public static final Scanner scanner = new Scanner(System.in);

    /**
     * Диалог с пользователем.
     * Регистрация нового игрока.
     *
     * @param number порядковый номер
     * @return Player новый игрок
     */

    public static Player addPlayer(int number) {
        String input;

        do {
            System.out.println("Введите имя " + number + "-го игрока: ");
            input = scanner.nextLine().replaceAll(" ", "");
        } while (input.length() == 0);

        return new Player(input);
    }

    /**
     * Диалог с пользователем.
     * Выбор скорости показа полей в авторежиме.
     *
     * @return int число (1-4) - выбранный пункт меню
     */

    public static int selectDelayMode() {
        int output;

        do {
            System.out.println("\nВыберите скорость показа полей игры: ");
            System.out.println("0. Без паузы.");
            System.out.println("1. Быстро.");
            System.out.println("2. Средне.");
            System.out.println("3. Медленно.");
            System.out.println("4. Выход.");
            String input = scanner.nextLine();
            input = input.replaceAll(" ", "");

            if (input.length() != 1) {
                System.out.println("Некорректные данные. Введите 0, 1, 2, 3 или 4.");
                output = 0;
                continue;
            }

            try {
                output = Integer.parseInt(input);

                if (output > 4 || output < 0) {
                    System.out.println("Некорректные данные. Введите 0, 1, 2, 3 или 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Некорректные данные. Введите 0, 1, 2, 3 или 4.");
                output = 0;
            }
        } while (output > 4 || output < 0);

        return output;
    }

    /**
     * Диалог с пользователем.
     * Выбор режима игры.
     *
     * @return int число (1-4) - выбранный пункт меню
     */

    public static int selectGameMode() {
        int output;

        do {
            System.out.println("\nВыберите режим игры: ");
            System.out.println("1. Человек vs Человек.");
            System.out.println("2. Человек vs Компьютер.");
            System.out.println("3. Компьютер vs Компьютер.");
            System.out.println("4. Выход.");
            String input = scanner.nextLine();
            input = input.replaceAll(" ", "");

            if (input.length() != 1) {
                System.out.println("Некорректные данные. Введите 1, 2, 3 или 4.");
                output = 0;
                continue;
            }

            try {
                output = Integer.parseInt(input);

                if (output > 4 || output < 1) {
                    System.out.println("Некорректные данные. Введите 1, 2, 3 или 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Некорректные данные. Введите 1, 2, 3 или 4.");
                output = 0;
            }
        } while (output > 4 || output < 1);

        return output;
    }

    /**
     * Диалог с пользователем.
     * Выбор режима расстановки кораблей или режима стрельбы.
     *
     * @param name          имя игрока
     * @param selectingMode режим расстановки или стрельбы
     * @return int число (1-3) - выбранный пункт меню
     */

    public static int selectMode(String name, String selectingMode) {
        int output;

        System.out.println("\n" + name + " выберите " + selectingMode + ":");

        do {
            System.out.println("1. Вручную.");
            System.out.println("2. Авто.");
            System.out.println("3. Выход.");
            String input = scanner.nextLine();
            input = input.replaceAll(" ", "");

            if (input.length() != 1) {
                System.out.println("Некорректные данные. Введите 1, 2 или 3.");
                output = 0;
                continue;
            }

            try {
                output = Integer.parseInt(input);

                if (output > 3 || output < 1) {
                    System.out.println("Некорректные данные. Введите 1, 2, 3 или 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Некорректные данные. Введите 1, 2 или 3.");
                output = 0;
            }
        } while (output > 3 || output < 1);

        return output;
    }

    /**
     * Диалог с пользователем.
     * Выбор ориентации корабля для ручной расстановки.
     *
     * @return int число (1-3) - выбранный пункт меню
     */

    public static int selectOrientation() {
        int output;

        do {
            System.out.println("Выберите ориентацию корабля:");
            System.out.println("1. Горизонтальная.");
            System.out.println("2. Вертикальная.");
            System.out.println("3. Выход");
            String input = scanner.nextLine();
            input = input.replaceAll(" ", "");

            if (input.length() != 1) {
                System.out.println("Некорректные данные.Введите 1, 2 или 3.");
                output = 0;
                continue;
            }

            try {
                output = Integer.parseInt(input);

                if (output > 3 || output < 1) {
                    System.out.println("Некорректные данные. Введите 1, 2, 3 или 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Некорректные данные.Введите 1, 2 или 3.");
                output = 0;
            }
        } while (output > 3 || output < 1);

        return output;
    }

    /**
     * Получение пары случайных имен для режима игры: Компьютер vs Компьютер.
     *
     * @return массив String из двух имен
     */

    public static String[] getComputerNames() {
        String[] computerNames = {"SkyNet", "Matrix", "R2-D2", "Deep Blue",
                "C-3PO", "JARVIS", "VIKI", "Optimus Prime", "Megatron", "Siri"};
        String[] pairNames = new String[2];

        do {
            pairNames[0] = computerNames[getRandomXY()];
            pairNames[1] = computerNames[getRandomXY()];
        } while (pairNames[0].equals(pairNames[1]));

        return pairNames;
    }

    /**
     * @return int случайное число от 0 до 9
     */

    public static int getRandomXY() {
        return (int) (Math.random() * 10);
    }
}
