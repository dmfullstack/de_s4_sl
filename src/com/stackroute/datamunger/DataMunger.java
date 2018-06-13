package com.stackroute.datamunger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import com.stackroute.datamunger.query.DataTypeDefinitions;
import com.stackroute.datamunger.query.Header;
import com.stackroute.datamunger.reader.CsvQueryProcessor;

public class DataMunger {

	public static void main(String[] args) {

		Header headers = null;
		DataTypeDefinitions dataTypes = null;
		CsvQueryProcessor csvQueryProcessor = null;
		// read the file name from the user
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the file name:");
		String fileName = scanner.nextLine();

		/*
		 * create object of CsvQueryProcessor. We are trying to read from a file inside
		 * the constructor of this class. Hence, we will have to handle exceptions.
		 */
		try {
			csvQueryProcessor = new CsvQueryProcessor(fileName);
		} catch (FileNotFoundException e1) {
			System.out.println("File not found!!!!");
		}

		try {
			// call getHeader() method to get the array of headers
			headers = csvQueryProcessor.getHeader();
			/*
			 * call getColumnType() method of CsvQueryProcessor class to retrieve the array
			 * of column data types which is actually the object of DataTypeDefinitions
			 * class
			 */
			dataTypes = csvQueryProcessor.getColumnType();
		} catch (IOException e) {

			e.printStackTrace();
		}
		/*
		 * display the columnName from the header object along with its data type from
		 * DataTypeDefinitions object
		 */
		for (int index = 0; index < headers.getHeaders().length; index++)
			System.out.println(headers.getHeaders()[index] + ":" + dataTypes.getDataTypes()[index]);

	}

}
