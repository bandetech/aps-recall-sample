package jp.ricksoft.activiti.recallTest;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

public class SetActivityCmd implements Command<Void> {
	private final String executionId;
	private final String activityId;
	
	public SetActivityCmd(String executionId, String activityId){
		this.executionId = executionId;
		this.activityId = activityId;
	}
	
	public Void execute(CommandContext commandContext) {
		
		ExecutionEntity execution = commandContext.getExecutionEntityManager().findExecutionById(this.executionId);
		execution.setActivity(new ActivityImpl(this.activityId, execution.getProcessDefinition()));
		
		
		return null;
	}
}
