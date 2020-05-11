package leandro.soares.quevedo.scheduller.model.teamwork.service;

import com.google.gson.annotations.SerializedName;

import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkPerson;

public class TeamworkPersonResponse extends TeamworkResponse {

	@SerializedName ("person")
	private TeamworkPerson person;

	public TeamworkPerson getPerson () {
		return person;
	}

	public void setPerson (TeamworkPerson person) {
		this.person = person;
	}
}
