package com.mincai.ikuncode.judge.codesandbox.threads;

public class TimeManagerThread extends Thread {

	private long time;

	private boolean isTimeout = false;

	private Process process;

	public TimeManagerThread(long time) {
		isTimeout = false;
		this.time = time;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(time);

		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		if (process.isAlive()) {
			process.destroy();
			isTimeout = true;
			this.stop();
		}
		super.run();
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public boolean IsTime_out() {
		return isTimeout;
	}

}
