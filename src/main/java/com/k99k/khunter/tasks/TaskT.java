package com.k99k.khunter.tasks;

import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.Task;

public class TaskT extends Task {
	
	
	
	public TaskT(String name, ActionMsg actionMsg) {
		super(name, actionMsg);
	}

	public TaskT(String name) {
		super(name, null);
	}

	private int i;


	@Override
	public void run() {
		i++;
		System.out.println(this.getName()+ " "+i);
	}
	
	

	

}