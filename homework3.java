/*
 * Напишите приложение, которое будет запрашивать у пользователя следующие данные, 
 * разделенные пробелом:
 * Фамилия Имя Отчество номер телефона
 * 
 * Форматы данных:
 * фамилия, имя, отчество - строки
 * номертелефона - целое беззнаковое число без форматирования
 * 1. Ввод всех элементов через пробел
 * 2. Приложение должно проверить введенные данные по количеству. 
 * Если количество не совпадает с требуемым, вернуть код ошибки, 
 * обработать его и показать пользователю сообщение, 
 * что он ввел меньше и больше данных, чем требуется.
 * 3. Приложение должно попытаться распарсить полученные значения и 
 * выделить из них требуемые параметры. 
 * 4. Если форматы данных не совпадают, нужно бросить исключение, 
 * соответствующее типу проблемы.
 * Можно использовать встроенные типы java и создать свои. 
 * Исключение должно быть корректно обработано, 
 * пользователю выведено сообщение с информацией, что именно неверно.
 * 5. Если всё введено и обработано верно, должен создаться файл с названием, 
 * равным фамилии, в него в одну строку должны записаться полученные данные, 
 * вида <Фамилия><Имя><Отчество><номер_телефона>
 * Однофамильцы должны записаться в один и тот же файл, в отдельные строки.
 * Не забудьте закрыть соединение с файлом.
 * При возникновении проблемы с чтением-записью в файл, 
 * исключение должно быть корректно обработано, 
 * пользователь должен увидеть стектрейс ошибки.
 */
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

   public class task_1 {
    public static void main(String[] args) {
        String[] myStr;
        // Существет проблемма с русскими символами в Scanner
        String encoding = System.getProperty("console.encoding", "UTF-8");
        Scanner scanner = new Scanner(System.in, encoding);

        while (true) {
            System.out.println("------------------");
            System.out.println(
                    "Введите строки в формате:\nФамилия Имя Отвество Телефон\nЧтобы закончить ввод данных - введите пустую строку.");
            myStr = GetDataFromUser(scanner);
            if (myStr == null) {
                System.out.println("Ввод данных завершен. Програма закончилась.");
                break;
            }
            SaveToFile(myStr);
            System.out.println("------------------");
            System.out.println("Введите следующую строку:");
        }

        scanner.close();
    }

    // Запись данных в файл
    public static void SaveToFile(String[] myStr) {
        String fileName = myStr[0] + ".txt";
        try {
            Files.write(Paths.get(fileName), (String.join(" ", myStr) + "\n").getBytes(),
                    StandardOpenOption.CREATE_NEW);
        } catch (FileAlreadyExistsException e) {
            System.out.println("Файл " + fileName + " существует. Открывем для дополнения.");
            try {
                Files.write(Paths.get(fileName), (String.join(" ", myStr) + "\n").getBytes(),
                        StandardOpenOption.APPEND);
            } catch (IOException err) {
                System.out.println(err.getMessage());
            }
        } catch (InvalidPathException ipe) {
            System.out.println("Неверный формат имени файла." + fileName);
            System.out.println("При вводе фамилии, нельзя использовать спец символы.");
        } catch (IOException e) {
            System.out.println("Ошибка записи файла.");
        }
    }

    // Ввод данных от пользователя.
    // Если введена пустая строка, то выходим из цикла и будем завершать программу.
    // Если введена строка, которая не соотвествует требованиям, тогда будут вызваны соответсвующие исключения.
    // После их работы, будет запрошен новый ввод данных.
    // Если данные корректные, то выходим из запроса и возвращаем массив из 4х
    // элементов.
    public static String[] GetDataFromUser(Scanner scanner) {
        String[] result = null;
        String myStr = "";

        while (true) {
            System.out.println("Введите строку: ");
            myStr = scanner.nextLine();

            if (myStr.length() == 0)
                break; // выход из ввода данных возращаем null;
            result = GetArrayString(myStr);

            if (result != null)
                if (GetPhoneNumber(result[3]))
                    break; // выхрд из ввода данных возвращаем правильный массив
            System.out.println("Повторите ввод строки.");
        }
        return result;
    }

    // полученную строку и проверяем если делиться меньше чем на 4 слова,
    // то вызывает exception, что колво должно быть больше 4х
    public static String[] GetArrayString(String str) {
        String[] result = null;
        try {
            result = str.strip().split(" ");
            if (result.length != 4) {
                throw new MyCountException(
                        "Ошибка! Количество введенных данных неверно.\nДолжно быть 4 элемента разделенных пробелом");
            }
        } catch (MyCountException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return result;
    }

    // метод проверяет является ли переданная строка телефоном. т.е приводиться ли к
    // целому числу long.
    // Если приводиться, тогда возвращается true, в противном случае false;
    public static boolean GetPhoneNumber(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Ошибка! Номер телефона должен состоять только из цифр!");
            return false;
        }
    }


class MyCountException extends RuntimeException {
    public MyCountException(String s) {
        super(s);
    }
}
   }
   