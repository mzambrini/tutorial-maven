package it.brainmaxz.shazam;

import it.brainmaxz.shazam.eccezione.ShazamException;

/**
 * Interfaccia che definisce i metodi di accesso per il recupero dei messaggi da
 * file di properties.
 * 
 * @author Massimiliano Zambrini
 */
public interface GestoreMessaggiErrore extends GestoreBundleMessaggi {

	/**
	 * Crea un messaggio di errore andando a reperire la stringa da un file di
	 * properties ed integrandola con le rappresentazioni in stringa degli oggetti
	 * passati nell'array.
	 * 
	 * @param inCodiceErrore il codice di errore utilizzato per rintracciare la
	 *                       stringa nel file di properties
	 * @param inParametri    un array di oggetti contenenti i dati custom necessari
	 *                       al messaggio parametrico
	 * @return il messaggio determinato
	 */
	String creaMessaggioErrore(int inCodiceErrore, Object... inParametri);

	/**
	 * Metodo di utilità che restituisce direttamente un'istanza dell'eccezione
	 * <code>GtartException</code>.
	 * 
	 * @param inCodiceErrore il codice di errore utilizzato per rintracciare la
	 *                       stringa nel file di properties
	 * @param inParametri    un array di oggetti contenenti i dati custom per
	 *                       generare il messaggio
	 * @return l'eccezione opportunamente costruita
	 */
	ShazamException generaShazamException(int inCodiceErrore, Object... inParametri);

	/**
	 * Metodo di utilità che restituisce direttamente un'istanza dell'eccezione
	 * <code>GtartException</code>.
	 * 
	 * @param inCausa        la causa dell'eccezione
	 * @param inCodiceErrore il codice di errore utilizzato per rintracciare la
	 *                       stringa nel file di properties
	 * @param inParametri    un array di oggetti contenenti i dati custom per
	 *                       generare il messaggio
	 * @return l'eccezione opportunamente costruita
	 */
	ShazamException generaShazamException(Throwable inCausa, int inCodiceErrore, Object... inParametri);

}
