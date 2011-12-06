package fi.smaa.libror;

import java.util.ArrayList;
import java.util.List;

public class FullValueFunction {
	
	private List<PartialValueFunction> vfs = new ArrayList<PartialValueFunction>();

	public FullValueFunction() {
	}
	
	public void addValueFunction(PartialValueFunction v) {
		vfs.add(v);
	}
	
	public String toString() {
		String retStr = "";
		for (PartialValueFunction vf : vfs) {
			retStr += vf.toString() + "\n";
		}
		return retStr;
	}
	
	public List<PartialValueFunction> getPartialValueFunctions() {
		return vfs;
	}
	
	public double evaluate(double[] point) {
		assert(point.length == vfs.size());
		
		double sum = 0.0;
		for (int i=0;i<point.length;i++) {
			sum += vfs.get(i).evaluate(point[i]);
		}
		return sum;
	}
}
