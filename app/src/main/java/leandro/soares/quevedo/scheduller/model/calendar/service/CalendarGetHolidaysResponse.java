package leandro.soares.quevedo.scheduller.model.calendar.service;

import java.util.List;

import leandro.soares.quevedo.scheduller.model.calendar.CalendarHolidayDate;

public class CalendarGetHolidaysResponse {

	private List<CalendarHolidayDate> holidayDateList;

	public List<CalendarHolidayDate> getHolidayDateList () {
		return holidayDateList;
	}

	public void setHolidayDateList (List<CalendarHolidayDate> holidayDateList) {
		this.holidayDateList = holidayDateList;
	}
}
