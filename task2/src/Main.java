import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.naturalOrder;

public class Main {
    public static void main(String[] args) {
        Collection<Person> persons = generateCollection();
        showAmountUnderage(persons);
        List<String> listRecruit = getListRecruit(persons);
        List<Person> listWorkers = getSortedWorkersList(persons);
        //listWorkers.forEach(x-> System.out.println(x.toString()));
    }

    public static List<Person> getSortedWorkersList(Collection<Person> persons) {
        Comparator<Person> familyCmpr = Comparator.comparing(Person::getFamily, naturalOrder());

        //Однопоточный стрим
        System.out.println("Формирование листа работоспособных");
        long timeStream = System.currentTimeMillis();
        List<Person> list = persons.stream()
                .filter(x -> (((x.getAge() > 17 & x.getAge() < 66) & x.getSex() == Sex.MAN) | ((x.getAge() > 17 & x.getAge() < 61) & x.getSex() == Sex.WOMEN)))
                .filter(x -> x.getEducation() == Education.HIGHER)
                .sorted(familyCmpr)
                .collect(Collectors.toList());
        System.out.println("Многопоточный стрим: " + (System.currentTimeMillis() - timeStream) + " мс");

        //Многопоточный стрим
        long timeParallelStream = System.currentTimeMillis();
        List<Person> parallelList = persons.parallelStream()
                .filter(x -> (((x.getAge() > 17 & x.getAge() < 66) & x.getSex() == Sex.MAN) | ((x.getAge() > 17 & x.getAge() < 61) & x.getSex() == Sex.WOMEN)))
                .filter(x -> x.getEducation() == Education.HIGHER)
                .sorted(familyCmpr)
                .collect(Collectors.toList());
        System.out.println("Однопоточный стрим: " + (System.currentTimeMillis() - timeParallelStream) + " мс");
        return parallelList;
    }

    public static List<String> getListRecruit(Collection<Person> persons) {
        //Однопоточный стрим
        long timeStream = System.currentTimeMillis();
        System.out.println("Формирование листа призывников");
        List<String> list = persons.stream()
                .filter(x -> x.getAge() > 17 && x.getAge() < 28)
                .filter(x -> x.getSex() == Sex.MAN)
                .map(Person::getFamily)
                .collect(Collectors.toList());
        System.out.println("Однопоточный стрим: " + (System.currentTimeMillis() - timeStream) + " мс");

        //Параллельный стрим
        long timeParallelStream = System.currentTimeMillis();
        List<String> parallelList = persons.parallelStream()
                .filter(x -> x.getAge() > 17 && x.getAge() < 28)
                .filter(x -> x.getSex() == Sex.MAN)
                .map(Person::getFamily)
                .collect(Collectors.toList());
        System.out.println("Многопоточный стрим: " + (System.currentTimeMillis() - timeParallelStream) + " мс");
        return parallelList;
    }

    public static void showAmountUnderage(Collection<Person> persons) {
        //Однопоточный стрим для поиска всех несовершеннолетних
        long timeStream = System.currentTimeMillis();
        Stream<Person> stream = persons.stream();
        System.out.print("Несовершеннолетних: ");
        System.out.println(stream.filter(x -> x.getAge() < 18)
                .count());
        System.out.println("Однопоточный стрим: " + (System.currentTimeMillis() - timeStream) + " мс");

        //параллельный стрим для поиска всех несовершеннолетних
        long timeParallelStream = System.currentTimeMillis();
        Stream<Person> parallelStream = persons.parallelStream();
        System.out.print("Несовершеннолетних: ");
        System.out.println(parallelStream.filter(x -> x.getAge() < 18)
                .count());
        System.out.println("Многопоточный стрим: " + (System.currentTimeMillis() - timeParallelStream) + " мс");
    }

    public static Collection<Person> generateCollection() {
        List<String> names = Arrays.asList("Jack", "Connor", "Harry", "George", "Samuel", "John");
        List<String> families = Arrays.asList("Evans", "Young", "Harris", "Wilson", "Davies", "Adamson", "Brown");
        Collection<Person> persons = new ArrayList<>();
        for (int i = 0; i < 10_000_000; i++) {
            persons.add(new Person(
                    names.get(new Random().nextInt(names.size())),
                    families.get(new Random().nextInt(families.size())),
                    new Random().nextInt(100),
                    Sex.values()[new Random().nextInt(Sex.values().length)],
                    Education.values()[new Random().nextInt(Education.values().length)])
            );
        }
        return persons;
    }


}
