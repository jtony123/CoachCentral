package utilities;



import java.util.LinkedList;

/**
 * @author Anthony Jackson
 *
 *	CircularFifoBuffer does as the name suggests, 
 *	the limit is the max size of the buffer
 *
 */

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
