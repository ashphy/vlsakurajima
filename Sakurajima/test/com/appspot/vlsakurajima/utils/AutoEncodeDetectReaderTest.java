package com.appspot.vlsakurajima.utils;

import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AutoEncodeDetectReaderTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	InputStream getStream(String fileName) {
		return getClass().getResourceAsStream(fileName);
	}
	
	@Test
	public void testGetAsString() throws Exception {
		assertThat(1740725240, is(AutoEncodeDetectReader.getAsString(getStream("volinfo-sjis.html")).hashCode()));
		assertThat(1740725240, is(AutoEncodeDetectReader.getAsString(getStream("volinfo-eucjp.html")).hashCode()));
		assertThat(1740725240, is(AutoEncodeDetectReader.getAsString(getStream("volinfo-utf8.html")).hashCode()));
	}

}
