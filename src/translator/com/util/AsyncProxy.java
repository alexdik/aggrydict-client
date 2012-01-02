package translator.com.util;

import translator.com.gui.MainWindow;
import translator.com.gui.ProgressIndicator;

public class AsyncProxy implements Runnable{
	private MainWindow window;
	private String word;
	private static boolean isRunning = false;
	
	private AsyncProxy(MainWindow window, String word) {
		this.window = window;
		this.word = word;
	}
	
	public static synchronized boolean isAlive() {
		return isRunning;
	}
	
	public static void translate(MainWindow window, String word) {
		AsyncProxy proxy = new AsyncProxy(window, word);
		isRunning = true;
		new Thread(proxy).start();
	}

	@Override
	public void run() {
		final ProgressIndicator progressIndicator = new ProgressIndicator(window);
		progressIndicator.start();
		try {
			String res = null;
			try {
				res = UrlFetcher.get(word);
			} catch (Exception e) {
				res = "Failed, please retry";
				e.printStackTrace();
			} finally {
				progressIndicator.stop();
				synchronized (AsyncProxy.class) {
					isRunning = false;
				}
			}
			window.setText(res);
			window.repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
