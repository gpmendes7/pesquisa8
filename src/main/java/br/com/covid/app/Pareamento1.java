package br.com.covid.app;

import static br.com.covid.util.StringUtil.normalizarString;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

public class Pareamento1 {
	
	public static void main(String[] args) throws IOException, ParseException {
        List<String[]> casos = carregarRegistros("src/main/resources/csv/casos.csv");
		
		casos = casos.stream()
		             .skip(1)
		             .collect(Collectors.toList());
		
		System.out.println("casos.size(): " + casos.size());
		
        List<String[]> controles = carregarRegistros("src/main/resources/csv/controles.csv");
		
        controles = controles.stream()
		         			 .skip(1)
		                     .collect(Collectors.toList());
        
		System.out.println("controles.size(): " + controles.size());
		
		List<String[]> pareamento = new ArrayList<String[]>();
		
		pareamento.addAll(casos);
		
		for (String[] caso : casos) {	
			List<String[]> controlesFiltradosAux;
			List<String[]> controlesFiltrados = filtrarControlesNaoUsados(controles, 6);
			System.out.println("Número de controles filtrados não usados: " + controlesFiltrados.size());
			
			controlesFiltradosAux = filtrarPorCampo(controlesFiltrados, caso[1], 1);
			if(controlesFiltradosAux.size() >= 5) {
				controlesFiltrados = controlesFiltradosAux;
				System.out.println("raca: " + caso[1]);
				System.out.println("Número de controles filtrados por campo raca: " + controlesFiltrados.size());
			}
			
			controlesFiltradosAux = filtrarPorCampo(controlesFiltrados, caso[2], 2);
			if(controlesFiltradosAux.size() >= 5) {
				controlesFiltrados = controlesFiltradosAux;
				System.out.println("regiao: " + caso[2]);
				System.out.println("Número de controles filtrados por campo regiao: " + controlesFiltrados.size());
			}
			
			controlesFiltradosAux =  filtrarPorCampo(controlesFiltrados, caso[3], 3);
			if(controlesFiltradosAux.size() >= 5) {
				controlesFiltrados = controlesFiltradosAux;
				System.out.println("sexo: " + caso[3]);
				System.out.println("Número de controles filtrados por campo sexo: " + controlesFiltrados.size());
			}
						
			controlesFiltrados = marcarRegistros(controlesFiltrados, 5, 6);
			System.out.println("Número de controles marcados como usados: " + controlesFiltrados.size());
			
			for(String[] controleFiltrado: controlesFiltrados) {
				String[] registroControleFiltrado = {controleFiltrado[0], controleFiltrado[1], controleFiltrado[2], controleFiltrado[3], controleFiltrado[4], controleFiltrado[5]};
				pareamento.add(registroControleFiltrado);
			}
			
			//System.out.println("Número de registros pareados: " + pareamento.size());
		}
		
		salvarRegistros(pareamento);
		
	}

	private static List<String[]> carregarRegistros(String nomeArquivo) throws IOException {
		Reader reader = Files.newBufferedReader(Paths.get(nomeArquivo));
		CSVReader csvReader = new CSVReaderBuilder(reader).build();
		
		return csvReader.readAll();
	}
	
	private static void salvarRegistros(List<String[]> pareamento) throws ParseException, FileNotFoundException, IOException {
		 String[] cabecalho = {"grupo", "raca", "regiao", "sexo", "ufres", "id"};
	        
		 pareamento.add(0, cabecalho);
		 		 
		 String nomeArquivo = "src/main/resources/csv/pareamento.csv";

         try (var fos = new FileOutputStream(nomeArquivo);
             var osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             var writer = new CSVWriter(osw)) {
             writer.writeAll(pareamento);
        }
		
	}
	
	private static List<String[]> filtrarPorCampo(List<String[]> registros, String campo, int indiceCampo) {
		return registros.stream()
				        .filter(r -> normalizarString(r[indiceCampo]).equals(normalizarString(campo)))
				        .collect(Collectors.toList());
	}
	
	private static List<String[]> filtrarControlesNaoUsados(List<String[]> registros, int indiceObservacaoUsoControles) {
		return registros.stream()
				.filter(r -> r[indiceObservacaoUsoControles] == null || r[indiceObservacaoUsoControles].equals(""))
				.collect(Collectors.toList());
	}
	
	private static List<String[]> marcarRegistros(List<String[]> registros, int qtd, int indiceObservacaoUso) {

		registros.stream()
		         .limit(qtd)
		         .forEach(r -> r[indiceObservacaoUso] = "usado");
		
		List<String[]> registrosUsados = registros.stream()
			                                    .filter(r -> r[indiceObservacaoUso] != null 
			                                            && !r[indiceObservacaoUso].equals(""))
			                                    .collect(Collectors.toList());
		
		return registrosUsados;
	}
	
}
