package FunctionalPrograms;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Query<T> {
    boolean tryAdvance(Consumer<T> action);

    public static <T> Query<T> of(T... src) {
        int[] idx = {0};
        return action -> {
            if (idx[0] >= src.length) return false;
            action.accept(src[idx[0]]);
            return idx[0]++ < src.length;
        };
    }

	default Query<T> takeWhile(Predicate<T> p){
        boolean[] stop={false};
        return action -> tryAdvance(t -> {
            if (p.test(t)&& !stop[0]){
                action.accept(t);
            }else {
                stop[0]=true;
            }
        });
    }

    default KeepTrack<T> takeWhile2(Predicate<T> pred){
        List<T> values=new ArrayList<>();
        this.forEach(values::add);
        Query<T> qr=new KeepTrack<T>((T[])values.toArray(),0,values.size()).takeWhile(pred);
        values=new ArrayList<>();
        qr.forEach(values::add);
        return new KeepTrack<T>((T[])values.toArray(),0,values.size());


    }

    public default void forEach(Consumer<T> action) {
        while (tryAdvance(action)) ;
    }


    default Query<T> concat(Query<T> other) {
        return cons -> {
            forEach(cons::accept);
            other.forEach(cons::accept);
            return false;
        };

    }

    default <R> Query<R> zip(Query<T> other, BiFunction<T, T, R> join) {
        return cons -> {
            forEach(t->{if (!other.tryAdvance(t2 -> cons.accept(join.apply(t, t2)))) cons.accept((R) t); });
            return false;
        };
    }

    static <T> CompletableFuture<Boolean> all(Stream<CompletableFuture<T>> cfs,Predicate<T> pred) {
        return cfs
                .map(cf -> cf.thenApply(pred::test))
                .reduce((prev, curr) -> CompletableFuture.supplyAsync(()->prev.join()&&curr.join()))
                    /*boolean b[] = {true};
                    prev.thenApplyAsync(t->{b[0]&=t;return t;});
                    curr.thenApplyAsync(t->{b[0]&=t;return t;});
                    return CompletableFuture.supplyAsync(() -> b[0]);*/
                .get();
    }

    static <T> CompletableFuture<T> first(Stream<CompletableFuture<T>> cfs,Predicate<T> pred) {
        return cfs
                .reduce((prev, curr) -> pred.test(prev.join()) ? prev : curr)
                .orElse(CompletableFuture.completedFuture(null))
                ;
    }

}
