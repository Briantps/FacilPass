package ejb.taskeng.actions;

import javax.ejb.Remote;

import ejb.taskeng.workflow.ActionCommand;

@Remote
public interface ReProcess extends ActionCommand {

}
