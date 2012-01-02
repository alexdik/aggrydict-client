package translator.com.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Properties;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import translator.com.util.AsyncProxy;
import translator.com.util.ClipboardHelper;

import com.melloware.jintellitype.JIntellitype;

public class MainWindow {
	private String textTrans = "";
	private String textWord = "";

	final JTextField word = new JTextField(textWord);
	private JFrame jfrm;
	
	public JFrame getJfrm() {
		return jfrm;
	}

	public void setTextWord(String textWord) {
		this.textWord = textWord;
	}
	
	public void repaint() {
		jfrm.repaint();
	}

	public void setText(String text) {
		this.textTrans = text;
	}
	public String getText() {
		return word.getText();
	}

	@SuppressWarnings("serial")
	public MainWindow(ActionListener buttonPressed, Properties props) throws FileNotFoundException, IOException {
		textTrans = new Formatter().format("Press Ñtrl+%s to translate word from buffer", props.getProperty("hotkey")).toString();
		
		final JTextArea textArea = new JTextArea(textTrans);
		textArea.setEditable(false);
		textArea.getWidth();
		final JScrollPane sp = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		final JButton button = new JButton("Translate");
		word.setPreferredSize(new Dimension(150, 28));
		
		BorderLayout borderLayout = new BorderLayout(5, 5);
		
		jfrm = new JFrame("Aggrydict") {
			public void paint(Graphics g) {
				String formattedStr = ResizeFormatter.format(textTrans, getWidth() - 29, textArea);
				textArea.setText(formattedStr);
				if (AsyncProxy.isAlive()) {
					word.setEditable(false);
					if (!word.getText().equals(textWord))
						word.setText(textWord);
				} else {
					word.setEditable(true);
					word.setText(textWord);
				}
				super.paint(g);
			}
		};
		
		button.addActionListener(buttonPressed);
		
		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(panel);
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addComponent(word).addComponent(button));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup().addGroup(
				groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(word).addComponent(button)));
		panel.setLayout(groupLayout);
		
		jfrm.setLayout(borderLayout);
		jfrm.setSize(350, 125);
//		jfrm.setAlwaysOnTop(true);
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jfrm.add(sp, BorderLayout.CENTER);
		jfrm.add(panel, BorderLayout.NORTH);
		jfrm.setVisible(true);
		word.addKeyListener((KeyListener)buttonPressed);
	}

	public static void main(String args[]) throws InterruptedException, InvocationTargetException {
		final HotKeyListener lstn = new HotKeyListener();
		final List<MainWindow> storage = new ArrayList<MainWindow>();
		final Properties props = new Properties();
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				MainWindow window = null;
				try {
					props.load(new FileInputStream("conf.properties"));
					window = new MainWindow(lstn, props);
				} catch (Exception e) {
					e.printStackTrace();
				}
				storage.add(window);
			}
		});
		
		final MainWindow window = storage.get(0);
		char key = props.getProperty("hotkey").toUpperCase().charAt(0);
		JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_CONTROL, (int)key);
		lstn.setWindow(window);
		JIntellitype.getInstance().addHotKeyListener(lstn);
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				JIntellitype.getInstance().cleanUp();
			}
		});
	}
	
	private static class HotKeyListener implements com.melloware.jintellitype.HotkeyListener, KeyListener, ActionListener {
		private MainWindow window;
		public void setWindow(MainWindow window) {
			this.window = window;
		}
		
		public void onHotKey(int arg0) {
			String word = null;
			try {
				if (arg0 == 1) {
					word = ClipboardHelper.getBuffer();
					window.getJfrm().toFront();
					window.getJfrm().setExtendedState(JFrame.NORMAL);
					window.repaint();
				} else {
					word = window.getText();
				}
				if (word != null) {
					word = word.trim();
					window.setTextWord(word);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (word == null || word.length() == 0) {
				window.setText("Buffer is empty");
				window.repaint();
				return;
			} else {
				if (!AsyncProxy.isAlive())
					AsyncProxy.translate(window, word);
			}
		}

		public void actionPerformed(ActionEvent e) {
			onHotKey(0);
		}
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER){
				onHotKey(0);
			}
		}
		public void keyReleased(KeyEvent e) {
		}
		public void keyTyped(KeyEvent e) {
		}
	}
}
