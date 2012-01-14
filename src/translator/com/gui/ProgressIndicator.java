package translator.com.gui;

public class ProgressIndicator implements Runnable {
	private boolean inProgress = false; 
	MainWindow window;
	
	public ProgressIndicator(MainWindow window) {
		this.window = window;
	}
	
	public void start() {
		inProgress = true;
		Thread executer = new Thread(this);
		executer.start();
	}
	
	public synchronized void stop() {
		inProgress = false; 
	}

	public void run() {
		final String initialStatus = "Translating |";
		String status = initialStatus;
		while (true) {
			synchronized(this) {
				if (inProgress) {
					window.setTransalationAndRepaint(status);
					status += "|";
					if (status.length() > 30)
						status = initialStatus;
				} else {
					break;
				}
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
