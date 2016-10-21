package modeloVectorial;

import java.util.Map;
import java.util.StringTokenizer;
import java.text.DecimalFormat;

public class Modelos {

	public static final String rutaDocumentos = "./Documentos/"; //Ruta en la que se encuentran guardados los documentos .html
	public static Map<Integer, String> hashDocumentos;
	public static final String [] consultas = {"What video game won Spike's best driving game award in 2006?", "What is the default combination of Kensington cables?", "Who won the first ACM Gerard Salton prize?"};
	public static String [] resultado;
	
	public static void main(String[] args) {
		
		/* Obtenemos los documentos y el hashMap de ellos para poder identificarlos por un numero unico */
		Documentos doc = new Documentos();
		hashDocumentos = doc.importarDocumentos(rutaDocumentos);
		
		/* Añadimos los terminos de cada uno de los documentos al diccionario */
		Diccionario diccionario = new Diccionario();
		Limpieza limpiezaTexto = new Limpieza();
//		if(!hashDocumentos.isEmpty()){
//			for(int i=0 ; i<hashDocumentos.size() ; i++){
				System.out.println("documentos: " + hashDocumentos.size());
				
				//OBTENGO LOS DATOS DEL DOCUMENTO
				String texto = doc.abrirFichero(hashDocumentos.get(0));
				
				//LIMPIO LOS DATOS DEL DOCUMENTO
				StringTokenizer textoLimpio = limpiezaTexto.filtro(texto);
				
				//System.out.println("Tokens: " + textoLimpio.countTokens());
				
				//AÑADO LOS DATOS AL DICCIONARIO
				diccionario.crearDiccionario(textoLimpio, 0);
				//System.out.println(diccionario.Diccionario.toString());;
//			}
//		}
		
//		Calculos a = new Calculos();
//		
//		crearResultado();
//		
//		for(int i=0 ; i<consultas.length ; i++){
//			
//			Map<String, Integer> consulta = diccionario.diccionarioConsulta(limpiezaTexto.filtro(consultas[i]));
//			
//			Map<String, Double> idf = a.calcularIDF(hashDocumentos.size(), Diccionario.Diccionario, consulta);
//			
//			Map<Integer, Double> tf = a.ProductoEscalarTF(hashDocumentos.size(), Diccionario.Diccionario, consulta);
//			
//			Map<Integer, Double> tfidf = a.ProductoEscalarTFIDF(hashDocumentos.size(), Diccionario.Diccionario, consulta, idf);
//			
//			Map<Integer, Double> costf = a.CosenoTF(Diccionario.Diccionario, tf, consulta);
//			
//			Map<Integer, Double> costfidf = a.CosenoTFIDF(Diccionario.Diccionario, tfidf, idf, consulta);
//			
//			addResultado(tf, tfidf, costf, costfidf);
//			
//		}
		//imprimirResultados();

		
	}
	
	public static void crearResultado(){
		
		resultado = new String[hashDocumentos.size() * 4];
		
		for(int i=0 ; i<hashDocumentos.size() ; i++){
			String [] nombre = hashDocumentos.get(i).split("/");
			String nombreSinExtension = nombre[nombre.length - 1].replace(".html", "");
			resultado[i] = nombreSinExtension;
			resultado[i + hashDocumentos.size()] = nombreSinExtension;
			resultado[i + 2 * hashDocumentos.size()] = nombreSinExtension;
			resultado[i + 3 * hashDocumentos.size()] = nombreSinExtension;
		}

	}
	
	public static void addResultado(Map<Integer, Double> tf, Map<Integer, Double> tfidf, Map<Integer, Double> costf, Map<Integer, Double> costfidf){
		
		DecimalFormat formateador = new DecimalFormat("#0.000000");
		
		for(int i=0 ; i<hashDocumentos.size() ; i++){
			//resultado[i] += "\t" + DecimalFormat("#0.00000").format(tf.get(i));
			resultado[i] += "\t" + formateador.format(tf.get(i));
			resultado[i + hashDocumentos.size()] += "\t" + formateador.format(tfidf.get(i));
			resultado[i + 2 * hashDocumentos.size()] += "\t" + formateador.format(costf.get(i));
			resultado[i + 3 * hashDocumentos.size()] += "\t" + formateador.format(costfidf.get(i));
		}
	}
	
	public static void imprimirResultados(){
		System.out.println("* RELEVANCIA: ProductoEscalarTF");
		System.out.println("Nombre del doc \tQ1 \t\tQ2 \t\tQ3 ");
		
		for (int i=0 ; i<hashDocumentos.size() ; i++){
			System.out.println(resultado[i]);
		}
		
		System.out.println("\n* RELEVANCIA: ProductoEscalarTFIDF");
		System.out.println("Nombre del doc \tQ1 \t\tQ2 \t\tQ3 ");
		
		for (int i=hashDocumentos.size() ; i<hashDocumentos.size()*2 ; i++){
			System.out.println(resultado[i]);
		}
		
		System.out.println("\n* RELEVANCIA: CosenoTF");
		System.out.println("Nombre del doc \tQ1 \t\tQ2 \t\tQ3 ");
		
		for (int i=hashDocumentos.size()*2 ; i<hashDocumentos.size()*3 ; i++){
			System.out.println(resultado[i]);
		}
		
		System.out.println("\n* RELEVANCIA: CosenoTFIDF");
		System.out.println("Nombre del doc \tQ1 \t\tQ2 \t\tQ3 ");
		
		for (int i=hashDocumentos.size()*3 ; i<hashDocumentos.size()*4 ; i++){
			System.out.println(resultado[i]);
		}
	}


}