package ru.clevertec.knyazev.data;

import java.io.IOException;
import java.util.Map;

public class DataReaderWriterDecorator implements DataReader, DataWriter{
	private DataReader dataReader;
	private DataWriter dataWriter;
	
	
	public DataReaderWriterDecorator(DataReader dataReader, DataWriter dataWriter) {
		this.dataReader = dataReader;
		this.dataWriter = dataWriter;
	}

	@Override
	public void writeData(String data) throws IOException {
		dataWriter.writeData(data);		
	}

	@Override
	public Map<String[], String[]> readData() throws IOException {
		return dataReader.readData();
	}
	
}
