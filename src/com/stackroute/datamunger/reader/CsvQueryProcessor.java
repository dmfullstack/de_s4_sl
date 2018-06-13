package com.stackroute.datamunger.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.stackroute.datamunger.query.DataTypeDefinitions;
import com.stackroute.datamunger.query.Header;

public class CsvQueryProcessor extends QueryProcessingEngine {

	private String fileName;
	BufferedReader reader = null;

	/*
	 * parameterized constructor to initialize filename. As you are trying to
	 * perform file reading, hence you need to be ready to handle the IO Exceptions.
	 */

	public CsvQueryProcessor(String fileName) throws FileNotFoundException {
		super();
		this.fileName = fileName;
		reader = new BufferedReader(new FileReader(fileName));
	}

	/*
	 * implementation of getHeader() method. We will have to extract the headers
	 * from the first line of the file.
	 */
	@Override
	public Header getHeader() throws IOException {

		String header = null;
		reader = new BufferedReader(new FileReader(fileName));
		// read the first line
		header = reader.readLine();
		// populate the header object with the String array containing the header names
		Header headers = new Header(header.split("\\s*,\\s*"));
		return headers;

	}

	/**
	 * This method will be used in the upcoming assignments
	 */
	@Override
	public void getDataRow() {

	}

	/*
	 * implementation of getColumnType() method. To find out the data types, we will
	 * read the first line from the file and extract the field values from it. In
	 * the previous assignment, we have tried to convert a specific field value to
	 * Integer or Double. However, in this assignment, we are going to use Regular
	 * Expression to find the appropriate data type of a field. Integers: should
	 * contain only digits without decimal point Double: should contain digits as
	 * well as decimal point Date: Dates can be written in many formats in the CSV
	 * file. However, in this assignment,we will test for the following date
	 * formats('dd/mm/yyyy',
	 * 'mm/dd/yyyy','dd-mon-yy','dd-mon-yyyy','dd-month-yy','dd-month-yyyy','yyyy-mm-dd')
	 */
	@Override
	public DataTypeDefinitions getColumnType() throws IOException {

		String row;
		String[] rows;
		String[] dataTypeArray;

		row = reader.readLine();
		rows = row.split("\\s*,\\s*", -1);
		dataTypeArray = new String[rows.length];

		for (int index = 0; index < rows.length; index++) {

			dataTypeArray[index] = getDataType(rows[index]).getClass().getName();
		}

		DataTypeDefinitions dataTypes = new DataTypeDefinitions(dataTypeArray);

		return dataTypes;
	}

	private Object getDataType(String input) {
		if (input.isEmpty()) {
			return new Object();
		}
		// checking for Integer
		else if (input.matches("^[0-9]+$"))
			return getIntegerValue(input);
		// checking for floating point numbers
		else if (input.matches("^[0-9\\.]*"))
			return getDoubleValue(input);
		// checking for date format dd/mm/yyyy
		else if (input.matches("^[0-3]{1}[0-9]{1}\\/[0-1]{1}[0-9]{1}\\/[0-9]{4}"))
			return getDateValue(input, "dd/MM/yyyy");
		// checking for date format mm/dd/yyyy
		else if (input.matches("^[0-1]{1}[0-9]{1}\\/[0-3]{1}[0-9]{1}\\/[0-9]{4}"))
			return getDateValue(input, "MM/dd/yyyy");
		// checking for date format dd-mon-yy
		else if (input.matches("^[0-9]{2}-[a-zA-Z]{3}-[0-9]{2}"))
			return getDateValue(input, "dd-MMM-yy");
		// checking for date format dd-mon-yyyy
		else if (input.matches("^[0-9]{2}-[a-zA-Z]{3}-[0-9]{4}"))
			return getDateValue(input, "dd-MMM-yyyy");
		// checking for date format dd-month-yy
		else if (input.matches("^[0-9]{2}-[a-zA-Z]{3}[a-z]+-[0-9]{2}"))
			return getDateValue(input, "dd-MMMM-yy");
		// checking for date format dd-month-yyyy
		else if (input.matches("^[0-9]{2}-[a-zA-Z]{3}[a-z]+-[0-9]{4}"))
			return getDateValue(input, "dd-MMMM-yyyy");
		// checking for date format yyyy-mm-dd
		else if (input.matches("^[0-9]{4}-[0-9]{2}-[0-3]{1}[0-9]{1}"))
			return getDateValue(input, "yyyy-MM-dd");
		else
			return input;
	}

	private Integer getIntegerValue(String text) {
		Integer number = Integer.parseInt(text);
		return number;
	}

	private Double getDoubleValue(String text) {
		Double number = Double.parseDouble(text);
		return number;
	}

	private Date getDateValue(String text, String dateFormat) {
		DateFormat df = new SimpleDateFormat(dateFormat);
		Date date = null;
		try {
			date = df.parse(text);
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return date;
	}

}
