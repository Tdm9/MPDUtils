package FunctionalPrograms;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
@FunctionalInterface
public interface MyToIntFunction<T> {

    int applyAsInt(T path) ;

    default MyToIntFunction<T> sumAll(MyToIntFunction<T>... others){

        return path -> {
            int sum = applyAsInt(path);
            for (MyToIntFunction<T> func:others) sum += func.applyAsInt(path);
            return sum;
        };
    }

    default MyToIntFunction<T> andThen(MyToIntFunction<Integer>... after){

        return path -> {
            int te=applyAsInt(path);
            for (MyToIntFunction<Integer> func:after){
                te =func.applyAsInt(te);
            }
            return te;
        };
    }

}
