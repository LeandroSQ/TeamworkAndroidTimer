package leandro.soares.quevedo.scheduller.model.teamwork.service;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkTask;

public class TeamworkListTasksResponse extends TeamworkResponse {

	@SerializedName ("todo-items")
	private List<TeamworkTask> taskList;

	public List<TeamworkTask> getTaskList () {
		return taskList;
	}

	public void setTaskList (List<TeamworkTask> taskList) {
		this.taskList = taskList;
	}
}
