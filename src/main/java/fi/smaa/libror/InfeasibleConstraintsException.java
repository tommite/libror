package fi.smaa.libror;

@SuppressWarnings("serial")
public class InfeasibleConstraintsException extends Exception {
	public InfeasibleConstraintsException() {
		super();
	}

	public InfeasibleConstraintsException(String reason) {
		super(reason);
	}

}
