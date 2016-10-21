package modeloVectorial;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.lang.Math;

public class Calculos {

	public Map<String, Double> calcularIDF(int numeroDocumentos, Map<String, Map<Integer, Integer>> diccionario, Map<String, Integer> consulta){
		
		Map<String, Double> IDF = new HashMap<String, Double>();	/* IDF de cada de una de las palabras del diccionario y consulta */
		Map<String, Integer> Count = new HashMap<String, Integer>();

		Double aux = (double) numeroDocumentos;
		
		for (Entry<String, Map<Integer, Integer>> entry : diccionario.entrySet()){
			int count = entry.getValue().size();
			String palabra = entry.getKey();
			if(consulta.containsKey(palabra)){
				Count.put(palabra, count+1);
			}else{
				Count.put(palabra, count);

			}
			IDF.put(palabra, Math.log10(aux / Count.get(palabra)));
		}
		
		/* Insertamos las palabras que aparezcan en la consulta pero no en el diccionario */
		for (Entry<String, Integer> entry : consulta.entrySet()){
			String palabra = entry.getKey();
			if(!diccionario.containsKey(palabra)){
				Count.put(palabra, 1);
			}

			IDF.put(palabra, Math.log10(aux / Count.get(palabra)));
		}
		
		return IDF;
	}
	
	public Map<Integer, Double> ProductoEscalarTF(int numeroDocumentos, Map<String, Map<Integer, Integer>> diccionario, Map<String, Integer> consulta){
		
		Map<Integer, Double> TF = new HashMap<Integer, Double>();	/* TF de cada una de los documentos */
		Map<String, Map<Integer, Integer>> palabrasTF = palabrasTF(diccionario, consulta);
		
		for (Entry<String, Map<Integer, Integer>> entry : palabrasTF.entrySet()){
			String palabra = entry.getKey();
			for (Entry<Integer, Integer> entry2 : entry.getValue().entrySet()){
				Integer doc = entry2.getKey();
				if(!TF.containsKey(doc)){
					TF.put(doc, (double)(entry2.getValue() * consulta.get(palabra)));
				}else{
					TF.put(doc, TF.get(doc) + entry2.getValue() * consulta.get(palabra));
				}
			}
		}
		
		for (int i=0 ; i<numeroDocumentos ; i++){
			if(!TF.containsKey(i)){
				TF.put(i, 0.0);
			}
		}
		
		return TF;
	}

	public Map<Integer, Double> ProductoEscalarTFIDF(int numeroDocumentos, Map<String, Map<Integer, Integer>> diccionario, Map<String, Integer> consulta, Map<String, Double> valoresIDF){
		
		Map<Integer, Double> TFIDF = new HashMap<Integer, Double>();	/* TFIDF de cada uno de los documentos */
		Map<String, Map<Integer, Integer>> palabrasTF = palabrasTF(diccionario, consulta);
		Map<String, Map<Integer, Double>> palabrasIDF = palabrasIDF(palabrasTF, valoresIDF);		
		
		for (Entry<String, Map<Integer, Double>> entry : palabrasIDF.entrySet()){
			String palabra = entry.getKey();
			for (Entry<Integer, Double> entry2 : entry.getValue().entrySet()){
				Integer doc = entry2.getKey();
				if(!TFIDF.containsKey(doc)){
					TFIDF.put(doc, (entry2.getValue() * (consulta.get(palabra) * valoresIDF.get(palabra))));
				}else{
					TFIDF.put(doc, TFIDF.get(doc) + entry2.getValue() * (consulta.get(palabra) * valoresIDF.get(palabra)));
				}
			}
		}
		
		for (int i=0 ; i<numeroDocumentos ; i++){
			if(!TFIDF.containsKey(i)){
				TFIDF.put(i, 0.0);
			}
		}
		
		return TFIDF;
	}
	
	public Map<Integer, Double> CosenoTF(Map<String, Map<Integer, Integer>> diccionario, Map<Integer, Double> calculoTF, Map<String, Integer> consulta){
		
		Map<String, Map<Integer, Integer>> elevarCuadradoTF = elevarCuadradoTF(diccionario);
		Map<Integer, Double> COSTF = new HashMap<Integer, Double>(); 
		
		for (Entry<String, Map<Integer, Integer>> entry : elevarCuadradoTF.entrySet()){
			for (Entry<Integer, Integer> entry2 : entry.getValue().entrySet()){
				Integer doc = entry2.getKey();
				if(!COSTF.containsKey(doc)){
					double aux = (double) entry2.getValue();
					COSTF.put(doc, aux);
				}else{
					COSTF.put(doc, COSTF.get(doc) + entry2.getValue());
				}
			}
		}
		
		double raizQuery = 0.0;
		
		/* Calculamos el valor de la raiz de la consulta */
		for (Entry<String, Integer> entry : consulta.entrySet()){
			raizQuery = raizQuery + Math.pow(entry.getValue(), 2);
		}

		raizQuery = Math.sqrt(raizQuery);
		for (Entry<Integer, Double> entry : COSTF.entrySet()){
			
			if(calculoTF.containsKey(entry.getKey())){
				double raiz = calculoTF.get(entry.getKey()) / (raizQuery * Math.sqrt(entry.getValue()));
				COSTF.put(entry.getKey(), raiz);
			}else{
				COSTF.put(entry.getKey(), 0.0);
			}
		}
		
		return COSTF;
	}

	public Map<Integer, Double> CosenoTFIDF(Map<String, Map<Integer, Integer>> diccionario, Map<Integer, Double> calculoTFIDF, Map<String, Double> valoresIDF, Map<String, Integer> consulta){
		
		Map<String, Map<Integer, Double>> elevarCuadradoTFIDF = elevarCuadradoIDF(diccionario, valoresIDF);
		Map<Integer, Double> COSTFIDF = new HashMap<Integer, Double>(); 
		for (Entry<String, Map<Integer, Double>> entry : elevarCuadradoTFIDF.entrySet()){
			for (Entry<Integer, Double> entry2 : entry.getValue().entrySet()){
				Integer doc = entry2.getKey();
				if(!COSTFIDF.containsKey(doc)){
					double aux = (double) entry2.getValue();
					COSTFIDF.put(doc, aux);
				}else{
					COSTFIDF.put(doc, COSTFIDF.get(doc) + entry2.getValue());
				}
			}
		}
		
		double raizQuery = 0.0;
		
		/* Calculamos el valor de la raiz de la consulta */
		for (Entry<String, Integer> entry : consulta.entrySet()){
			raizQuery = raizQuery + (Math.pow(entry.getValue()*valoresIDF.get(entry.getKey()), 2));
		}

		raizQuery = Math.sqrt(raizQuery);
		for (Entry<Integer, Double> entry : COSTFIDF.entrySet()){
			
			if(calculoTFIDF.containsKey(entry.getKey())){
				double raiz = calculoTFIDF.get(entry.getKey()) / (raizQuery * Math.sqrt(entry.getValue()));
				COSTFIDF.put(entry.getKey(), raiz);
			}else{
				COSTFIDF.put(entry.getKey(), 0.0);
			}
		}
		return COSTFIDF;
		
	}
	
	/* Metodo auxiliar para obtener las palabras del diccionario que estan en la consulta */
 	public static Map<String, Map<Integer, Integer>> palabrasTF(Map<String, Map<Integer, Integer>> diccionario, Map<String, Integer> consulta){
		
		Map<String, Map<Integer, Integer>> palabrasTF = new HashMap<String, Map<Integer, Integer>>();
		
		for (Entry<String, Integer> entry : consulta.entrySet()){
			String palabra = entry.getKey();
			if(diccionario.containsKey(palabra)){
				palabrasTF.put(palabra, diccionario.get(palabra));
			}else{
				
			}
		}
		
		return palabrasTF;
	}

 	/* Metodo auxiliar para obtener las frecuencias por el valor de IDF de palabras del diccionario que estan en la consulta */
 	public static Map<String, Map<Integer, Double>> palabrasIDF(Map<String, Map<Integer, Integer>> palabrasTF, Map<String, Double> valoresIDF){
		
		Map<String, Map<Integer, Double>> palabrasIDF = new HashMap<String, Map<Integer, Double>>();
		
		for (Entry<String, Map<Integer, Integer>> entry : palabrasTF.entrySet()){
			String palabra = entry.getKey();
			for (Entry<Integer, Integer> entry2 : entry.getValue().entrySet()){
				Integer doc = entry2.getKey();
				if(palabrasIDF.containsKey(palabra)){
					palabrasIDF.get(palabra).put(doc, entry2.getValue() * valoresIDF.get(palabra));
				}else{
					Map<Integer, Double> aux = new HashMap<Integer, Double>();
					aux.put(doc, entry2.getValue() * valoresIDF.get(palabra));
					palabrasIDF.put(palabra, aux);
				}
			}
		}
		
		return palabrasIDF;
	}
 	
 	/* Metodo auxiliar que eleva el numero de apariciones de las palabras al cuadrado */
 	public static Map<String, Map<Integer, Integer>> elevarCuadradoTF(Map<String, Map<Integer, Integer>> diccionario){

 		Map<String, Map<Integer, Integer>> diccionarioTF = new HashMap<String, Map<Integer, Integer>>();
 		
 		for (Entry<String, Map<Integer, Integer>> entry : diccionario.entrySet()){
 			String palabra = entry.getKey();
			for (Entry<Integer, Integer> entry2 : entry.getValue().entrySet()){
				Integer doc = entry2.getKey();
				if(diccionarioTF.containsKey(palabra)){
					int resultado = (int) Math.pow((diccionario.get(palabra).get(doc)), 2);
					diccionarioTF.get(palabra).put(doc, resultado);
				}else{
					Map<Integer, Integer> aux = new HashMap<Integer, Integer>();
					int resultado = (int) Math.pow((diccionario.get(palabra).get(doc)), 2);
					aux.put(doc, resultado);
					diccionarioTF.put(palabra, aux);
				}
			}
 		}

 		return diccionarioTF;
 	}
 	
 	/* Metodo auxiliar que multiplica por el valor de idf y lo eleva al cuadrado */
 	public static Map<String, Map<Integer, Double>> elevarCuadradoIDF(Map<String, Map<Integer, Integer>> diccionario, Map<String, Double> valoresIDF){
 		 		
		Map<String, Map<Integer, Double>> diccionarioIDF = new HashMap<String, Map<Integer, Double>>();
		
		for (Entry<String, Map<Integer, Integer>> entry : diccionario.entrySet()){
			String palabra = entry.getKey();
			for (Entry<Integer, Integer> entry2 : entry.getValue().entrySet()){
				Integer doc = entry2.getKey();
				if(diccionarioIDF.containsKey(palabra)){
					Double resultado = Math.pow(entry2.getValue() * valoresIDF.get(palabra), 2);
					diccionarioIDF.get(palabra).put(doc, resultado);

					
				}else{
					
					Map<Integer, Double> aux = new HashMap<Integer, Double>();
					Double resultado = Math.pow((entry2.getValue() * valoresIDF.get(palabra)), 2);
					aux.put(doc, resultado);
					diccionarioIDF.put(palabra, aux);
				}
			}
		}
 		return diccionarioIDF;
 	}
 	
}