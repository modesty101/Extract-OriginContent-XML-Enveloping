package genEnveloping;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XML-Enveloping-Signature 
 *  - 원본 값(origin data) 추출
 * 
 * @author <a href="mailto:modesty101@daum.net">김동규</a>
 * @since 2017
 */
public class ExtractEnveloping {
	public static void main(String args) throws Exception {
		File xmlFile = new File(args);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);
		doc.getDocumentElement().normalize();
		Node node = null;
		Node nd = null;
		NodeList nList = doc.getElementsByTagName("Object");
		for (int i = 0; i < nList.getLength(); i++) {
			node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				if (eElement.hasChildNodes()) {
					NodeList nl = node.getChildNodes();
					nd = nl.item(0);
					System.out.println(nd.getTextContent());
					// for (int j = 0; j < nl.getLength(); j++) {
					// nd = nl.item(0);
					// String name = nd.getTextContent();
					// System.out.println(name);
					// }
				}
				System.out.println("");
			}
		}

		// Save to file
		OutputStream os = new FileOutputStream("out.xml");
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(node), new StreamResult(os));
	}
}
