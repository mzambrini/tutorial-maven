package it.aci.informatica.shazam.properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Test;

import it.brainmaxz.shazam.properties.GestoreBundleMessaggiImpl;

@SuppressWarnings("nls")
public class TestGestoreBundleMessaggiImpl {

	private static final String PRIMO = "primo";
	private static final String SECONDO = "secondo";
	private static final String TERZO = "terzo";
	private static final String PREFISSO = "pref";
	private GestoreBundleMessaggiImpl gbm;

	@Test
	public void testGetNomiBundles() {
		this.gbm = new GestoreBundleMessaggiImpl(PRIMO, SECONDO, TERZO);
		assertEquals(Arrays.asList(PRIMO, SECONDO, TERZO), this.gbm.getNomiBundles());
	}

	@Test
	public void testGetStringThreeBundles() {
		this.gbm = new GestoreBundleMessaggiImpl(PRIMO, SECONDO, TERZO);
		assertEquals("ciao", this.gbm.getString("100"));
		assertEquals("pino", this.gbm.getString("200"));
		assertEquals("girello", this.gbm.getString("300"));
		assertEquals("!400!", this.gbm.getString("400"));
	}

	@Test
	public void testGetStringTwoBundles() {
		this.gbm = new GestoreBundleMessaggiImpl(SECONDO, TERZO);
		assertEquals("gino", this.gbm.getString("100"));
		assertEquals("pino", this.gbm.getString("200"));
		assertEquals("girello", this.gbm.getString("300"));
		assertEquals("!400!", this.gbm.getString("400"));
	}

	@Test
	public void testGetStringOneBundle() {
		this.gbm = new GestoreBundleMessaggiImpl(TERZO);
		assertEquals("cotoletta", this.gbm.getString("100"));
		assertEquals("filetto", this.gbm.getString("200"));
		assertEquals("girello", this.gbm.getString("300"));
		assertEquals("!400!", this.gbm.getString("400"));
	}

	@Test
	public void testGetStringNoBundle() {
		this.gbm = new GestoreBundleMessaggiImpl();
		assertEquals("!100!", this.gbm.getString("100"));
		assertEquals("!200!", this.gbm.getString("200"));
		assertEquals("!300!", this.gbm.getString("300"));
		assertEquals("!400!", this.gbm.getString("400"));
	}

	@Test
	public void testGetStringWithPrefix() {
		this.gbm = new GestoreBundleMessaggiImpl(PREFISSO);
		assertEquals("nipote", this.gbm.getString("prefisso", "nonno"));
	}

	@Test
	public void testGetStringWithPrefixNull() {
		this.gbm = new GestoreBundleMessaggiImpl(PREFISSO);
		try {
			this.gbm.getString(null, "nonno");
			fail();
		} catch (@SuppressWarnings("unused") IllegalArgumentException e) {
			// Nulla
		}
	}

}
