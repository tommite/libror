package fi.smaa.libror.ws;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.xmlbeans.XmlException;
import org.decision_deck.xmcda3.uta.UtagmsInputDocument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;

import fi.smaa.libror.InfeasibleConstraintsException;
import fi.smaa.libror.RORModel;
import fi.smaa.libror.UTAGMSSolver;
import fi.smaa.libror.xml.XMLMarshaller;

public class WSMain {
	
	public static final String MSGSFILE = "messages.xml";
	public static final String ALTFILE = "alternatives.xml";
	public static final String PERFFILE = "performances.xml";
	public static final String PREFFILE = "preferences.xml";
	public static final String FILE_NECESSARY = "necessary.xml";
	public static final String FILE_POSSIBLE = "possible.xml";
	public static final String XMCDA_HEADER = "<XMCDA>\n";
	public static final String XSLT_FILE = "utagms-v2-to-v3.xslt";

	public static void main(String[] args) throws IOException, XmlException, InfeasibleConstraintsException, TransformerException {
		MyOptions opts = new MyOptions();
		CmdLineParser parser = new CmdLineParser(opts);
		try {
			parser.parseArgument(args);		
			if (opts.getInputDir() == null || opts.getOutputDir() == null) {
				printUsage();
				return;
			}
		} catch (CmdLineException e ) {
			System.err.println(e.getMessage());
			printUsage();			
			return;
		}
		
		File altFile = new File(opts.getInputDir() + File.pathSeparator + ALTFILE);
		File perfFile = new File(opts.getInputDir() + File.pathSeparator + PERFFILE);
		File prefFile = new File(opts.getInputDir() + File.pathSeparator + PREFFILE);
		
		String input = readFiles(altFile, perfFile, prefFile);

		RORModel model = processInput(input);
		
		UTAGMSSolver solver = new UTAGMSSolver(model);
		solver.solve();
		RealMatrix nec = solver.getNecessaryRelation();
		RealMatrix pos = solver.getPossibleRelation();
		
		writeOutputRelation(opts.getOutputDir() + File.pathSeparator + FILE_NECESSARY, nec);
		writeOutputRelation(opts.getOutputDir() + File.pathSeparator + FILE_POSSIBLE, pos);
	}

	public static RORModel processInput(String input) throws TransformerFactoryConfigurationError,
			TransformerConfigurationException, TransformerException, XmlException, MalformedURLException, IOException {
		TransformerFactory fact = TransformerFactory.newInstance();
		InputStream xsltStream = Resources.newInputStreamSupplier(Resources.getResource(XSLT_FILE)).getInput();
		Transformer tf = fact.newTransformer(new StreamSource(xsltStream));
		StringWriter outp = new StringWriter();
		tf.transform(new StreamSource(new StringReader(input)), new StreamResult(outp));
		
		UtagmsInputDocument doc = UtagmsInputDocument.Factory.parse(outp.toString());
		RORModel model = XMLMarshaller.xmlInputToRORModel(doc);
		return model;
	}
	
	private static void writeOutputRelation(String fileName, RealMatrix rel) throws IOException {
		File f = new File(fileName);
		PrintWriter w = new PrintWriter(f, "UTF-8");
		w.print(XMCDA_HEADER);
		w.print("<alternativesComparisons>\n");
		w.print("<pairs>\n");
		for (int i=0;i<rel.getRowDimension();i++) {
			for (int j=0;j<rel.getColumnDimension();j++) {
				double val = rel.getEntry(i, j);
				if (val == 1.0) {
					w.write("<pair>\n");
					w.write("<initial>\n");
					w.write("<alternativeID>" + i + "</alternativeID>\n");
					w.write("</initial>\n");
					w.write("<terminal>\n");
					w.write("<alternativeID>" + j + "</alternativeID>\n");					
					w.write("</terminal>\n");
					w.write("</pair>\n");
				}
			}
		}
		w.print("</alternativesComparisons>\n");
		w.print("</pairs>\n");
		w.print("</XMCDA>\n");
		w.close();
	}

	private static String readFiles(File altFile, File perfFile, File prefFile) throws IOException {
		String altStr = Files.toString(altFile, Charsets.US_ASCII);
		String perfStr = Files.toString(perfFile, Charsets.US_ASCII);
		String prefStr = Files.toString(prefFile, Charsets.US_ASCII);
		
		String finalString = XMCDA_HEADER+altStr+"\n"+perfStr+"\n"+prefStr+"\n</XMCDA>\n";
		return finalString;
	}

	private static void printUsage() {
		System.err.println("Usage: java -jar JARFILE -i inputDir -o outputDir");
	}	
}
