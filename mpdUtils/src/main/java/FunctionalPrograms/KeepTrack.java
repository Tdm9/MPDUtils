package FunctionalPrograms;


import java.util.function.Consumer;

public class KeepTrack<T> implements Query {
    public final T[] array;
    private int origin; // current index, advanced on split or traversal
    private final int fence; // one past the greatest index
    public boolean end=false;
    public boolean stop=true;



    public KeepTrack(T[] array,int origin,int fence){
        this.array = array; this.origin = origin; this.fence = fence;
    }

    @Override
    public boolean tryAdvance(Consumer action) {
        if (origin < fence) {
            action.accept((T) array[origin]);
            origin += 1;
            return true;
        }
        else // cannot advance
        {
            end=!end;
            stop=!stop;
            return false;
        }

    }

}
