/**
 * 
 */
package it.brainmaxz.shazam.properties;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.brainmaxz.shazam.GestoreBundleMessaggi;

public class GestoreBundleMessaggiImpl implements GestoreBundleMessaggi {

	private static final Logger LOGGER = LoggerFactory.getLogger(GestoreBundleMessaggiImpl.class);
	/** La lista di bundles contenenti i messaggi di errore. */
	private final List<ResourceBundle> resourceBundles;
	/** La lista dei nomi dei bundles da caricare. */
	private final List<String> nomiBundles;

	/**
	 * Costruttore. Riceve in ingresso il nome del bundle da caricare. Invoca il
	 * metodo init per avviare l'inizializzazione.
	 * 
	 * @param inNomeBundle il nome del bundle da caricare
	 */
	public GestoreBundleMessaggiImpl(String... inNomiBundles) {
		super();
		this.nomiBundles = Arrays.asList(inNomiBundles);
		this.resourceBundles = new ArrayList<>();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Costruttore; nomeBundle: {}", Arrays.toString(inNomiBundles)); //$NON-NLS-1$
		}
		init();
	}

	@SuppressWarnings("nls")
	private void init() {
		ResourceBundle bundle = null;
		for (String nome : this.nomiBundles) {
			try {
				bundle = ResourceBundle.getBundle(nome);
				if (LOGGER.isTraceEnabled()) {
					for (String key : bundle.keySet()) {
						String value = bundle.getString(key);
						LOGGER.trace("Chiave: {}, Valore: {}", key, value);
					}
				}
				this.resourceBundles.add(bundle);
			} catch (RuntimeException e) {
				LOGGER.warn("Eccezione sul caricamento del bundle specifico; classe: {}, messsaggio: {}", e.getClass(), //$NON-NLS-1$
						e.getMessage());
			}
		}
	}

	/**
	 * @return the nomeBundle
	 */
	public List<String> getNomiBundles() {
		return new ArrayList<>(this.nomiBundles);
	}

	/**
	 * Restituisce il messaggio individuato dalla chiave specifica. Ricerca nella
	 * lista di bundles. Se non trova la chiave procede col successivo. Se non trova
	 * la chiave su tutti i bundles definiti restituisce la chiave stessa tra due
	 * punti esclamativi.
	 * 
	 * @param inChiave la chiave
	 * @return la stringa ottenuta
	 */
	@SuppressWarnings("nls")
	@Override
	public String getString(String inChiave) {
		String valore = '!' + inChiave + '!';
		for (int i = 0; i < this.resourceBundles.size(); i++) {
			ResourceBundle bundle = this.resourceBundles.get(i);
			LOGGER.trace("analisi del bundle '{}'", this.nomiBundles.get(i));
			try {
				valore = bundle.getString(inChiave);
				LOGGER.trace("usando la chiave {} ho trovato il valore {}", inChiave, valore);
				break;
			} catch (@SuppressWarnings("unused") MissingResourceException e) {
				LOGGER.trace("usando la chiave {} non ho trovato valori", inChiave);
			}
		}
		return valore;
	}

	@SuppressWarnings("nls")
	@Override
	public final String getString(String inPrefisso, String inChiave) {
		if (inPrefisso == null) {
			LOGGER.error("passato prefisso nullo");
			throw new IllegalArgumentException("Il prefisso non può essere nullo");
		}
		return getString(inPrefisso + "." + inChiave);
	}

	@Override
	public final String creaMessaggio(String inChiave, Object... inParametri) {
		MessageFormat mf = null;
		String template = getString(inChiave);
		mf = new MessageFormat(template);
		return mf.format(inParametri);
	}

	@Override
	public final String creaMessaggio(String inPrefisso, String inChiave, Object... inParametri) {
		MessageFormat mf = null;
		String template = getString(inPrefisso, inChiave);
		mf = new MessageFormat(template);
		return mf.format(inParametri);
	}

}
