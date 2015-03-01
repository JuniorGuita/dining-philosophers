package me.lucasfelix.diningphilosophers.concurrence;

import static me.lucasfelix.diningphilosophers.concurrence.PhilosopherState.EATING;
import static me.lucasfelix.diningphilosophers.concurrence.PhilosopherState.HUNGRY;
import static me.lucasfelix.diningphilosophers.concurrence.PhilosopherState.THINKING;
import me.lucasfelix.diningphilosophers.gui.Main;

public class Philosopher extends Thread {

	private int idPhilosopher;
	private Semaphore semaphore;
	
	public Philosopher(int idPhilosopher, String name, Semaphore semaphore) {
		super(name);
		this.idPhilosopher = idPhilosopher;
		this.semaphore = semaphore;
	}

	public void hungry() {
		Main.states.set(this.idPhilosopher, HUNGRY);
		printStateMessage(HUNGRY);
	}

	public void eat() {
		Main.states.set(this.idPhilosopher, EATING);
		printStateMessage(EATING);

		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void think() {
		Main.states.set(this.idPhilosopher, THINKING);
		printStateMessage(THINKING);
		
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void putFork() {
		try {
			semaphore.release();

			think();

			Main.philosophers[left()].tryTakeFork();
			Main.philosophers[right()].tryTakeFork();

			semaphore.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void takeFork() throws InterruptedException {
		try {
			semaphore.release();
			hungry();

			tryTakeFork();

			semaphore.take();
			Main.semaphores[this.idPhilosopher].release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void tryTakeFork() throws InterruptedException {
		if (Main.states.get(this.idPhilosopher)  == HUNGRY && 
			Main.states.get(left()) != EATING &&
			Main.states.get(right()) != EATING){
			
			eat();
			
			Main.semaphores[this.idPhilosopher].take();
		}
	}

	private int right() {
		return (this.idPhilosopher + 1) % 5;
	}
	
	private int left() {
		return (this.idPhilosopher == 0) ? 4 : (this.idPhilosopher - 1) % 5;
	}
	
	private void printStateMessage(PhilosopherState state) {
		System.out.printf("O filósofo #%d, %s está %s\n", +idPhilosopher, getName(),
				state.getStateName());
	}
	
	@Override
	public void run() {
		try {
			do {
				takeFork();
				Thread.sleep(1000L);
				putFork();
			} while (true);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}
	}
}
