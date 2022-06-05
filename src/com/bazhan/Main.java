package com.bazhan;

import com.sun.source.doctree.SeeTree;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.*;

public class Main {


    public static <T>void show(String title, IntStream stream){
        final int SIZE=10;
        int[] firstElements=stream.limit(SIZE+1).toArray();
        System.out.print(title+": ");
        for (int i=0; i<firstElements.length; i++){
            if (i>0) System.out.print(", ");
            if (i<SIZE) System.out.print(firstElements[i]);
            else System.out.print("...");
        }
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        Path path= Paths.get("C:/Users/Валентина/Desktop/corejava/gutenberg/alice30.txt");
        var contents=new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        //создать поток данных
        List<String> wordsOfList=List.of(contents.split("\\PL+"));


        //перебор слов в списке
        long count=0;
        for (String w:wordsOfList){
            if (w.length()>12) count++;
        }
        System.out.println(count);
        //создание потока данных
        count=wordsOfList.stream().filter(w->w.length()>12).count();
        System.out.println(count);
        //создание параллельного потока данных
        count=wordsOfList.parallelStream().filter(w->w.length()>12).count();
        System.out.println(count);

        //Преобразование коллекции в поток данных
        Stream<String> wordsOf=Stream.of(contents.split("\\PL+"));
        Show.show("words", wordsOf);
        //можно построить поток из любого количества аргументов
        Stream<String> song = Stream.of("gently", "down", "the", "stream");
        Show.show("song", song);
        //поток данных без элементов empty
        Stream<String> silence = Stream.empty();
        Show.show("silence", silence);
        //поток постоянных значений,
        Stream<String>echos=Stream.generate(()->"Echos");
        Show.show("echos", echos);
        //поток случайных значений
        Stream<Double> randoms = Stream.generate(Math::random);
        Show.show("randoms", randoms);
        //бесконечный поток данных, бесконечная последовательность
        Stream<BigInteger> integers=Stream.iterate(BigInteger.ZERO, n->n.add(BigInteger.ONE));
        Show.show("integers", integers);


        Path path1= Paths.get("C:/Users/Валентина/Desktop/corejava/gutenberg/alice30.txt");
        var contents1=new String(Files.readAllBytes(path1), StandardCharsets.UTF_8);
        //элементы являются частями входной последовательност символов, разделяемых по данному шаблону
        Stream<String> wordsAnotherWay = Pattern.compile("\\PL+").splitAsStream(contents1);
        Show.show("wordsAnotherWay", wordsAnotherWay);

        //поток данных, эелементы котрого составляют строки из указаного файла в кодировке UTF_8
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8))
        {
            Show.show("lines", lines);
        }

        Iterable<Path> iterable = FileSystems.getDefault().getRootDirectories();
        //возвращает разделяемый итератор для данного итерируемого объекта
        Stream<Path> rootDirectories = StreamSupport.stream(iterable.spliterator(), false);
        Show.show("rootDirectories", rootDirectories);

        Iterator<Path> iterator = Paths.get("/usr/share/dict/words").iterator();
        //Spliterator.ORDERED - константа в виде последовательности битов
        //возвращает разделяемый итератор неизвестного размера с хаарктеристиками
        Stream<Path> pathComponents = StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                iterator, Spliterator.ORDERED), false);
        Show.show("pathComponents", pathComponents);

        Stream<String> words=Stream.of(contents.split("\\PL+"));
        IntStream is1=IntStream.generate(()->(int) (Math.random()*100));
        show("is1", is1);
        //без верхнего предела
        IntStream is2=IntStream.range(5,10);
        show("is2", is2);
        //с верхним пределом
        IntStream is3=IntStream.rangeClosed(5,10);
        show("is3", is3);
        //поток объектов преобразовать в поток примитивных типов
        IntStream is4=words.mapToInt(String::length);
        show("is4", is4);
        //получает поток кодов символов в Юникоде
        var sentence = "\uD840\uDD46 is the set of octonions.";
        System.out.println(sentence);
        IntStream codes=sentence.codePoints();
        System.out.println(codes.mapToObj(c->String.format("%X",c)).collect(Collectors.joining()));
        //возвращает поток объектов-оболочек ждя элементов исходного потока данных
        Stream<Integer> integers2=IntStream.range(0,100).boxed();
        IntStream is5=integers2.mapToInt(Integer::intValue);
        show("is5", is5);

        List<String> wordsList=List.of(contents.split("\\PL+"));
        /*var shortWords=new int[10];
        wordsList.parallelStream().forEach(s->{
            if (s.length()<10) shortWords[s.length()]++;
        });
        System.out.println(Arrays.toString(shortWords));*/

        //сгруппировать и подсчитать результаты
        Map<Integer, Long> shortWordCount=wordsList
                .parallelStream()
                .filter(s->s.length()<10)
                .collect(groupingBy(String::length, counting()));
        System.out.println(shortWordCount);

        //нисходящий поток не детерминирован
        Map<Integer, List<String>> result=wordsList.parallelStream().collect(Collectors
                .groupingByConcurrent(String::length));
        System.out.println(result.get(5));

        Map<Integer, Long> wordCount=wordsList.parallelStream()
                .collect(groupingByConcurrent(String::length, counting()));

        System.out.println(wordCount);

    }
}
