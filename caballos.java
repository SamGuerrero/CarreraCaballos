
import java.awt.TextArea;
import javax.swing.*;

//Me crea ventanas
class ventanas{
	private JFrame ventana = new JFrame();
	private TextArea texto_ventana = new TextArea();
	
	ventanas(String nombre, int x, int y){
		ventana.setName(nombre);
		ventana.setTitle(nombre);
		ventana.setSize(400, 400);
		ventana.setVisible(true);
		ventana.add(texto_ventana);
		ventana.setLocation(x, y);
	}
	
	public void escribecadena(String cadena){
		texto_ventana.setText(texto_ventana.getText() + cadena);
	}
	
}

//Guardo los resultados de cada carrera
class Resultados{
	int[][] carrera;
	int[] carreras;
	String[][] cadena;
	
	Resultados(int numCorredores, int numCarrera) {
		this.carrera = new int[numCarrera][numCorredores];
		this.carreras = new int[numCarrera];
		this.cadena = new String[numCarrera] [numCorredores];
	}
	
	void getResultados() {
		
		for(int t = 0; t < carreras.length; t++) {
			for(int i = 0; i < carrera.length; i++) {
				this.cadena[t][i] = "Carrera " + this.carreras[t] + ": El corredor " + (i+1) + " ha tardado: " + this.carrera[t][i] + " sprints\n";
			}
		}
	}
}

//Lo que ejecuta el hilo de cada Carrera
class Operacionesenhilos extends Thread {
	private int numhilo;
	private ventanas ventana_hilo;
	private final int meta = 50;
	private Resultados res;
	private int numCarrera;
	
	Operacionesenhilos(int i, Resultados res, int numCarrera){
		this.numhilo = i;
		ventana_hilo = new ventanas("Corredor " + numhilo, i*300, i);
		this.res = res;
		this.numCarrera = numCarrera-1;
	}
	
	public void run(){
		String cadena = new String();
		int metros = 1;
		int i = 1;
		while (metros < meta){
			cadena="Soy el Corredor " + numhilo + " es mi sprint " + i +  " y he recorrido " + metros + " metros\n"; //Guardo cadena
			ventana_hilo.escribecadena(cadena); //Imprimo cadena en ventana
			try{
				sleep(1000);
			}catch(InterruptedException e) {
				System.out.println("Hilo Interrumpido.");
			}
			
			i++; //Sumo sprint
			metros = metros + (int) (Math.random() * 20); //Sumo metros aleatorios
		}
		res.carrera[numCarrera][numhilo-1]=i;
		
		System.out.println("Soy el Corredor "+ numhilo +" he FINALIZADO"); //Imprimo en consola
		cadena="Soy el Corredor" + numhilo + " he FINALIZADO"; //Guardo cadena
		ventana_hilo.escribecadena(cadena); //Imprimo cadena
		
	}
	
}

class OperacionHilos extends Thread {
	private Resultados res;
	private int numCorredores;
	private int numCarrera;
	
	OperacionHilos(int i, Resultados res, int numCorredores){
		this.numCarrera = i;
		this.res = res;
		this.numCorredores = numCorredores;
	}
	
	public void run(){
		res.carreras[numCarrera-1] = numCarrera;
		Operacionesenhilos[] corredor = new Operacionesenhilos[numCorredores];
		for (int i = 0; i < numCorredores; i++) {
			corredor[i] = new Operacionesenhilos (i+1, res, numCarrera); //A cada hilo le paso su contador, el almacén y el número de carrera en el que se encuentra
			corredor[i].start(); //Empiezo el hilo
		}
		
		try{
			for (int i = 0; i < numCorredores; i++) {
				corredor[i].join(); //Espero a los hilos
			}
			
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		
	}
	
}

public class caballos {
	static int numCarreras = 4; //Cuántos corredores voy a tener
	private static OperacionHilos corredor[]; //Array de carreras
	private static int numCorredores;
	public static void main(String[] args) {
		numCorredores = 4;
		
		Resultados res = new Resultados(numCorredores, numCarreras);
		corredor = new OperacionHilos[numCarreras];
		
		//Realizo varias carreras una a continuación de la otra
		for (int i = 0; i < numCarreras; i++) {
			corredor[i] = new OperacionHilos (i+1, res, numCarreras); //A cada hilo le paso su contador y el almacén
			corredor[i].start(); //Empiezo el hilo
			
			System.out.println("La Carrera " + (i+1) +" ha comenzado... Hagan sus apuestas...");
			try {
				corredor[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	
		//Muestra los resultados de cada carrera, a ver qué corredor tarda menos sprints en llegar a la meta
		System.out.println("Las carreras han finalizado... Han llegado los " + numCarreras + " corredores");
		res.getResultados();
		for (int t = 0; t < numCarreras; t++) {
			for (int i = 0; i < numCorredores; i++)
				System.out.print(res.cadena[t][i]);
			
			System.out.println("--------------------------------------------------------------------------------");
		}
		
	}
}
