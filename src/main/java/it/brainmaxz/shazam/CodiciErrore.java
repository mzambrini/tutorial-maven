package it.brainmaxz.shazam;

/**
 * Contiene i codici degli errori comuni a tutto il progetto SHAZAM. Il range di
 * errori è 10000 - 19999
 * 
 * @author Massimiliano Zambrini
 * 
 */
public final class CodiciErrore {

	public static final int DEFAULT = 10000;

	public static final int ERRORE_SQL_EXCEPTION = 12000;

	public static final int ERRORE_UNSUPPORTED_ENCODING_EXCEPTION = 12100;

	public static final int ERRORE_CREAZIONE_INITIAL_CONTEXT = 12200;

	public static final int ERRORE_REMOTE_EXCEPTION = 12300;

	public static final int ERRORE_CREATE_EXCEPTION = 12400;

	public static final int ERRORE_GENERICO_EJB_AUTENTICAZIONE = 15001;

	public static final int FILE_CONFIGURAZIONE_ASSENTE = 17000;

	public static final int ERRORE_CREAZIONE_CONNESSIONE = 17050;

	public static final int FALLIMENTO_CREAZIONE_INITIAL_CONTEXT = 19000;

	public static final int NOME_LOGICO_SCONOSCIUTO = 19050;

	public static final int ERRORE_CREAZIONE_DATASOURCE = 19150;

	public static final int ERRORE_NEI_DATI = 19200;

	public static final int ERRORE_GENERICO = 19300;

	public static final int ERRORE_WEB_SERVICES = 19400;

	private CodiciErrore() {
		super();
	}
}
