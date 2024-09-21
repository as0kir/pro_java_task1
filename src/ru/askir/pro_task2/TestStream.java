package ru.askir.pro_task2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class TestStream {

    // Реализуйте удаление из листа всех дубликатов
    private static <E> List<E> deleteCopy(List<E> list){
        return list.stream()
                .collect(Collectors.toSet())
                .stream()
                .toList();
    }

    // Найдите в списке целых чисел 3-е наибольшее число (пример: 5 2 10 9 4 3 10 1 13 => 10)
    private static Integer findMax(List<Integer> list){
        return list.stream()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .findAny()
                .get();
    }

    // Найдите в списке целых чисел 3-е наибольшее «уникальное» число (пример: 5 2 10 9 4 3 10 1 13 => 9, в отличие от прошлой задачи здесь разные 10 считает за одно число)
    private static Integer findUniqMax(List<Integer> list) {
        return list.stream()
                .collect(Collectors.toSet())
                .stream()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .findAny()
                .get();
    }

    // Имеется список объектов типа Сотрудник (имя, возраст, должность), необходимо получить список имен 3 самых старших сотрудников с должностью «Инженер», в порядке убывания возраста
    private static List<String> findOldestEngineer(List<Employee> list){
        return list.stream()
                .filter(employee -> employee.getPost().equals("Инженер"))
                .sorted(Comparator.comparingInt(Employee::getAge).reversed())
                .limit(3)
                .map(employee -> employee.getName())
                .toList();
    }

    // Имеется список объектов типа Сотрудник (имя, возраст, должность), посчитайте средний возраст сотрудников с должностью «Инженер»
    private static Double avgAgeEngineer(List<Employee> list){
        return list.stream()
                .filter(employee -> employee.getPost().equals("Инженер"))
                .mapToInt(Employee::getAge)
                .average().getAsDouble();
    }

    // Найдите в списке слов самое длинное
    private static String findLongestWord(String str) {
        return Arrays.stream(str.split(" "))
                .sorted(Comparator.comparingInt(String::length).reversed())
                .findAny()
                .get();
    }

    // Имеется строка с набором слов в нижнем регистре, разделенных пробелом. Постройте хеш-мапы, в которой будут хранится пары: слово - сколько раз оно встречается во входной строке
    private static Map<String, Long> stringToMap(String str) {
        return Arrays.stream(str.split(" "))
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));
    }

    // Отпечатайте в консоль строки из списка в порядке увеличения длины слова, если слова имеют одинаковую длины, то должен быть сохранен алфавитный порядок
    private static void printStrings(List<String> list) {
        list.stream()
                .sorted(Comparator.comparing(String::length).thenComparing(String::compareTo))
                .forEach(s -> System.out.println(s));
    }

    // Имеется массив строк, в каждой из которых лежит набор из 5 слов, разделенных пробелом, найдите среди всех слов самое длинное, если таких слов несколько, получите любое из них
    private static String findMaxFromFive(List<String> list) {
        return list.stream()
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .sorted(Comparator.comparingInt(String::length).reversed())
                .findAny().get();
    }

    public static void main(String[] args) {
        System.out.println(deleteCopy(List.of(1, 2, 2, 3, 5, 6)));
        System.out.println(deleteCopy(List.of("Вася", "Петя", "Ваня", "Вася")));

        System.out.println(findMax(List.of(5, 2, 10, 9, 4, 3, 10, 1, 13)));

        System.out.println(findUniqMax(List.of(5, 2, 10, 9, 4, 3, 10, 1, 13)));

        List<Employee> employees = List.of(
                new Employee("Иванов", "Инженер", 45),
                new Employee("Петров", "Инженер", 34),
                new Employee("Сидоров", "Бухгалтер", 23),
                new Employee("Медведев", "Инженер", 55),
                new Employee("Литл", "Инженер", 21)
                );

        System.out.println(findOldestEngineer(employees));

        System.out.println(avgAgeEngineer(employees));

        System.out.println(findLongestWord("найдите в списке слов самое длинное"));

        System.out.println();
        System.out.println(stringToMap("найдите в списке слов самое длинное найдите в списке слов найдите найдите"));

        System.out.println();
        printStrings(List.of(
                "Реализуйте удаление из листа всех дубликатов",
                "Найдите в списке целых чисел 3-е наибольшее число (пример: 5 2 10 9 4 3 10 1 13 => 10)",
                "Найдите в списке целых чисел 3-е наибольшее «уникальное» число (пример: 5 2 10 9 4 3 10 1 13 => 9, в отличие от прошлой задачи здесь разные 10 считает за одно число)",
                "Имеется список объектов типа Сотрудник (имя, возраст, должность), необходимо получить список имен 3 самых старших сотрудников с должностью «Инженер», в порядке убывания возраста",
                "Найдитееее удаление из листа всех дубликатов",
                "Имеется список объектов типа Сотрудник (имя, возраст, должность), посчитайте средний возраст сотрудников с должностью «Инженер»",
                "Найдите в списке слов самое длинное"));

        System.out.println();
        System.out.println(findMaxFromFive(
                List.of(
                        "Реализуйте удаление из листа всех",
                        "Найдите в списке целых чисел",
                        "Имеется список объектов типа Сотрудник"
                )
        ));
    }
}
