package me.lucasfelix.diningphilosophers.concurrence;

public enum PhilosopherState {
	
	THINKING("PENSANDO"), 
	HUNGRY("FAMINTO"), 
	EATING("COMENDO");
	
	private String stateName;
	
	private PhilosopherState(String stateName) {
		this.stateName = stateName;
	}
	
	public String getStateName() {
		return this.stateName;
	}
	
}
