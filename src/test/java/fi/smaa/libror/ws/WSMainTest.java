package fi.smaa.libror.ws;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.xmlbeans.XmlException;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import fi.smaa.libror.RORModel;

public class WSMainTest {

	private static final String FILE = "utagms-v2-input.xml";

	@Test
	public void testProcessInput() throws IOException, TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException, XmlException {
		URL url = Resources.getResource(FILE);
		String input = Resources.toString(url, Charsets.UTF_8);

		RORModel model = WSMain.processInput(input);
		assertEquals(2, model.getNrAlternatives());
		assertEquals(2, model.getNrCriteria());
		assertArrayEquals(new int[]{1, 0}, model.getPerfMatrix().getLevelIndices(0));
		assertArrayEquals(new int[]{0, 1}, model.getPerfMatrix().getLevelIndices(1));
		assertEquals(0, model.getPrefPairs().get(0).a);
		assertEquals(1, model.getPrefPairs().get(0).b);
		assertEquals(1, model.getPrefPairs().size());
	}
}
