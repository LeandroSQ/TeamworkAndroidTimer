package leandro.soares.quevedo.scheduller.model.teamwork;

import com.google.gson.annotations.SerializedName;

public class TeamworkAccount {

	@SerializedName("requirehttps")
	private boolean requirehttps;

	@SerializedName("time-tracking-enabled")
	private boolean timeTrackingEnabled;

	@SerializedName("name")
	private String name;

	@SerializedName("datesignedup")
	private String datesignedup;

	@SerializedName("companyname")
	private String companyname;

	@SerializedName("ssl-enabled")
	private boolean sslEnabled;

	@SerializedName("created-at")
	private String createdAt;

	@SerializedName("cacheUUID")
	private String cacheUUID;

	@SerializedName("account-holder-id")
	private String accountHolderId;

	@SerializedName("logo")
	private String logo;

	@SerializedName("id")
	private String id;

	@SerializedName("URL")
	private String uRL;

	@SerializedName("email-notification-enabled")
	private boolean emailNotificationEnabled;

	@SerializedName("companyid")
	private String companyid;

	@SerializedName("lang")
	private String lang;

	@SerializedName("code")
	private String code;

	public boolean isRequirehttps () {
		return requirehttps;
	}

	public void setRequirehttps (boolean requirehttps) {
		this.requirehttps = requirehttps;
	}

	public boolean isTimeTrackingEnabled () {
		return timeTrackingEnabled;
	}

	public void setTimeTrackingEnabled (boolean timeTrackingEnabled) {
		this.timeTrackingEnabled = timeTrackingEnabled;
	}

	public String getName () {
		return name;
	}

	public void setName (String name) {
		this.name = name;
	}

	public String getDatesignedup () {
		return datesignedup;
	}

	public void setDatesignedup (String datesignedup) {
		this.datesignedup = datesignedup;
	}

	public String getCompanyname () {
		return companyname;
	}

	public void setCompanyname (String companyname) {
		this.companyname = companyname;
	}

	public boolean isSslEnabled () {
		return sslEnabled;
	}

	public void setSslEnabled (boolean sslEnabled) {
		this.sslEnabled = sslEnabled;
	}

	public String getCreatedAt () {
		return createdAt;
	}

	public void setCreatedAt (String createdAt) {
		this.createdAt = createdAt;
	}

	public String getCacheUUID () {
		return cacheUUID;
	}

	public void setCacheUUID (String cacheUUID) {
		this.cacheUUID = cacheUUID;
	}

	public String getAccountHolderId () {
		return accountHolderId;
	}

	public void setAccountHolderId (String accountHolderId) {
		this.accountHolderId = accountHolderId;
	}

	public String getLogo () {
		return logo;
	}

	public void setLogo (String logo) {
		this.logo = logo;
	}

	public String getId () {
		return id;
	}

	public void setId (String id) {
		this.id = id;
	}

	public String getuRL () {
		return uRL;
	}

	public void setuRL (String uRL) {
		this.uRL = uRL;
	}

	public boolean isEmailNotificationEnabled () {
		return emailNotificationEnabled;
	}

	public void setEmailNotificationEnabled (boolean emailNotificationEnabled) {
		this.emailNotificationEnabled = emailNotificationEnabled;
	}

	public String getCompanyid () {
		return companyid;
	}

	public void setCompanyid (String companyid) {
		this.companyid = companyid;
	}

	public String getLang () {
		return lang;
	}

	public void setLang (String lang) {
		this.lang = lang;
	}

	public String getCode () {
		return code;
	}

	public void setCode (String code) {
		this.code = code;
	}
}
