package jp.ricksoft.activiti.recallTest;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

public class DeleteTaskWithoutCheckCommand implements Command<Object> {
	private final TaskEntity task;
	
	public DeleteTaskWithoutCheckCommand(TaskEntity task){
		this.task = task;
	}
	
	public Object execute(CommandContext commandContext) {
		commandContext.getTaskEntityManager().deleteTask(this.task, "", false);
		return null;
	}

}
