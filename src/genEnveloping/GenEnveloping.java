package genEnveloping;

import javax.xml.crypto.*;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dom.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.*;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.security.*;
import java.util.Collections;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * This is a simple example of generating an Enveloping XML
 * Signature using the JSR 105 API. The signature in this case references a
 * local URI that points to an Object element.
 * The resulting signature will look like (certificate and
 * signature values will be different):
 *
 * <pre><code>
 * <Signature xmlns="http://www.w3.org/2000/09/xmldsig#">
 *   <SignedInfo>
 *     <CanonicalizationMethod Algorithm="http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments"/>
 *     <SignatureMethod Algorithm="http://www.w3.org/2000/09/xmldsig#dsa-sha1"/>
 *     <Reference URI="#object">
 *       <DigestMethod Algorithm="http://www.w3.org/2000/09/xmldsig#sha1"/>
 *       <DigestValue>7/XTsHaBSOnJ/jXD5v0zL6VKYsk=</DigestValue>
 *     </Reference>
 *   </SignedInfo>
 *   <SignatureValue>
 *     RpMRbtMHLa0siSS+BwUpLIEmTfh/0fsld2JYQWZzCzfa5kBTz25+XA==
 *   </SignatureValue>
 *   <KeyInfo>
 *     <KeyValue>
 *       <DSAKeyValue>
 *         <P>
 *           /KaCzo4Syrom78z3EQ5SbbB4sF7ey80etKII864WF64B81uRpH5t9jQTxeEu0Imbz
 *           RMqzVDZkVG9xD7nN1kuFw==
 *         </P>
 *         <Q>
 *           li7dzDacuo67Jg7mtqEm2TRuOMU=
 *         </Q>
 *         <G>
 *           Z4Rxsnqc9E7pGknFFH2xqaryRPBaQ01khpMdLRQnG541Awtx/XPaF5Bpsy4pNWMOH
 *           CBiNU0NogpsQW5QvnlMpA==
 *         </G>
 *         <Y>
 *           wbEUaCgHZXqK4qLvbdYrAc6+Do0XVcsziCJqxzn4cJJRxwc3E1xnEXHscVgr1Cql9
 *           i5fanOKQbFXzmb+bChqig==
 *         </Y>
 *       </DSAKeyValue>
 *     </KeyValue>
 *   </KeyInfo>
 *   <Object Id="object">some text</Object>
 * </Signature>
 *
 * </code></pre>
 */

/**
 * XML-Enveloping-Signature 예제 변형 그리고, 원본 값 추출하기
 * 
 * + 추가사항 - 2017.02.09 : GUI 구현, (암호화 알고리즘)DSA에서 RSA로 변경
 * 
 * @source <a href=
 *         "https://docs.oracle.com/javase/8/docs/technotes/guides/security/xmldsig/GenEnveloping.java"/>
 * @author <a href="mailto:modesty101@daum.net">김동규</a>
 * @since 2017
 */
public class GenEnveloping {

	/**
	 * 파일을 불러온다.
	 * 
	 * @param lFile
	 * @return bytes;
	 * @throws IOException
	 */
	public static byte[] loadFile(String lFile) throws IOException {
		File file = new File(lFile);
		int len = (int) file.length();

		BufferedInputStream reader = new BufferedInputStream(new FileInputStream(lFile));
		byte[] bytes = new byte[len];
		reader.read(bytes, 0, len);
		reader.close();

		return bytes;
	}

	/**
	 * Enveloping 시그니처를 생성한다. 
	 * + XML 파일, 텍스트 파일 전용..
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args) throws Exception {
		/* 로그 파일 생성 */
		File file = new File("log.txt");
		PrintStream printStream = new PrintStream(new FileOutputStream(file));
		System.setOut(printStream);

		/* 파일을 불러온다 */
		byte[] bytes = loadFile(args);
		String str = new String(bytes);

		// XMLSignatureFactory 객체에 DOM 인스턴스를 받아온다.
		XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

		// Next, create a Reference to a same-document URI that is an Object
		// element and specify the SHA1 digest algorithm
		// 레퍼런스 생성, 동일한 문서의 URI(오브젝트) 그리고 SHA1 알고리즘 설정하라는 소리.
		Reference ref = fac.newReference("#object", fac.newDigestMethod(DigestMethod.SHA1, null));

		// 노드 내용 추가, <Object>1234 형식으로 만들어진다.
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc = dbf.newDocumentBuilder().newDocument();
		Node text = doc.createTextNode(str);
		XMLStructure content = new DOMStructure(text);
		// <object>1234</object>
		XMLObject obj = fac.newXMLObject(Collections.singletonList(content), "object", null, null);

		// SignedInfo 생성
		SignedInfo si = fac.newSignedInfo(
				fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS,
						(C14NMethodParameterSpec) null),
				fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));

		// RSA 키 생성
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(1024);
		KeyPair kp = kpg.generateKeyPair();

		// 키 값에 RSA 공개키를 포함한다.
		KeyInfoFactory kif = fac.getKeyInfoFactory();
		KeyValue kv = kif.newKeyValue(kp.getPublic());

		// 키 정보를 생성하고 키 값을 추가한다.
		KeyInfo ki = kif.newKeyInfo(Collections.singletonList(kv));

		// XML 시그니처 생성한다. (아직 서명 전임)
		XMLSignature signature = fac.newXMLSignature(si, ki, Collections.singletonList(obj), null, null);

		/*
		 * DOMSignContext 생성하고, RSA 개인키로 문서를 서명한다.
		 */
		DOMSignContext dsc = new DOMSignContext(kp.getPrivate(), doc);

		// 마지막으로 enveloping 시그니처를 생성한다. 개인키를 씀으로
		signature.sign(dsc);

		// 결과
		OutputStream os;
		os = new FileOutputStream(args + ".xml");

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(doc), new StreamResult(os));
	}
}