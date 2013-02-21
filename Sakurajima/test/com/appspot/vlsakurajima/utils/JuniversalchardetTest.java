package com.appspot.vlsakurajima.utils;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mozilla.universalchardet.Constants;
import org.mozilla.universalchardet.UniversalDetector;

public class JuniversalchardetTest {

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

	public String detect(String fileName) throws Exception {
	    byte[] buf = new byte[4096];
	    InputStream in = this.getClass().getResourceAsStream(fileName);
	    UniversalDetector detector = new UniversalDetector(null);
	    int nread;
	    while ((nread = in.read(buf)) > 0 && !detector.isDone()) {
	      detector.handleData(buf, 0, nread);
	    }
	    detector.dataEnd();
	    in.close();
	    return detector.getDetectedCharset();
	}
	
	@Test
	public void test() throws Exception {
		assertThat("SHIFT_JIS", is(detect("volinfo-sjis.html")));
		assertThat("EUC-JP", is(detect("volinfo-eucjp.html")));
		assertThat("UTF-8", is(detect("volinfo-utf8.html")));
	}
	
	@Test
	public void testCharset() throws Exception {
		assertThat("Shift_JIS", is(Charset.forName(Constants.CHARSET_SHIFT_JIS).name()));
		assertThat("EUC-JP", is(Charset.forName(Constants.CHARSET_EUC_JP).name()));
		assertThat("UTF-8", is(Charset.forName(Constants.CHARSET_UTF_8).name()));
	}
}
