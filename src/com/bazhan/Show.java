package com.bazhan;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Show {
    public static <T>void show(String title, Stream<T> stream){
        final int SIZE=10;
        List<T> firstElements=stream.limit(SIZE+1).collect(Collectors.toList());
        System.out.print(title+": ");
        for (int i=0; i<firstElements.size(); i++){
            if (i>0) System.out.print(", ");
            if (i<SIZE) System.out.print(firstElements.get(i));
            else System.out.print("...");
        }
        System.out.println();
    }
}
