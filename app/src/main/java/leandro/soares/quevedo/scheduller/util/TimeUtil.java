package leandro.soares.quevedo.scheduller.util;

import java.util.Calendar;

public final class TimeUtil {

	public static int extractHoursFromTime (String time) {
		if (time == null || time.isEmpty ())
			return Calendar.getInstance ().get (Calendar.HOUR_OF_DAY);

		String[] tokens = time.split (":");
		return Integer.parseInt (tokens[0]);
	}

	public static int extractMinutesFromTime (String time) {
		if (time == null || time.isEmpty ()) return Calendar.getInstance ().get (Calendar.MINUTE);

		String[] tokens = time.split (":");
		return Integer.parseInt (tokens[1]);
	}

}
