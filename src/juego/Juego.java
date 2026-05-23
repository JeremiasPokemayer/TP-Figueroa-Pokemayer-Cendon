package juego;

import java.util.Random;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	public static class Aleatorio {
		private static Random random= new Random();
		
		public static int numAleatX() {
			return(random.nextInt(3000));
		}
	
		public static int numAleatY() {
			return (random.nextInt(350)+200);
		}
		
		public static int numAleatAncho() {
			return (random.nextInt(490)+250);
		}
	}
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	
	// Variables y métodos propios de cada grupo
	// ...
	private Isla[] isla;
	private int camaraX; // variable para mover la camara conforme al personaje. 
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
		// Inicializar lo que haga falta para el juego
		this.isla = new Isla[10];
		
		this.isla[0]= new Isla(200,565,400,100);
		this.isla[1]= new Isla(650,565,300,100);
		this.isla[2]= new Isla(1200,565,500,100); 	// islas base
		for(int i = 3;i<this.isla.length;i++) {
			Isla nueva = new Isla(Aleatorio.numAleatX(),Aleatorio.numAleatY(),Aleatorio.numAleatAncho(),40); //islas flotantes
			boolean colisiona=true;
			while(colisiona) {
				colisiona=false;
				for(int j = 0; j < i;j++) {
					if(nueva.alejaDe(this.isla[j])) { //chequea que las islas no colisionen, si colisionan crea una nueva
						colisiona=true;
						nueva= new Isla(Aleatorio.numAleatX(),Aleatorio.numAleatY(),Aleatorio.numAleatAncho(),40);
					}
				}
			}
			this.isla[i]=nueva;
		}
		// Inicia el juego!
		this.entorno.iniciar();
	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y 
	 * por lo tanto es el método más importante de esta clase. Aquí se debe 
	 * actualizar el estado interno del juego para simular el paso del tiempo 
	 * (ver el enunciado del TP para mayor detalle).
	 */
	public void tick()
	{
		// Procesamiento de un instante de tiempo
		// ...
		for(int i=0;i<isla.length;i++) {
			isla[i].dibujarIsla(entorno,camaraX);
		}
	}
	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
