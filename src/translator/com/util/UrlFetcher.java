package translator.com.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class UrlFetcher {
	private static final String ENCODING = "UTF-8";
	private static final String API_ADRR = "http://aggrydict.appspot.com/api/short/"; //"http://127.0.0.1:8888/api/short/"; 
	public static String get(String word) throws IOException {
		word = URLEncoder.encode(word, ENCODING);
		URL url = new URL(API_ADRR + word);
		URLConnection conn = url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setDoOutput(true);
		conn.connect();
		byte[] bos = IOUtil.readStream(conn.getInputStream());
		return new String(bos, ENCODING);
	}
}