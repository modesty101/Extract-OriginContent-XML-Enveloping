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
	 * 문자열을 헥스값으로 변환한다.
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
	 * 다이제스트값을 비교하여 검증한다.
	 * 
	 * @param encodingData
	 * @param value
	 * @return flag;
	 */
	public static boolean logging(String encodingData, String value) {
		/* 검증 */
		String digestValue = value;
		boolean flag = false;

		if (digestValue.equals(encodingData)) {
			flag = true;
			System.out.println(digestValue + " 는 " + encodingData + " 와 같다 :" + flag);
		} else {
			System.out.println(digestValue + " != " + encodingData + ":" + flag);
			flag = false;
		}

		return flag;
	}

	/**
	 * 다이제스트 값을 추출한다.
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
	 * Base64 인코딩할 파일을 불러오고 저장합니다.
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
	 * 파일을 불러옵니다.
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
	 * 추출한 XML 파일을 다이제스트, 다이제스트 값: 원본과 서명본 비교
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

		// 바이트를 헥스값으로 변환한다.
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		byte[] encoded = encodeFile(byteData, false);
		String extractValue = new String(encoded);

		/* 다이제스트 값 확인 */
		String value = beforeComparison(extractXml);

		return logging(extractValue, value);
	}
}
