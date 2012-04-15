package fi.smaa.libror.ws;

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
		// checks
	}
}
