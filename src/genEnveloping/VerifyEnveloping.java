package genEnveloping;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VerifyEnveloping {

	/**
	 * ���ڿ��� �������� ��ȯ�Ѵ�.
	 * 
	 * @param s
	 * @return result;
	 */
	public static String stringToHex(String s) {
		String result = "";

		for (int i = 0; i < s.length(); i++) {
			result += String.format("%02X ", (int) s.charAt(i));
		}

		return result;
	}

	/**
	 * ��������Ʈ���� ���Ͽ� �����Ѵ�.
	 * 
	 * @param encodingData
	 * @param value
	 * @return flag;
	 */
	public static boolean logging(String encodingData, String value) {
		/* ���� */
		String digestValue = value;
		boolean flag = false;

		if (digestValue.equals(encodingData)) {
			flag = true;
			System.out.println(digestValue + " �� " + encodingData + " �� ���� :" + flag);
		} else {
			System.out.println(digestValue + " != " + encodingData + ":" + flag);
			flag = false;
		}

		return flag;
	}

	/**
	 * ��������Ʈ ���� �����Ѵ�.
	 * 
	 * @param args
	 * @return nd.getTextContent();
	 * @throws Exception
	 * @throws IOException
	 */
	public static String beforeComparison(String args) throws Exception {
		File xmlFile = new File(args);

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

		return nd.getTextContent();
	}

	/**
	 * Base64 ���ڵ��� ������ �ҷ����� �����մϴ�.
	 * 
	 * @param encodeFile
	 * @param encodingFile
	 * @param isChunked
	 * @return encodingImage;
	 * @throws IOException
	 */
	public static byte[] encodeFile(byte[] encodingFile, boolean isChunked) throws IOException {

		byte[] encodingImage = Base64.encodeBase64(encodingFile, isChunked);

		return encodingImage;
	}

	/**
	 * ������ �ҷ��ɴϴ�.
	 * 
	 * @param encodeFile
	 * @return bytes
	 * @throws IOException
	 */
	public static byte[] loadFile(String fileName) throws IOException {
		File file = new File(fileName);
		int len = (int) file.length();

		BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
		byte[] bytes = new byte[len];
		reader.read(bytes, 0, len);
		reader.close();

		return bytes;
	}

	/**
	 * ������ XML ������ ��������Ʈ, ��������Ʈ ��: ������ ���� ��
	 * 
	 * @param extractXml
	 * @return flag of logging()
	 * @throws Exception
	 */
	public static boolean main(String extractXml) throws Exception {
		byte[] bytes = loadFile(extractXml + ".test");

		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(bytes);
		byte byteData[] = md.digest();

		// ����Ʈ�� �������� ��ȯ�Ѵ�.
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		byte[] encoded = encodeFile(byteData, false);
		String extractValue = new String(encoded);

		/* ��������Ʈ �� Ȯ�� */
		String value = beforeComparison(extractXml);

		return logging(extractValue, value);
	}
}
