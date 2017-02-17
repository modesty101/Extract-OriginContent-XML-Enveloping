package genEnveloping;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class AppendStr {
	
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
	 * 파일의 내용을 조작한다. + 추가, 삭제
	 * 
	 * @param args
	 * @return true;
	 * @throws Exception
	 */
	public static boolean main(String args) throws Exception {
		byte bytes[] = loadFile(args); // Extracted.xml
		String str = new String(bytes);
		BufferedWriter bw;
		StringBuffer sb = new StringBuffer(str);
		int index = 0;
		int lastindex = 0;
		String insert = " xmlns=\"http://www.w3.org/2000/09/xmldsig#\"";
		sb.insert(45, insert);

		System.out.println("Where is <?xml ... > : " + sb.indexOf(">"));
		index = sb.indexOf(">");
		System.out.println("where is End Sign > : " + sb.lastIndexOf(">"));
		lastindex = sb.lastIndexOf(">");
		System.out.println("Where is <Object ..> : " + sb.indexOf("t"));

		bw = new BufferedWriter(new FileWriter(args));
		/* 컴퓨터는 0부터 시작한다. 정상적인 값을 얻으려면 마지막에 +1을 해야된다. */
		bw.write(sb.substring(index + 1, lastindex + 1));
		bw.close();

		System.out.println("Done");
		System.out.println();

		return true;
	}

}
