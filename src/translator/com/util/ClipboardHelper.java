package translator.com.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;

public class ClipboardHelper {
	public static String getBuffer() {
		Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard(); 
		DataFlavor[] df = systemClipboard.getAvailableDataFlavors();
		for (DataFlavor dataFlavor : df) {
			Class<?> clazz = dataFlavor.getRepresentationClass();
			if (clazz == java.lang.String.class && dataFlavor.getMimeType().contains("text/plain; class=java.lang.String")) {
				String str = null;
				try {
					str = (String) systemClipboard.getData(dataFlavor);
				} catch (Exception e) {
					e.printStackTrace();
				}			
				return str;
			}
		}
		return null;
	}
}
