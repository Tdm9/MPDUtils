package FunctionalPrograms;

import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class App {

    public static void main(String[] args) {
        IntFunction<Stream<Integer>> sub =
                n -> Stream.of(n-1, n, n +1);
        Stream
                .iterate(1, n -> n + 10)
                .limit(3)
                .flatMap(sub::apply)
                .forEach(n -> System.out.print(n + ","));
    }

}
