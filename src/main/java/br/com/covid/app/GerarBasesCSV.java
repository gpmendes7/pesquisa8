package br.com.covid.app;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

public class GerarBasesCSV {
	
	public static void main(String[] args) throws IOException {
		Reader reader = Files.newBufferedReader(Paths.get("src/main/resources/csv/arquivo.csv"));
		CSVReader csvReader = new CSVReaderBuilder(reader).build();
		
		List<String[]> casos = new ArrayList<String[]>();
		List<String[]> controles = new ArrayList<String[]>();
		
		List<String[]> registros = csvReader.readAll();
				
		for(String[] registro: registros) {	
			if(registro[0].equals("0-Redome")) {
				String[] registroControles = {registro[0], registro[1], registro[2], registro[3], registro[4], registro[5], ""};
				controles.add(registroControles);
			} else if(registro[0].equals("1-DM1")) {
				casos.add(registro);
			}
		}
		
		System.out.println("casos: " + casos.size());
		System.out.println("controles: " + controles.size());
		
		casos.add(0, registros.get(0));
		
		String[] cabecalhoControles = {"grupo", "raca", "regiao", "sexo", "ufres", "id", "observacaoUso"};
		
		controles.add(0, cabecalhoControles);
		
		String arquivoCasos = "src/main/resources/csv/casos.csv";

        try (var fos = new FileOutputStream(arquivoCasos);
             var osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             var writer = new CSVWriter(osw)) {
             writer.writeAll(casos);
		
	    }
        
        String arquivoControles = "src/main/resources/csv/controles.csv";

        try (var fos = new FileOutputStream(arquivoControles);
             var osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             var writer = new CSVWriter(osw)) {
             writer.writeAll(controles);
		
	    }
        
	} 

}
