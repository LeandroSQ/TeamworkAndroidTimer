package leandro.soares.quevedo.scheduller.service;

import java.util.List;

import leandro.soares.quevedo.scheduller.model.calendar.CalendarHolidayDate;
import leandro.soares.quevedo.scheduller.model.calendar.service.CalendarGetHolidaysResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ICalendarHolidayAPI {

	@GET("https://api.calendario.com.br/?json=true&token=[seu-token]&ano=2018&ibge=3550308")
	Call<List<CalendarHolidayDate>> getHolidays (
			@Query("json") boolean json,
			@Query("token") String token,
			@Query("ano") int year,
			@Query("estado") String uf,
			@Query("city") String city
	);

}
