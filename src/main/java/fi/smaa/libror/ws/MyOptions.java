package fi.smaa.libror.ws;

import org.kohsuke.args4j.Option;

public class MyOptions {

	@Option(name="-i",usage="input directory")
	private String inputDir;

	@Option(name="-o",usage="output directory")
	private String outputDir;
	
	public String getInputDir() {
		return inputDir;
	}
	
	public String getOutputDir() {
		return outputDir;
	}
}
