package FunctionalPrograms;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class App {

    public static void main(String[] args) {
        //Query.of(7,7,9,11,11,3,11,11,9,7).takeWhile2(n->n!=9).forEach(System.out::println);
        Object[] yy=Query.of(7,7,9,11,9).takeWhile2(n->n!=5).array;

        System.out.println(yy[0]);
        System.out.println(yy[1]);
        System.out.println(yy[2]);
        System.out.println(yy[3]);
        System.out.println(yy[4]);
        System.out.println(Query.first(Stream.of(CompletableFuture.supplyAsync(()->(2)),CompletableFuture.supplyAsync(()->2),CompletableFuture.supplyAsync(()->5),CompletableFuture.supplyAsync(()->7)),(t)->t>40).join());

        //Query.of(6,2,8,23,6,8,9).zip(Query.of(4,72,6,4),(q1,q2)->q1+q2).forEach(System.out::println);
        System.out.println(Query.first(Stream.of(CompletableFuture.supplyAsync(()->(2)),CompletableFuture.supplyAsync(()->2),CompletableFuture.supplyAsync(()->5),CompletableFuture.supplyAsync(()->7)),(t)->t>40).join());



    }

}
