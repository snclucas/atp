package com.atp.data.io;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.atp.util.PropertiesUtil;

public class ResultsWriter {

	Writer out;

	public ResultsWriter() {

	}

	public void open() {
		try {
			out = new OutputStreamWriter(new FileOutputStream("/Users/snclucas/Desktop/results.html"),  "UTF-8");
		}
		catch(Exception c) {
			c.printStackTrace();
		}
	}
	
	
	public void open(String quote) {
		String fileSep = System.getProperty("file.separator");
		String path = PropertiesUtil.pathToData + fileSep + quote + "." + PropertiesUtil.fileType;
		try {
			out = new OutputStreamWriter(new FileOutputStream(path),  "UTF-8");
		}
		catch(Exception c) {
			c.printStackTrace();
		}
	}


	/** Write fixed content to the given file. */
	public void write(String text) {
		try {
			out.write(text);
		}
		catch(Exception c) {
			c.printStackTrace();
		}
	}
	
	
	public void close() {
		try {
			out.close();
		}
		catch(Exception c) {
			c.printStackTrace();
		}
	}

}
