package fi.smaa.libror.ws;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class OutputMessage {
	
	public static enum Type { ERROR, LOG };
	
	private Type type;
	private String msg;
	private String msgName;
	
	public OutputMessage(String msg, String msgName, Type type) {
		this.msg = msg;
		this.type = type;
		this.msgName = msgName;
	}
	
	public Node createXmlNode(Document doc) {
		Node node = null;
		if (type == Type.ERROR) {
			node = doc.createElement("errorMessage");
		} else {
			node = doc.createElement("logMessage");
		}
		if (msgName != null) {
			Attr nameAttrib = doc.createAttribute("name");
			nameAttrib.setTextContent(msgName);
			node.getAttributes().setNamedItem(nameAttrib);
		}
		Node textNode = doc.createElement("text");
		node.appendChild(textNode);
		textNode.setTextContent(msg);
		return node;
	}

}
