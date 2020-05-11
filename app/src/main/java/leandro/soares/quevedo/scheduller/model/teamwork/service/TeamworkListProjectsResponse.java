package leandro.soares.quevedo.scheduller.model.teamwork.service;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkProject;

public class TeamworkListProjectsResponse extends TeamworkResponse {

	@SerializedName("projects")
	private List<TeamworkProject> projectList;

	public List<TeamworkProject> getProjectList () {
		return projectList;
	}

	public void setProjectList (List<TeamworkProject> projectList) {
		this.projectList = projectList;
	}
}
