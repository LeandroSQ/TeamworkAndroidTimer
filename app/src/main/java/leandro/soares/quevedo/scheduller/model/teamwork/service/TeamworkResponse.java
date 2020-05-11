package leandro.soares.quevedo.scheduller.model.teamwork.service;

import com.google.gson.annotations.SerializedName;

public abstract class TeamworkResponse {

	@SerializedName("STATUS")
	private String status;

	@SerializedName("MESSAGE")
	private String message;

	public String getStatus () {
		return status;
	}

	public void setStatus (String status) {
		this.status = status;
	}

	public String getMessage () {
		return message;
	}

	public void setMessage (String message) {
		this.message = message;
	}
}
