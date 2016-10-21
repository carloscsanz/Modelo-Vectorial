package modeloVectorial;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;

public class Documentos {
	
	private static ArrayList<String> documentos = new ArrayList<String>();
	
	/*
	 * Metodo que devuelve el texto limpio en un String de un Documento dado
	 */
	public String abrirFichero(String documento){
		File input = new File(documento);
		String texto = null;
		try {
			texto = Jsoup.parse(input, "UTF-8").text();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return texto;
	}
	

	/*
	 * Metodo que se encarga de mapear los documentos con un unico numero identificador
	 * Suponiendo que todos los documentos tienen distinto nombre
	 */
	public Map<Integer, String> importarDocumentos(String ruta){
		Map<Integer, String> hasDocumentos = new HashMap<Integer, String>();
		
		listarDocumentos(ruta);
		
		for(int i=0 ; i<documentos.size() ; i++){
			hasDocumentos.put(i, documentos.get(i));
		}
		
		return hasDocumentos;
	}
	
	/* 
	 * Metodo que recorre todos los directorios contenidos dentro de una carpeta 
	 * guardando en el ArrayList documentos todos los documentos con formato .html
	 * 
	 */
	public void listarDocumentos(String ruta){
		
		File carpeta = new File(ruta);
		File[] ficheros = carpeta.listFiles();
		
		for( int i=0 ; i < ficheros.length ; i++ ){
			if(ficheros[i].isDirectory()){
				listarDocumentos(ruta+ficheros[i].getName()+"/");
			}else{
				
				/* Solo añadimos los documentos que tengan extension html */
				String extension = getExtension(ficheros[i].getName());
				
				if(extension.toLowerCase().equals("html")){
					documentos.add(ruta+ficheros[i].getName());
				}
			}
		}
	}
	
	/* 
	 * Metodo que obtiene la extension de un fichero dado
	 * 
	 */
	public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        
        int extensionPos = filename.lastIndexOf('.');
        int lastUnixPos = filename.lastIndexOf('/');
        int lastWindowsPos = filename.lastIndexOf('\\');
        int lastSeparator = Math.max(lastUnixPos, lastWindowsPos);

        int index = lastSeparator > extensionPos ? -1 : extensionPos;
        
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }

}
