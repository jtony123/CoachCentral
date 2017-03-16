package utilities;

import java.util.LinkedList;

public class FIFOQueue extends LinkedList<Double>{
	
    private int limit;

    public FIFOQueue(int limit) {
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
    		total += (double)d;
    	}
		return total/super.size();
    }

}
