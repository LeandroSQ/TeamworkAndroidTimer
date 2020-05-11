package leandro.soares.quevedo.scheduller.model.calendar;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarHolidayDate {

	@SerializedName("date")
	private String date;

	@SerializedName("name")
	private String name;

	@SerializedName("link")
	private String link;

	@SerializedName("type")
	private String type;

	@SerializedName("description")
	private String description;

	@SerializedName("type_code")
	private int typeCode;


	public String getDate () {
		return date;
	}

	public void setDate (String date) {
		this.date = date;
	}

	@SuppressLint("SimpleDateFormat")
	public Date getFormattedDate () {
		try {
			if (this.date == null || this.date.isEmpty ()) return null;
			else return new SimpleDateFormat ("dd/MM/yyyy").parse (this.date);
		} catch (Exception e) {
			e.printStackTrace ();

			return null;
		}
	}

	public String getName () {
		return name;
	}

	public void setName (String name) {
		this.name = name;
	}

	public String getLink () {
		return link;
	}

	public void setLink (String link) {
		this.link = link;
	}

	public String getType () {
		return type;
	}

	public void setType (String type) {
		this.type = type;
	}

	public String getDescription () {
		return description;
	}

	public void setDescription (String description) {
		this.description = description;
	}

	public int getTypeCode () {
		return typeCode;
	}

	public void setTypeCode (int typeCode) {
		this.typeCode = typeCode;
	}
}
