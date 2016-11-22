package utilities;



import java.util.LinkedList;

public class CircularFifoBuffer<E> extends LinkedList<E> {
    private int limit;

    public CircularFifoBuffer(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(E o) {
        super.add(o);
        while (size() > limit) { super.remove(); }
        return true;
    }
}
