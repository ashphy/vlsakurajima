package com.appspot.vlsakurajima.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.mozilla.universalchardet.UniversalDetector;

public class AutoEncodeDetectReader {

	public static String getAsString(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UniversalDetector detector = new UniversalDetector(null);
        byte[] buf = new byte[4096];
        int nread;
	    while ((nread = in.read(buf)) > 0) {
	    	if(!detector.isDone()) {
	    		detector.handleData(buf, 0, nread);
	    	}
	    	out.write(buf, 0, nread);
		}
	    detector.dataEnd();
        String string = new String(out.toByteArray(), Charset.forName(detector.getDetectedCharset()));
        return string;
	}
	
}
