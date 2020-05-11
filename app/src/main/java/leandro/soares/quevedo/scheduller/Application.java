package leandro.soares.quevedo.scheduller;

import leandro.soares.quevedo.scheduller.util.SessionManager;

public class Application extends android.app.Application {

	@Override
	public void onCreate () {
		super.onCreate ();

		SessionManager.initialize (this);
	}
}
