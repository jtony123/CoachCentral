package utilities;




import java.util.LinkedList;

/**
 * @author Anthony Jackson
 *
 *	CircularFifoBuffer does as the name suggests, 
 *	the limit is the max size of the buffer
 *
 */

public class CircularDoubleBuffer<Double> extends LinkedList<Double> {
    private int limit;

    public CircularDoubleBuffer(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(Double d) {
        super.add(d);
        while (size() > limit) { super.remove(); }
        return true;
    }
    
    public double getAverage() {
    	
    	double total = 0;
    	for (Object d : this.toArray()){
    		//System.out.println("size " + super.size() +" - "+d);
    		
    		total += (double)d;
    		
    	}
		return total/super.size();
		
		
    }
}
