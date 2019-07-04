package FunctionalPrograms;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class CompFutures {
    static <T> CompletableFuture<Boolean> all(Stream<CompletableFuture<T>> cfs, Predicate<T> pred) {
        return cfs
                .map(cf -> cf.thenApply(pred::test))
                .reduce((prev, curr) -> CompletableFuture.supplyAsync(()->prev.join()&&curr.join()))
                .get();
    }

    static <T> CompletableFuture<T> first(Stream<CompletableFuture<T>> cfs,Predicate<T> pred) {
        return cfs
                .reduce((prev, curr) -> pred.test(prev.join()) ? prev : curr)
                .orElse(completedFuture(null))
                ;
    }




    public static void main(String[] args) {

        System.out.println(first(Stream.of(CompletableFuture.supplyAsync(()->(2)),CompletableFuture.supplyAsync(()->2),CompletableFuture.supplyAsync(()->5),CompletableFuture.supplyAsync(()->7)),(t)->t>40).join());
        System.out.println(first(Stream.of(CompletableFuture.supplyAsync(()->(2)),CompletableFuture.supplyAsync(()->2),CompletableFuture.supplyAsync(()->5),CompletableFuture.supplyAsync(()->7)),(t)->t>40).join());



    }
}
