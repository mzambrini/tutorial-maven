package it.brainmaxz.shazam;

/**
 * Interfaccia che definisce i metodi di accesso per il recupero dei messaggi da
 * file di properties.
 * 
 * @author Massimiliano Zambrini
 */
public interface GestoreBundleMessaggi {
	String getString(String inChiave);

	String getString(String inPrefisso, String inChiave);

	String creaMessaggio(String inChiave, Object... inParametri);

	String creaMessaggio(String inPrefisso, String inChiave, Object... inParametri);

}
