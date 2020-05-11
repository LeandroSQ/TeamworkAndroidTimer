package leandro.soares.quevedo.scheduller.interfaces;

public interface SimpleCallback <T> {

	void onSuccess (T response);

	void onError (String message, int statusCode);

}
