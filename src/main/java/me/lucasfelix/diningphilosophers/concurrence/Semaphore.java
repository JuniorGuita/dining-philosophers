package me.lucasfelix.diningphilosophers.concurrence;

public class Semaphore {

	// TODO to define count variable access modifier
	private int count;

	public Semaphore() {
		this(0);
	}

	public Semaphore(int count) {
		this.count = count;
	}

	public synchronized void take() {
		this.count++;
		this.notify();
	}

	public synchronized void release() throws InterruptedException {
		while (this.count == 0)	wait();
		this.count--;
	}

}