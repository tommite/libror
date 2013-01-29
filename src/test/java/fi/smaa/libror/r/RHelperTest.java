package fi.smaa.libror.r;

import org.apache.commons.math.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class RHelperTest {

	@Test
	public void testRArrayMatrixToRealMatrix() {
		double[] data = new double[]{1.0, 4.0, 2.0, 5.0, 3.0, 6.0};
		RealMatrix mat = RHelper.rArrayMatrixToRealMatrix(data, 2);
		assertArrayEquals(new double[] {1.0, 2.0, 3.0}, mat.getRow(0), 0.0001);
		assertArrayEquals(new double[] {4.0, 5.0, 6.0}, mat.getRow(1), 0.0001);
	}
}
