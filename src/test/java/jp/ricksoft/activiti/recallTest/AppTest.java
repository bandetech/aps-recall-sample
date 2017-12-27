package jp.ricksoft.activiti.recallTest;


import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;

import jp.ricksoft.activiti.recallTest.DeleteTaskWithoutCheckCommand;
import jp.ricksoft.activiti.recallTest.SetActivityCmd;

import static org.junit.Assert.*;

import java.util.List;

public class AppTest {
	
	private static final Logger logger = Logger.getLogger(AppTest.class);
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	/*
	 * Get Activities
	 * @param executionId, not {@code null}
	 * @return List of activities, {@code null} if no activities for executionId.
	 */
	private List<String> getActivities(String executionId){
		
	    List<String> activities = activitiRule.getRuntimeService().getActiveActivityIds(executionId);
	    logger.info("N of Activities : " + activities.size());
	    for(String s: activities){
	    	logger.info("activity :" + s);
	    }
	    
	    return activities;
	}
	
	/*
	 * Recall process
	 * @param currentTask, not {@code null}
	 * @param toTask, not {@code null}
	 * @return void
	 */
	private void moveToTask(Task currentTask, String toTask){
	    ProcessEngineConfigurationImpl cfgImpl = (ProcessEngineConfigurationImpl) activitiRule.getProcessEngine().getProcessEngineConfiguration();

	    CommandExecutor cmdExecutor = cfgImpl.getCommandExecutor();

	    ProcessInstance processInstance = this.activitiRule.getRuntimeService().createProcessInstanceQuery().singleResult();
	    cmdExecutor.execute(new SetActivityCmd(processInstance.getId(),toTask));
	    cmdExecutor.execute(new DeleteTaskWithoutCheckCommand((TaskEntity)currentTask));
	    this.activitiRule.getRuntimeService().signal(processInstance.getId());
		
	}
	
	@Test
	@Deployment(resources = {"org/activiti/test/TestDoubleReview.bpmn20.xml"})
	public void test() {
		ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("TestDoubleReview");
		assertNotNull(processInstance);		
		
	    // Get Process ID
	    logger.info("---- process id :" + processInstance.getId());
	    
	    // Get Activity
	    this.getActivities(processInstance.getId());
	    
	    // Get current task list
	    Task task = this.activitiRule.getTaskService().createTaskQuery().singleResult();
	    
	    // Go through receiveTask
	    this.activitiRule.getRuntimeService().signal(processInstance.getId());
	    
	    // Pass userReview1
	    processInstance = this.activitiRule.getRuntimeService().createProcessInstanceQuery().singleResult();
	    task = this.activitiRule.getTaskService().createTaskQuery().singleResult();
	    assertEquals("userReview1", task.getName());
	    assertEquals("userReview1", this.getActivities(processInstance.getId()).get(0));
	    
	    // Complete current task
	    activitiRule.getTaskService().complete(task.getId());
	    processInstance = this.activitiRule.getRuntimeService().createProcessInstanceQuery().singleResult();
	    
	    // Get current task list
	    task = this.activitiRule.getTaskService().createTaskQuery().singleResult();
	    assertEquals("userReview2", task.getName());
	    assertEquals("userReview2", this.getActivities(processInstance.getId()).get(0));
	    
	    // Move to userReview1
	    this.moveToTask(task, "receiveTask");
	    
	    System.out.println("Recalled.....");
	    
	    // activity will move...

	    // Current activity should be userReview1
	    processInstance = this.activitiRule.getRuntimeService().createProcessInstanceQuery().
				processInstanceId(processInstance.getProcessInstanceId()).singleResult();
	    assertEquals("userReview1", processInstance.getActivityId());

	    //// Current task should be userReview1 but task was null...
	    task = this.activitiRule.getTaskService().createTaskQuery().singleResult();
	    assertNotNull(task);
	    assertEquals("userReview1", task.getName());
	    
	    // Complete userReview1
	    // Complete current task
	    activitiRule.getTaskService().complete(task.getId());
	    processInstance = this.activitiRule.getRuntimeService().createProcessInstanceQuery().singleResult();
	    
	    // Get current task list
	    task = this.activitiRule.getTaskService().createTaskQuery().singleResult();
	    assertEquals("userReview2", task.getName());
	    assertEquals("userReview2", this.getActivities(processInstance.getId()).get(0));
	    
	    	    
	}

}