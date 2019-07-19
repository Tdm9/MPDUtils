package FunctionalPrograms;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.*;
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

    public default List<T> toList() {
        List<T> list = new ArrayList<>();
        while (tryAdvance(list::add));
        return list;
    }

    default Query<T> Union(Query<T> other) {
        List<T> elems = new ArrayList<>();
        List<T> elems2= new ArrayList<>();
        boolean[] first = {true};
        return cons -> {
            if (first[0] == true) {
                elems.addAll(this.toList());
                first[0] = false;
            }
            boolean[] found = {false};
            while (other.tryAdvance(e -> {
                if (elems.contains(e)&& !elems2.contains(e)) {
                    cons.accept(e);
                    elems2.add(e);
                    found[0] = true;
                }
            }) && !found[0]) ;
            return found[0];
        };
    }

    public default Query<T> limit(long maxSize) {
        final int[] count = {0};
        return action -> count[0]++ < maxSize ? tryAdvance(action) : false;
    }

    public static <T> Query<T> generate(Supplier<T> generator) {
        return act ->{
            try {
                act.accept(generator.get());
            }catch (Exception e){
                return false;
            }
            return true;
        };
    }

    public static <T> Query<T> iter(T seed, UnaryOperator<T> accumulator){
        T[] ss= (T[])new Object[]{seed};
        return action-> {
            action.accept(ss[0]); return (ss[0]=accumulator.apply(ss[0]))!=seed;
        };
    }

    public default Query<T> skipWhile(Predicate<T> condition){
        boolean[] b={true};
        return action -> tryAdvance(t->{
                if (!(b[0]=condition.test(t))) action.accept(t);

        }
        );


    }

    public default <R> Query<R> map(Function<T, R> mapper) {
        return action -> tryAdvance(value-> action.accept(mapper.apply(value)));
    }

    public default Query<T> slice(int from, int to) {
        int [] idx={0};
        return action-> tryAdvance(value->{
            if (from<(++idx[0]) && idx[0]<=to) action.accept(value);
        });
    }





    public static void main(String[] args) {
        //Query.of(7,7,9,11,11,3,11,11,9,7).takeWhile2(n->n!=9).forEach(System.out::println);
        //Query.iter(2, prev -> prev*2).limit(10).forEach(n -> System.out.print(n + " "));
        int[] n={5};

        Query.generate(() -> {n[0] -= 1;return 10/n[0];}).map(v -> v + ",").forEach(System.out::print);
        /*Query
                .of(2,4,8,16,32,62,128)
                .map(val -> val + ",")
                .slice(3,6)
                .forEach(System.out::print);*/
        //Query.of(1, 2, 3, 4, 5).skipWhile(n -> n < 3).forEach(n -> System.out.print(n + " "));
        /*Object[] yy=Query.of(7,7,9,11,9).takeWhile2(n->n!=5).array;

        System.out.println(yy[0]);
        System.out.println(yy[1]);
        System.out.println(yy[2]);
        System.out.println(yy[3]);
        System.out.println(yy[4]);
        Query.of(6,2,8,23,6,8,9).zip(Query.of(4,72,6,4),(q1,q2)->q1+q2).forEach(System.out::println);*/

    }



}

