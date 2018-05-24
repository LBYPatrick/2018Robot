package frc.team5181.tasking;

import java.util.ArrayList;
import java.util.Arrays;

import frc.team5181.StringUtils;

/**
 * Created by TylerLiu on 2017/03/17.
 */
public class ParallelTask implements Task{

    private ArrayList<Task> tasks = new ArrayList<>();

    public ParallelTask(){}

    public ParallelTask(Task... tasks) {
        this.tasks.addAll(Arrays.asList(tasks));
    }

    public ParallelTask add(Task... tasks) {
        this.tasks.addAll(Arrays.asList(tasks));
        return this;
    }

    @Override
    public boolean nextStep() {
        tasks.removeIf(Task::nextStep);
        return tasks.isEmpty();
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder().append("Paralling ").append(this.tasks.size()).append(" tasks\n");
    	int index = 0;
    	for (Task task : this.tasks) {
    		sb.append(StringUtils.indent("Task " + index++ + "\n" + StringUtils.indent(task.toString())));
    	}
    	return sb.toString();
    }

}
