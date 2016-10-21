package modeloVectorial;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class Limpieza {
	
	public StringTokenizer filtro(String dato){
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		String [] palabras = dato.split(" ");
		
		List<String> myList = Arrays.asList(palabras);
		
		
		List<String> filtered = myList
			.stream()
			.filter(p -> p.length() < 15)
			.collect(Collectors.toList());
		
		
		System.out.println("Acaba limpia: " + filtered.size());
		
		
		
		StringTokenizer st = new StringTokenizer(filtered.toString(), ".,;:·¨|¦*+—^‘’°ºª�©@®#€$¢&%∞¬÷-_“”≠=´/()[]{}|<>?¿'!¡ \t\n\\\"\u00a0");
		
		
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		System.out.println("Acaba st: " + st.countTokens());
		
		return st;
	}

}
