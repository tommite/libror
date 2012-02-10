package fi.smaa.libror;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class PartialValueFunctionTest {
	
	private PartialValueFunction f;


	@Before
	public void setUp() {
		f = new PartialValueFunction(3);
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
		PartialValueFunction newf = f.deepCopy();
		assertTrue(Arrays.equals(newf.getValues(), f.getValues()));
	}
	
	@Test
	public void testEquals() {
		f.setValue(1, 0.5);
		PartialValueFunction f2 = new PartialValueFunction(3);
		assertFalse(f.equals(f2));
		f2.setValue(1, 0.5);
		assertEquals(f, f2);
	}
}
