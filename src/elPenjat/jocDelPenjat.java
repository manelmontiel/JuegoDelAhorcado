package elPenjat;

import java.io.*;
import java.util.Scanner;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.json.JSONObject;		
		

public class jocDelPenjat {
	
	private final static String palabras = "paraules.json";
	private final static String partidas = "partides.json";

	public static void main(String[] args) throws InterruptedException, JSONException, IOException {
			jocDelPenjat programa = new jocDelPenjat();
			programa.inici();
	}

	
	String nombreUsuario = "";
	JSONArray json;
	
	
	String estado;
		
	private void inici() throws JSONException, IOException  {
		Scanner teclat = new Scanner(System.in);
		int opcion;
		
		opcion = menuOpcion();
		
		while(opcion != 0) {
			switch(opcion) {
			case 1:
				System.out.println("Escriba su nick de jugador");
				nombreUsuario = teclat.next();
				char respuesta;
				
				do {
				JSONArray json1 = leerPalabras(); //Leemos el archivo paraules.json gracias al metodo leerPalabras()  i lo guardamos en la array json1
				JSONObject objRandom = new JSONObject();
				
				objRandom = (JSONObject) json1.get((int) (Math.random() * json1.length())); //para coger una palabra al azar de paraules.json (json1) para jugar
						
				String palabra = objRandom.get("PARAULA").toString();	//para coger una palabra al azar de paraules.json (json1) para jugar
				
					final int ERRORES_MAXIMOS = 8;
					int intentos = 0;			//intentos es igual a errores
					int aciertos = 0;
					int mostrarAciertos = 0;	//para mostrar los aciertos
					
			        char[] desguazada = desguaza(palabra); //desguazada utiliza el metodo desguaza para descomponer la palabra en sus letras
			        char[] copia = desguaza(palabra); // Algo auxiliar para mas tarde
			        
			        char[] tusRespuestas = new char[desguazada.length];
			 
			        // Rellenamos palabras con guiones
			        for(int i = 0; i < tusRespuestas.length; i++){ 		//cambia las letras que aun no he acertado por un _
			            tusRespuestas[i] = '_';
			        }
			        
			        System.out.println("Adivina la palabra!");
				
				
					while(intentos < ERRORES_MAXIMOS && aciertos != palabra.length()){  //mentres intentos sigui mes petit que errores maximos i aciertos sigui diferent a la paraula a adivinar seguira jugant
			            imprimeOculta(tusRespuestas);
			            
			            // Preguntamos para que introduzca una letra
			            System.out.println("\nIntroduce una letra: ");
			            respuesta = teclat.next().toUpperCase().charAt(0);		//per a que tots els caracters introduits es transformin en mayuscula
			            boolean error = false;
			            
			            // Recorremos el array y comprobamos si se ha producido un acierto
			            for(int i = 0; i < desguazada.length; i++){
			                if(desguazada[i]==respuesta){
			                    tusRespuestas[i] = respuesta; 
			                    error = true;
			                    aciertos++;
			                }
			            }    
			            
			            if (error == false) {
			            	intentos++;
			            }else {
			            	mostrarAciertos++;
			            }
			            
			            System.out.println("Llevas " + mostrarAciertos + " aciertos y " + intentos + " errores.");
			        }
					// Si hemos acertado todas imprimimos un mensaje de felicitación
			        if(aciertos == tusRespuestas.length){ 
			            System.out.print("\nFelicidades!! Has acertado la palabra: ");
			            imprimeOculta(tusRespuestas); 
			            estado = "GANADA";
			        }
			        // Si no se acierta y se llega al límite de fallos imprimimos un mensaje correspondiento mostrando la palabra
			        else{
			            System.out.print("\nLo sentimos, la palabra era: ");
			            for(int i = 0; i < copia.length; i++){
			                System.out.print(copia[i] + " ");			                
			            }
			            estado = "PERDIDA";
			        }
			        // Reseteamos contadores
			        intentos = 0;
			        aciertos = 0;
			        
			        respuesta = pregunta("\n\nQuieres volver a jugar?",teclat);
		        }while(respuesta != 'n');
				
				Calendar calendario = Calendar.getInstance();	
				int hora, minuts, segons, dia, mes, any;
				String fecha;
				
				dia = calendario.get(Calendar.DAY_OF_MONTH);
				mes = calendario.get(Calendar.WEEK_OF_MONTH);
				any = calendario.get(Calendar.YEAR);
				hora = calendario.get(Calendar.HOUR_OF_DAY);
				minuts = calendario.get(Calendar.MINUTE);
				segons = calendario.get(Calendar.SECOND);
				
				fecha = dia + "-" + mes + "-" + any + " " + hora + ":" + minuts + ":" + segons; //perque estigui en aquest format
				
				JSONArray escribir = leerPartidas();
				JSONObject obj2 = new JSONObject(escribir);
				
				obj2.put("JUGADOR", nombreUsuario);
				obj2.put("MOMENT", fecha);
				obj2.put("RESULTAT", estado);
				
				escribir.put(obj2);
				
				FileWriter escriure = new FileWriter(partidas);
				escriure.write(escribir.toString());
				escriure.flush();								//para que escriba en partides.json el obj2 (jugador, moment, resultat)
				break;
			case 2:
				//metodo mostrar
				System.out.println("Resultados Generales");
				
				JSONArray json2 = leerPartidas(); //Leemos el archivo partides.json gracias al metodo leerPartidas() i lo guardamos en la array json2
				
				for(int a = 0; a < json2.length(); a++) {
					JSONObject obj1 = new JSONObject();
					obj1 = (JSONObject) json2.get(a);
					
					String jugador = obj1.get("JUGADOR").toString();
					String moment = obj1.get("MOMENT").toString();
					String resultat = obj1.get("RESULTAT").toString();
					
					System.out.println(jugador + " " + moment + " " + resultat);
				}
				opcion = pregunta("\n\nQuieres continuar?", teclat);
				break;
				
			case 3:
				//metodo borrar
				System.out.println("El registro de resultados ha sido borrado.");
				
				JSONArray json3 = leerPartidas();	//Leemos el archivo partides.json gracias al metodo leerPartidas() i lo guardamos en la array json3
				
				for(int a = json3.length() - 1;a >= 0;a--) {		//borramos el contenido de json3
					json3.remove(a);
				}
				FileWriter borrar = new FileWriter(partidas);		//le decimos que reescriba partides.json con json3, o sea, lo dejarà en blanco
				borrar.write(json3.toString());
				borrar.flush();
				break;
			}
		opcion = menuOpcion();
		}
		if (opcion == 0) {		//si escoge salir mostramos un mensaje final
			System.out.println("Gracias por jugar.");
		}
	}
	
	private static int menuOpcion() {
        // mostrem el menu en pantalla
		System.out.println("* * * * * * * * * * * * * *");
		System.out.println("*                         *");
		System.out.println("*   Elige una opción:     *");
		System.out.println("*   0: Salir              *");
		System.out.println("*   1: Jugar              *");
		System.out.println("*   2: Mostrar            *");
		System.out.println("*   3: Borrar             *");
		System.out.println("*                         *");
		System.out.println("* * * * * * * * * * * * * * ");

        cLector cl = new cLector();
        return cl.llegirEnter(" ");
    }
		
	private static char[] desguaza(String palAzar){		//descompone las palabras en las letras
        char[] letras;
        letras = new char[palAzar.length()];
        for(int i = 0; i < palAzar.length(); i++){
            letras[i] = palAzar.charAt(i);
        }
        return letras;
    }
    
    private static void imprimeOculta(char[] tusRespuestas){	//a medida que se vaya acertando las letras, ensenya esas letras cambiando las por las barras que hay en su lugar
        for(int i = 0; i < tusRespuestas.length; i++){			// recorrer la taula que li estic mostrant al usuari per a que tot el rato es mostri, al principi son totes 
            System.out.print(tusRespuestas[i] + " ");			// i es van canviant per les lletres que siguin correctes
        }	
    }
    
    public static char pregunta(String men, Scanner teclat) {	//pregunta si seguir en partida o no
        char resp;
        System.out.println(men + " (s/n)");
        resp = teclat.next().toLowerCase().charAt(0);		//toLowerCase convierte todo caracter del string en minuscula
        while (resp != 's' && resp != 'n') {
            System.out.println("Error! solo se admite S o N");
            resp = teclat.next().toLowerCase().charAt(0);
        }
        return resp;
    }

    private JSONArray leerPalabras() throws IOException, JSONException { 	//lee el archivo paraules.json
		
		FileReader lectorArchivos = new FileReader(palabras);
		BufferedReader lector = new BufferedReader(lectorArchivos);
		
		String s = "";									//lee el string i lo pone (suma) en un sitring enorme(jsonString)
		String jsonString = "";
		

		// Llegim totes les linies del fitxer
		while((s = lector.readLine()) != null) {
			jsonString = jsonString + s;
		}
		
		JSONArray leerPalabras = new JSONArray(jsonString);		//guardamos jsonString en un JSONArray i hacemos que retorne este JSONArray
		lector.close();
		
		return leerPalabras;

    }
	
    private static JSONArray leerPartidas() throws IOException, JSONException { 	//lee el archivo partides.json
		
		FileReader lectorArchivos = new FileReader(partidas);
		BufferedReader lector = new BufferedReader(lectorArchivos);
		
		String s = "";									//lee el string i lo pone (suma) en un sitring enorme(jsonString)
		String jsonString = "";
		
		// Llegim totes les linies del fitxer
		while((s = lector.readLine()) != null) {
			jsonString = jsonString + s;
		}
		
		JSONArray leerPartidas = new JSONArray(jsonString);			//guardamos jsonString en un JSONArray i hacemos que retorne este JSONArray
		lector.close();
		
		return leerPartidas;

    }
}



