package FunctionalPrograms;

import io.reactivex.internal.util.ArrayListSupplier;

import java.time.LocalDate;
import java.util.Map;
import java.util.function.*;

public class MethodReferences<T,R> {
    public void ref(){
        Map<Integer, String> cnt = Map.of(3,"madkmadkmd");
        BiConsumer<Integer,String> putter = cnt::put;
        Function<T,String> getter = cnt::get;
        Supplier dum = cnt.get(11)::hashCode;
        Consumer<Map> eraser = Map::clear;
        Consumer<Map> addToMap= cnt::putAll;

        String slb = "Mala Ciao";
        Supplier a = LocalDate::now;
        ToIntFunction<String> b = String::length;
        Supplier c = slb::length;
        Supplier d = StringBuilder::new;
        Function<Integer,String[]> e = String[]::new;
    }
}
