package translator.com.gui;

import javax.swing.JTextArea;

public class ResizeFormatter {
	private static String LSEP = System.getProperty("line.separator");
	public static String format(String words, int frameW, JTextArea textArea) {
		String[] arr = words.split(" ");
		StringBuilder sb = new StringBuilder();
		
		textArea.setText("");
		for (String str : arr) {
			textArea.setText(textArea.getText() + str + " ");
			if (textArea.getPreferredSize().getWidth() > frameW && sb.length() > 0) {
				sb.append(LSEP);
				textArea.setText(str);
			}
			sb.append(str);
			sb.append(" ");
		}
		return sb.toString();
	}
}

