package translator.com.util;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class FixedJTextField extends JTextField {
	private static final long serialVersionUID = 8716922349950820341L;
	public FixedJTextField(String textWord) {
		super(textWord);
	}

	@Override
	public void setEditable(final boolean b) {
		super.setEditable(b);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getCaret().setVisible(b && isEnabled());
			}
		});
	}
}
