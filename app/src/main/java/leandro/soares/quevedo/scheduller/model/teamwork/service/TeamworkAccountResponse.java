package leandro.soares.quevedo.scheduller.model.teamwork.service;

import com.google.gson.annotations.SerializedName;

import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkAccount;

public class TeamworkAccountResponse extends TeamworkResponse {

	@SerializedName("account")
	private TeamworkAccount account;

	public TeamworkAccount getAccount () {
		return account;
	}

	public void setAccount (TeamworkAccount account) {
		this.account = account;
	}

}
