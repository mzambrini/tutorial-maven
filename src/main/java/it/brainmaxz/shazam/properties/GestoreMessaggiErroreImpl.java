package it.brainmaxz.shazam.properties;

import it.brainmaxz.shazam.GestoreMessaggiErrore;
import it.brainmaxz.shazam.eccezione.ShazamException;

public class GestoreMessaggiErroreImpl extends GestoreBundleMessaggiImpl implements GestoreMessaggiErrore {
	private static final String PREFIX = "errore"; //$NON-NLS-1$

	/**
	 * Costruttore per l'utilizzo standard.
	 * 
	 * @param inNomiBundles la lista dei nomi dei vari bundles
	 */
	public GestoreMessaggiErroreImpl(String... inNomiBundles) {
		super(inNomiBundles);
	}

	@Override
	public String creaMessaggioErrore(int inCodiceErrore, Object... inParametri) {
		return creaMessaggio(PREFIX, String.valueOf(inCodiceErrore), inParametri);
	}

	@Override
	public ShazamException generaShazamException(int inCodiceErrore, Object... inParametri) {
		return new ShazamException(creaMessaggioErrore(inCodiceErrore, inParametri), inCodiceErrore);
	}

	@Override
	public ShazamException generaShazamException(Throwable inCausa, int inCodiceErrore, Object... inParametri) {
		return new ShazamException(inCausa, creaMessaggioErrore(inCodiceErrore, inParametri), inCodiceErrore);
	}

}
