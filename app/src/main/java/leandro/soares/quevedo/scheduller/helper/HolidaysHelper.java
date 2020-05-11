package leandro.soares.quevedo.scheduller.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import leandro.soares.quevedo.scheduller.generic.BaseActivity;
import leandro.soares.quevedo.scheduller.interfaces.SimpleCallback;
import leandro.soares.quevedo.scheduller.model.calendar.CalendarHolidayDate;
import leandro.soares.quevedo.scheduller.service.CalendarHolidayService;
import leandro.soares.quevedo.scheduller.util.SessionManager;

public final class HolidaysHelper {
	private Context context;
	private List<CalendarHolidayDate> holidayDateList;

	public HolidaysHelper (BaseActivity context) {
		this.context = context;

		loadHolidays (context);
	}

	public HolidaysHelper (Context context) {
		this.context = context;
		this.holidayDateList = SessionManager.getInstance ().loadHolidayDateList ();
	}

	private void loadHolidays (BaseActivity baseActivity) {
		int currentYear = Calendar.getInstance ().get (Calendar.YEAR);
		this.holidayDateList = SessionManager.getInstance ().loadHolidayDateList ();

		// Check if is a valid list
		if (holidayDateList == null || holidayDateList.size () <= 0 || holidayDateList.get (0).getFormattedDate ().getYear () + 1900 != currentYear) {
			// Update the list
			CalendarHolidayService service = new CalendarHolidayService (baseActivity);
			service.getHolidays (new SimpleCallback<List<CalendarHolidayDate>> () {
				@Override
				public void onSuccess (List<CalendarHolidayDate> response) {
					// Update list locally
					holidayDateList = response;

					// Save to local storage
					SessionManager.getInstance ().saveHolidayDateList (holidayDateList);
				}

				@Override
				public void onError (String message, int statusCode) {
					baseActivity.showErrorDialog (message);
				}
			});
		}
	}

	public List<CalendarHolidayDate> getHolidayDateList () {
		return holidayDateList;
	}

	public List<CalendarHolidayDate> getNotWorkedHolidayDateList () {
		List<CalendarHolidayDate> dateList = new ArrayList<> ();

		for (CalendarHolidayDate holiday : holidayDateList) {
			switch (holiday.getTypeCode ()) {
				case 1:// Feriado Nacional
				case 2:// Feriado Estadual
				case 3:// Feriado Municipal
					dateList.add (holiday);
					break;
				case 4:// Facultativo
					if (holiday.getName ().equalsIgnoreCase ("Carnaval") || holiday.getName ().equalsIgnoreCase ("Corpus Christi")) {
						dateList.add (holiday);
					}
					break;
			}
		}

		return dateList;
	}
}
