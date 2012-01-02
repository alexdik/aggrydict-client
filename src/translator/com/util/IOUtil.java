package translator.com.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtil {
	private static final int BUF_SIZE = 1024;
	private static final String ENCODING = "UTF-8";

	public static byte[] readStream(final InputStream is) throws IOException {
		byte buf[] = new byte[BUF_SIZE];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		int read = 0;
		do {
			read = is.read(buf, 0, BUF_SIZE);
			if (read > 0) {
				bos.write(buf, 0, read);
			}
		} while (read > 0);

		bos.flush();
		byte[] result = bos.toByteArray();
		bos.close();

		return result;
	}

	public static String getString(final InputStream is) throws IOException {
		byte[] buf = readStream(is);
		return new String(buf, ENCODING);
	}
}
