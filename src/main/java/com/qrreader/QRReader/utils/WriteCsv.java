package com.qrreader.QRReader.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

public class WriteCsv {
	private final String filePath = Constants.outputfile;
	private File file = null;
	private FileWriter outputfile = null;
	private CSVWriter writer = null;
	private static WriteCsv writeCsv = null;

	public WriteCsv() {
		try {
			file = new File(filePath);
			outputfile = new FileWriter(file);
			writer = new CSVWriter(outputfile, CSVWriter.DEFAULT_SEPARATOR,
					CSVWriter.NO_QUOTE_CHARACTER,
				    CSVWriter.NO_ESCAPE_CHARACTER,
				    CSVWriter.DEFAULT_LINE_END);
//	        "Number of Pixels,Read QR,Number of Pixels,Read QR,Number of Pixels,Read QR,Number of Pixels,Read QR\n"
			writer.writeNext(new String[] {
					"Number of Pixels,Read QR,Number of Pixels,Read QR,Number of Pixels,Read QR,Number of Pixels,Read QR" });
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized static WriteCsv getInstance() {
		if (writeCsv == null)
			writeCsv = new WriteCsv();
		return writeCsv;
	}

	public CSVWriter getCSVWriter() {
		if (writer == null) {
			setCSVWriter();
		}
//		if(writer.i)
		return writer;
	}

	private synchronized void setCSVWriter() {
		try {
			file = new File(filePath);
			outputfile = new FileWriter(file);
			writer = new CSVWriter(outputfile, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,
					CSVWriter.DEFAULT_LINE_END);
//	        "Number of Pixels,Read QR,Number of Pixels,Read QR,Number of Pixels,Read QR,Number of Pixels,Read QR\n"
			writer.writeNext(new String[] {
					"Number of Pixels,Read QR,Number of Pixels,Read QR,Number of Pixels,Read QR,Number of Pixels,Read QR\n" });
		} catch (IOException e) {
//			System.out.println("Error1");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
