package br.com.covid.app;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class VerificarIDsControles {
	
	public static void main(String[] args) throws IOException {
		Reader reader = Files.newBufferedReader(Paths.get("src/main/resources/csv/controles.csv"));
		CSVReader csvReader = new CSVReaderBuilder(reader).build();
		
		List<String[]> controles = csvReader.readAll();
		
		Set<String> ids = new HashSet<String>();
		
		System.out.println("ID's repetidos na base de controles");
		
		controles.remove(0);
		
		for(String[] registro: controles) {	
			if(ids.contains(registro[5])) {
				System.out.println("ID repetido: " + registro[5]);
			} else {
				ids.add(registro[5]);
			}
		}
	}

}
