package frc.team5181.tasking;

import java.util.ArrayDeque;

import frc.team5181.StringUtils;

/**
 * A sequence of tasks.
 */
public class TaskSequence implements Task {

    private ArrayDeque<Task> tasks = new ArrayDeque<>();
    private int finishedCount = 0;

    public TaskSequence(){}

    public TaskSequence add(Task... tasks) {
        for (Task t:tasks){
            this.tasks.add(t);
        }
        return this;
    }

    @Override
    public boolean nextStep() {
        Task task = tasks.getFirst();
        // protection
        if (task == null || task.nextStep()) {
            // If the task finishes (returned true), start to work on next task.
            tasks.pop();
            finishedCount++;
        }
        return tasks.isEmpty();
    }
    
    @Override
    public String toString() {
    	return "Step: " + this.finishedCount + "/" + (this.tasks.size() + this.finishedCount) + "\n" +
    			StringUtils.indent(this.tasks.getFirst().toString());
    }

}
