package genEnveloping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.security.MessageDigest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VerifyEnveloping {

	public static String stringToHex(String s) {
		String result = "";

		for (int i = 0; i < s.length(); i++) {
			result += String.format("%02X ", (int) s.charAt(i));
		}

		return result;
	}

	public static String calcValue(String args) throws Exception {
		File file = new File("log.txt");
		PrintStream printStream = new PrintStream(new FileOutputStream(file));
		System.setOut(printStream);

		FileInputStream fis = new FileInputStream(new File(args));
		System.out.println(args);
		System.out.println("Total file size : +" + fis.available());

		byte[] readBuf = new byte[fis.available()];
		while (fis.read(readBuf) != -1) {
		}

		String re = new String(readBuf);
		System.out.println(re);

		fis.close();

		System.out.println("Hex : ");
		System.out.println(stringToHex(re));

		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(re.getBytes());

		byte byteData[] = md.digest();

		// convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		System.out.println();
		System.out.println("Hex format : " + sb.toString());
		String encodingData = Base64.encodeBase64String(byteData);
		System.out.println("Base64 Encoding format : " + encodingData);

		return encodingData;
	}

	public static boolean verifyEnveloping(String b64Value, String xml) throws Exception {
		File xmlFile = new File(xml);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);
		doc.getDocumentElement().normalize();

		Node node = null;
		Node nd = null;
		NodeList nList = doc.getElementsByTagName("DigestValue");

		for (int i = 0; i < nList.getLength(); i++) {
			node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				if (eElement.hasChildNodes()) {
					NodeList nl = node.getChildNodes();
					nd = nl.item(0);
					System.out.println(nd);
				}
				System.out.println("");
			}
		}

		String digestValue = new String(nd.getTextContent());
		boolean flag = false;
		if (digestValue.equals(b64Value)) {
			flag = true;
			System.out.println("Extract.xml 의 " + digestValue + " 는 " + b64Value + " 와 같다 :" + flag);
		} else {
			System.out.println("Extract.xml 의 " + digestValue + " != " + b64Value + ":" + flag);
		}

		return flag;
	}

	public static boolean main(String extractXml, String originXml) throws Exception {
		/* 원본 */
		String data = calcValue(extractXml);
		/* 원본과 xml 비교 */
		boolean flag = verifyEnveloping(data, originXml);

		return flag;
	}

}
