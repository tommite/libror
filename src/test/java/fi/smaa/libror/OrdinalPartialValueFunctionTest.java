package fi.smaa.libror;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class OrdinalPartialValueFunctionTest {
	
	private OrdinalPartialValueFunction f;


	@Before
	public void setUp() {
		f = new OrdinalPartialValueFunction(3);
	}
	

	@Test
	public void testConstructor() {
		assertArrayEquals(new double[] {0.0, 0.0, 1.0}, f.getValues(), 0.000001);
	}
	
	@Test
	public void testSetValue() {
		f.setValue(1, 0.8);
		assertArrayEquals(new double[] {0.0, 0.8, 1.0}, f.getValues(), 0.000001);
	}
	
	@Test
	public void testDeepCopy() {
		f.setValue(1, 0.8);
		OrdinalPartialValueFunction newf = f.deepCopy();
		assertTrue(Arrays.equals(newf.getValues(), f.getValues()));
	}
}
