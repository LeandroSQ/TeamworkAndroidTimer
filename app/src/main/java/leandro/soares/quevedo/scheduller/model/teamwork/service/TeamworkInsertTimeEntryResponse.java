package leandro.soares.quevedo.scheduller.model.teamwork.service;

import com.google.gson.annotations.SerializedName;

public class TeamworkInsertTimeEntryResponse extends TeamworkResponse {
	@SerializedName ("timeLogId")
	private String timeLogId;

	public String getTimeLogId () {
		return timeLogId;
	}

	public void setTimeLogId (String timeLogId) {
		this.timeLogId = timeLogId;
	}
}
