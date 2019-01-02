package it.brainmaxz.shazam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GestoreProperties {
	private static final Logger LOG = LoggerFactory.getLogger(GestoreProperties.class);

	private GestoreProperties() {
		super();
	}

	/**
	 * Restituisce, se definito tra le proprietà di sistema, il path del folder di
	 * configurazione configurato sul server. Ovviamente non è detto che corrisponda
	 * effettivamente ad un percorso esistente. Nel caso in cui la variabile esista,
	 * verifica la presenza del carattere di separatore del file. In caso di assenza
	 * lo inserisce. Restituisce null se la variabile non è definita
	 * 
	 * @return
	 */
	@SuppressWarnings("nls")
	public static String getShazamConfigDir() {
		String configDir = System.getProperty(SystemProperties.SHAZAM_CONFIG_DIR);
		LOG.debug("SHAZAM config dir {}", configDir);
		if (configDir != null) {
			/* Verifico che l'ultimo carattere non sia il separatore di path */
			char lastCharacter = configDir.charAt(configDir.length() - 1);
			if (lastCharacter != File.separatorChar) {
				/* Dobbiamo aggiungerlo */
				configDir += File.separatorChar;
			}
			LOG.debug("configDir {}", configDir);
		}
		return configDir;
	}

	/**
	 * Cerca il file nella cartella definita dalla proprietà di sistema
	 * {@link SystemProperties#SHAZAM_CONFIG_DIR} e lo carica su oggetto di tipo
	 * <code>Properties</code>.
	 * 
	 * @param nomeFileProperties il nome del file di properties
	 * @return l'istanza in caso di successo, <code>null</code> in caso di errore
	 */
	private static Properties caricaDaSystemProperties(String nomeFileProperties) {
		Properties config = null;
		String configDir = getShazamConfigDir();
		if (configDir != null) {
			String configFile = configDir + nomeFileProperties;
			LOG.debug("configFile {}", configFile); //$NON-NLS-1$
			config = leggiDaFile(configFile);
		}
		return config;
	}

	/**
	 * Carica un file di properties.
	 * 
	 * @param inConfigFile il path assoluto del file di properties.
	 * @return l'istanza di <code>Properties</code> in caso di successo,
	 *         <code>null</code> altrimenti
	 */
	private static Properties leggiDaFile(String inConfigFile) {
		Properties config = null;
		try (FileInputStream fis = new FileInputStream(new File(inConfigFile))) {
			config = new Properties();
			config.load(fis);
		} catch (FileNotFoundException e) {
			LOG.debug("Errore caricamento file properties '{}'", e //$NON-NLS-1$
					.getMessage());
		} catch (IOException e) {
			LOG.debug("Errore caricamento file properties '{}'", e //$NON-NLS-1$
					.getMessage());
		}
		return config;
	}

	/**
	 * Cerca il file di configurazione all'interno del classpath
	 * 
	 * @param nomeFileProperties il nome del file di properties
	 * @return <code>true</code> in caso di successo
	 */
	private static Properties caricaDaClassPath(String nomeFileProperties) {
		Properties config = null;
		URL url = GestoreProperties.class.getResource("/" + nomeFileProperties); //$NON-NLS-1$
		LOG.debug("URL {}", url); //$NON-NLS-1$
		if (url != null) {
			try {
				config = new Properties();
				config.load(url.openStream());
			} catch (IOException e) {
				LOG.debug("Errore caricamento file properties da classpath '{}'", e //$NON-NLS-1$
						.getMessage());
			}
		}
		return config;
	}

	/**
	 * Tenta di caricare il file di configurazione cercandolo nella user dir.
	 * 
	 * @param nomeFileProperties il nome del file di properties
	 * @return <code>true</code> in caso di successo
	 */
	private static Properties caricaDaUserDir(String nomeFileProperties) {
		Properties config = null;
		String pathAssoluto = System.getProperty("user.dir") + File.separator + nomeFileProperties; //$NON-NLS-1$
		LOG.debug("pathAssoluto {}", pathAssoluto); //$NON-NLS-1$
		config = leggiDaFile(pathAssoluto);
		return config;
	}

	/**
	 * Cerca di caricare un file di properties avente il nome specificato. Attua tre
	 * differenti strategie. In prima istanza determina se è definita la proprietà
	 * di sistema {@link SystemProperties#SHAZAM_CONFIG_DIR}. In caso positivo tenta
	 * di caricare il file di properties dalla posizione indicata dalla variabile.
	 * Nel caso in cui ciò non sia possibile tenta di caricare il file da classpath.
	 * In caso di ulteriore fallimento cerca il file nella cartella individuata
	 * dalla viariabile di sistema <code>user.dir</code>.
	 * 
	 * @param nomeFileProperties il nome del file di properties
	 * @return l'istanza di <code>Properties</code>
	 * @throws SISTAException impossibilità di caricare il file
	 */
	@SuppressWarnings("nls")
	public static Properties caricaProperties(String nomeFileProperties) {
		Properties properties = caricaDaSystemProperties(nomeFileProperties);

		if (properties == null) {
			properties = caricaDaClassPath(nomeFileProperties);
			if (properties == null) {
				properties = caricaDaUserDir(nomeFileProperties);
				if (properties == null) {
					LOG.error("Errore caricamento file properties {}", nomeFileProperties);
				}
			}
		}
		LOG.debug("caricaProperties; nomeFileProperties: {}, result: {}", //$NON-NLS-1$
				nomeFileProperties, properties);
		return properties;
	}

	public static boolean isPropertyFilePresent(String nomeFileProperties) {
		return caricaProperties(nomeFileProperties) != null;
	}

	public static String leggiValoreDaProperties(String nomeFileProperties, String chiave) {
		try {
			Properties properties = caricaProperties(nomeFileProperties);
			return properties.getProperty(chiave);
		} catch (Exception e) {
			LOG.debug("Errore caricamento file properties da classpath '{}'", e //$NON-NLS-1$
					.getMessage());
		}
		return null;
	}

}
