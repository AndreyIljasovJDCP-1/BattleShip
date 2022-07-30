/**
 * Класс реализует метод получения координат от пользователя.
 * Объект класса является полем <b>pointMode</b> класса Player
 * и определяет метод обработки координат этого игрока.
 */
public class UserPoint extends Point {

    @Override
    public void setNewXY(char[][] field) {
        boolean notInField;

        do {
            System.out.println("Введите координаты в формате: A1 (A-J)вертикаль (1-10)горизонталь");
            String input = scanner.nextLine();

            if (input.length() < 2) {
                notInField = true;
                System.out.println("Некорректные координаты. Повторите ввод.");
                continue;
            }

            if (!input.contains(" ")) {
                StringBuilder sb = new StringBuilder(input);
                input = sb.insert(1, ' ').toString();
            }

            try {
                x = Integer.parseInt(input.split(" ")[1]) - 1;
                y = Character.isUpperCase(input.charAt(0)) ? input.charAt(0) - 65
                        : Character.toUpperCase(input.charAt(0)) - 65;
            } catch (NumberFormatException e) {
                System.out.println("Некорректные координаты. Повторите ввод!");
                notInField = true;
                continue;
            }

            if (!isInField()) {
                System.out.println("Координаты вне поля. Повторите ввод.");
                notInField = true;
                continue;
            }

            if (isBusyField(field)) {
                System.out.println("Координаты заняты. Повторите ввод.");
                notInField = true;
            } else {
                notInField = false;
            }

        } while (notInField);
    }

    /**
     * В этом классе не реализован
     */
    @Override
    public void setRandomXY() {
    }
}
