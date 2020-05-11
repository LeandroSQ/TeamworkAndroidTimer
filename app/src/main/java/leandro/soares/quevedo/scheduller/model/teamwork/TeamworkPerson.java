package leandro.soares.quevedo.scheduller.model.teamwork;


import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TeamworkPerson {
	@SerializedName("site-owner")
	private boolean siteOwner;

	@SerializedName("twitter")
	private String twitter;

	@SerializedName("last-name")
	private String lastName;

	@SerializedName("profile")
	private String profile;

	@SerializedName("userUUID")
	private String userUUID;

	@SerializedName("marketoId")
	private String marketoId;

	@SerializedName("user-name")
	private String userName;

	@SerializedName("id")
	private String id;

	@SerializedName("phone-number-office")
	private String phoneNumberOffice;

	@SerializedName("feature-updates-count")
	private String featureUpdatesCount;

	@SerializedName("phone-number-mobile")
	private String phoneNumberMobile;

	@SerializedName("user-type")
	private String userType;

	@SerializedName("first-name")
	private String firstName;

	@SerializedName("im-service")
	private String imService;

	@SerializedName("im-handle")
	private String imHandle;

	@SerializedName("login-count")
	private String loginCount;

	@SerializedName("openId")
	private String openId;

	@SerializedName("private-notes-text")
	private String privateNotesText;

	@SerializedName("phone-number-office-ext")
	private String phoneNumberOfficeExt;

	@SerializedName("twoFactorAuthEnabled")
	private boolean twoFactorAuthEnabled;

	@SerializedName("documentEditorInstalled")
	private boolean documentEditorInstalled;

	@SerializedName("has-access-to-new-projects")
	private boolean hasAccessToNewProjects;

	@SerializedName("phone-number-fax")
	private String phoneNumberFax;

	@SerializedName("last-login")
	private String lastLogin;

	@SerializedName("companyId")
	private String companyId;

	@SerializedName("has-feature-updates")
	private boolean hasFeatureUpdates;

	@SerializedName("administrator")
	private boolean administrator;

	@SerializedName("pid")
	private String pid;

	@SerializedName("profile-text")
	private String profileText;

	@SerializedName("phone-number-home")
	private String phoneNumberHome;

	@SerializedName("email-address")
	private String emailAddress;

	@SerializedName("lengthOfDay")
	private String lengthOfDay;

	@SerializedName("tags")
	private List<Object> tags = null;

	@SerializedName("private-notes")
	private String privateNotes;

	@SerializedName("company-name")
	private String companyName;

	@SerializedName("last-changed-on")
	private String lastChangedOn;

	@SerializedName("deleted")
	private boolean deleted;

	@SerializedName("notes")
	private String notes;

	@SerializedName("user-invited-status")
	private String userInvitedStatus;

	@SerializedName("has-desk-account")
	private boolean hasDeskAccount;

	@SerializedName("created-at")
	private String createdAt;

	@SerializedName("user-invited-date")
	private String userInvitedDate;

	@SerializedName("avatar-url")
	private String avatarUrl;

	@SerializedName("in-owner-company")
	private String inOwnerCompany;

	@SerializedName("user-invited")
	private String userInvited;

	@SerializedName("email-alt-1")
	private String emailAlt1;

	@SerializedName("email-alt-2")
	private String emailAlt2;

	@SerializedName("email-alt-3")
	private String emailAlt3;

	@SerializedName("title")
	private String title;


	public boolean isSiteOwner () {
		return siteOwner;
	}

	public void setSiteOwner (boolean siteOwner) {
		this.siteOwner = siteOwner;
	}

	public String getTwitter () {
		return twitter;
	}

	public void setTwitter (String twitter) {
		this.twitter = twitter;
	}

	public String getLastName () {
		return lastName;
	}

	public void setLastName (String lastName) {
		this.lastName = lastName;
	}

	public String getProfile () {
		return profile;
	}

	public void setProfile (String profile) {
		this.profile = profile;
	}

	public String getUserUUID () {
		return userUUID;
	}

	public void setUserUUID (String userUUID) {
		this.userUUID = userUUID;
	}

	public String getMarketoId () {
		return marketoId;
	}

	public void setMarketoId (String marketoId) {
		this.marketoId = marketoId;
	}

	public String getUserName () {
		return userName;
	}

	public void setUserName (String userName) {
		this.userName = userName;
	}

	public String getId () {
		return id;
	}

	public void setId (String id) {
		this.id = id;
	}

	public String getPhoneNumberOffice () {
		return phoneNumberOffice;
	}

	public void setPhoneNumberOffice (String phoneNumberOffice) {
		this.phoneNumberOffice = phoneNumberOffice;
	}

	public String getFeatureUpdatesCount () {
		return featureUpdatesCount;
	}

	public void setFeatureUpdatesCount (String featureUpdatesCount) {
		this.featureUpdatesCount = featureUpdatesCount;
	}

	public String getPhoneNumberMobile () {
		return phoneNumberMobile;
	}

	public void setPhoneNumberMobile (String phoneNumberMobile) {
		this.phoneNumberMobile = phoneNumberMobile;
	}

	public String getUserType () {
		return userType;
	}

	public void setUserType (String userType) {
		this.userType = userType;
	}

	public String getFirstName () {
		return firstName;
	}

	public void setFirstName (String firstName) {
		this.firstName = firstName;
	}

	public String getImService () {
		return imService;
	}

	public void setImService (String imService) {
		this.imService = imService;
	}

	public String getImHandle () {
		return imHandle;
	}

	public void setImHandle (String imHandle) {
		this.imHandle = imHandle;
	}

	public String getLoginCount () {
		return loginCount;
	}

	public void setLoginCount (String loginCount) {
		this.loginCount = loginCount;
	}

	public String getOpenId () {
		return openId;
	}

	public void setOpenId (String openId) {
		this.openId = openId;
	}

	public String getPrivateNotesText () {
		return privateNotesText;
	}

	public void setPrivateNotesText (String privateNotesText) {
		this.privateNotesText = privateNotesText;
	}

	public String getPhoneNumberOfficeExt () {
		return phoneNumberOfficeExt;
	}

	public void setPhoneNumberOfficeExt (String phoneNumberOfficeExt) {
		this.phoneNumberOfficeExt = phoneNumberOfficeExt;
	}

	public boolean isTwoFactorAuthEnabled () {
		return twoFactorAuthEnabled;
	}

	public void setTwoFactorAuthEnabled (boolean twoFactorAuthEnabled) {
		this.twoFactorAuthEnabled = twoFactorAuthEnabled;
	}

	public boolean isDocumentEditorInstalled () {
		return documentEditorInstalled;
	}

	public void setDocumentEditorInstalled (boolean documentEditorInstalled) {
		this.documentEditorInstalled = documentEditorInstalled;
	}

	public boolean isHasAccessToNewProjects () {
		return hasAccessToNewProjects;
	}

	public void setHasAccessToNewProjects (boolean hasAccessToNewProjects) {
		this.hasAccessToNewProjects = hasAccessToNewProjects;
	}

	public String getPhoneNumberFax () {
		return phoneNumberFax;
	}

	public void setPhoneNumberFax (String phoneNumberFax) {
		this.phoneNumberFax = phoneNumberFax;
	}

	public String getLastLogin () {
		return lastLogin;
	}

	public void setLastLogin (String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getCompanyId () {
		return companyId;
	}

	public void setCompanyId (String companyId) {
		this.companyId = companyId;
	}

	public boolean isHasFeatureUpdates () {
		return hasFeatureUpdates;
	}

	public void setHasFeatureUpdates (boolean hasFeatureUpdates) {
		this.hasFeatureUpdates = hasFeatureUpdates;
	}

	public boolean isAdministrator () {
		return administrator;
	}

	public void setAdministrator (boolean administrator) {
		this.administrator = administrator;
	}

	public String getPid () {
		return pid;
	}

	public void setPid (String pid) {
		this.pid = pid;
	}

	public String getProfileText () {
		return profileText;
	}

	public void setProfileText (String profileText) {
		this.profileText = profileText;
	}

	public String getPhoneNumberHome () {
		return phoneNumberHome;
	}

	public void setPhoneNumberHome (String phoneNumberHome) {
		this.phoneNumberHome = phoneNumberHome;
	}

	public String getEmailAddress () {
		return emailAddress;
	}

	public void setEmailAddress (String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getLengthOfDay () {
		return lengthOfDay;
	}

	public void setLengthOfDay (String lengthOfDay) {
		this.lengthOfDay = lengthOfDay;
	}

	public List<Object> getTags () {
		return tags;
	}

	public void setTags (List<Object> tags) {
		this.tags = tags;
	}

	public String getPrivateNotes () {
		return privateNotes;
	}

	public void setPrivateNotes (String privateNotes) {
		this.privateNotes = privateNotes;
	}

	public String getCompanyName () {
		return companyName;
	}

	public void setCompanyName (String companyName) {
		this.companyName = companyName;
	}

	public String getLastChangedOn () {
		return lastChangedOn;
	}

	public void setLastChangedOn (String lastChangedOn) {
		this.lastChangedOn = lastChangedOn;
	}

	public boolean isDeleted () {
		return deleted;
	}

	public void setDeleted (boolean deleted) {
		this.deleted = deleted;
	}

	public String getNotes () {
		return notes;
	}

	public void setNotes (String notes) {
		this.notes = notes;
	}

	public String getUserInvitedStatus () {
		return userInvitedStatus;
	}

	public void setUserInvitedStatus (String userInvitedStatus) {
		this.userInvitedStatus = userInvitedStatus;
	}

	public boolean isHasDeskAccount () {
		return hasDeskAccount;
	}

	public void setHasDeskAccount (boolean hasDeskAccount) {
		this.hasDeskAccount = hasDeskAccount;
	}

	public String getCreatedAt () {
		return createdAt;
	}

	public void setCreatedAt (String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUserInvitedDate () {
		return userInvitedDate;
	}

	public void setUserInvitedDate (String userInvitedDate) {
		this.userInvitedDate = userInvitedDate;
	}

	public String getAvatarUrl () {
		return avatarUrl;
	}

	public void setAvatarUrl (String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getInOwnerCompany () {
		return inOwnerCompany;
	}

	public void setInOwnerCompany (String inOwnerCompany) {
		this.inOwnerCompany = inOwnerCompany;
	}

	public String getUserInvited () {
		return userInvited;
	}

	public void setUserInvited (String userInvited) {
		this.userInvited = userInvited;
	}

	public String getEmailAlt1 () {
		return emailAlt1;
	}

	public void setEmailAlt1 (String emailAlt1) {
		this.emailAlt1 = emailAlt1;
	}

	public String getEmailAlt2 () {
		return emailAlt2;
	}

	public void setEmailAlt2 (String emailAlt2) {
		this.emailAlt2 = emailAlt2;
	}

	public String getEmailAlt3 () {
		return emailAlt3;
	}

	public void setEmailAlt3 (String emailAlt3) {
		this.emailAlt3 = emailAlt3;
	}

	public String getTitle () {
		return title;
	}

	public void setTitle (String title) {
		this.title = title;
	}
}
