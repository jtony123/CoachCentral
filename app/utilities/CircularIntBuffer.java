package utilities;




import java.util.LinkedList;

/**
 * @author Anthony Jackson
 *
 *	CircularFifoBuffer does as the name suggests, 
 *	the limit is the max size of the buffer
 *
 */

public class CircularIntBuffer<Integer> extends LinkedList<Integer> {
    private int limit;

    public CircularIntBuffer(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(Integer i) {
        super.add(i);
        while (size() > limit) { super.remove(); }
        return true;
    }
}
