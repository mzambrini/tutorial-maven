package it.brainmaxz.shazam.eccezione;

/**
 * @author Massimiliano Zambrini
 */
public class ShazamException extends Exception {

	private static final long serialVersionUID = 1234571155622771231L;

	private final int codiceErrore;

	/**
	 * Costruttore per l'eccezione.
	 * 
	 * @param inCausa           istanza di <code>Throwable</code>
	 * @param inMessaggioErrore il messaggio associato a questo errore
	 * @param inCodiceErrore    il codice dell'errore.
	 */
	public ShazamException(Throwable inCausa, String inMessaggioErrore, int inCodiceErrore) {
		super(inMessaggioErrore, inCausa);
		this.codiceErrore = inCodiceErrore;
	}

	/**
	 * Costruttore per l'eccezione. Codice di errore = 0. Messaggio null.
	 * 
	 * @param inCausa istanza di <code>Throwable</code>
	 */
	public ShazamException(Throwable inCausa) {
		this(inCausa, null, 0);
	}

	/**
	 * Costruttore.
	 * 
	 * @param inMessaggioErrore il messaggio associato a questo errore.
	 * @param inCodiceErrore    il codice dell'errore.
	 * 
	 */
	public ShazamException(String inMessaggioErrore, int inCodiceErrore) {
		this(null, inMessaggioErrore, inCodiceErrore);
	}

	/**
	 * @return Restituisce il campo codiceErrore.
	 */
	public final int getCodiceErrore() {
		return this.codiceErrore;
	}

}
