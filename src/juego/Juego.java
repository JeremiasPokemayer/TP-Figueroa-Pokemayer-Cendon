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
	// el objeto entorno que controla el tiempo y otros
	private Entorno entorno;
	
	// variables y metodos propios de cada grupo
	
	private Isla[] isla;
	private int camaraX; // variable para mover la camara conforme al personaje. 
	private Enemigos[] enemigos;
	private Princesa princesa;
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
		// Inicializar lo que haga falta para el juego
		this.isla = new Isla[10];
		
		this.isla[0]= new Isla(200,565,400,100);
		this.isla[1]= new Isla(650,565,300,100);
		this.isla[2]= new Isla(1200,565,500,100); 	// islas base
		this.isla[3]= new Isla(400,355,400,40);		// primer isla
		for(int i = 4;i<this.isla.length;i++) {
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
		
		
		//enemigos
		this.enemigos = new Enemigos[2];
		this.enemigos[0] = new Enemigos(entorno, camaraX);
		this.enemigos[1] = new Enemigos(entorno,camaraX);
		//para darle distancia al otro enemigo
		this.enemigos[1].setX(this.enemigos[1].getX() + 300);
		
		//princesa
		this.princesa=new Princesa(400,300);
		
		
		// Inicia el juego!
		this.entorno.iniciar();
	}


	public void tick()
	{
		// Procesamiento de un instante de tiempo
		// ...
		for(int i=0;i<isla.length;i++) {
			isla[i].dibujarIsla(entorno,camaraX);
		}
		
		//MOVIMIENTO ENEMIGOS
		for (int i = 0; i < enemigos.length; i++) {
	        // importante que el casillero no este null
	        if (this.enemigos[i] != null) {
	            //movemos al enemigo y chequeamos si choca con
	            this.enemigos[i].actualizar(this.isla);
	            //vemos si salio de camcarax
	            if (this.enemigos[i].SalioPantalla(entorno, camaraX)) {
	                this.enemigos[i] = null;
	            } else {
	                this.enemigos[i].dibujar(entorno, camaraX);
	            }
	        }
	    }
		
		//MOVIMIENTO PRINCESA
		if(this.entorno.estaPresionada(this.entorno.TECLA_DERECHA)) {
			princesa.moverDerecha();
		};
		
		if(this.entorno.estaPresionada(this.entorno.TECLA_IZQUIERDA)) {
			princesa.moverIzquierda();
		}
		
		this.camaraX = (int)(this.princesa.getX() - 400);
		if(this.camaraX < 0) this.camaraX = 0; //la princesa no puede retroceder antes del inicio del mapa
		
		princesa.actualizarFisica();
		
		boolean tocandoSuelo = false;
		
		for(int i = 0; i<isla.length;i++) {
			if(princesa.colisionaCon(isla[i])) {
				tocandoSuelo = true;
			}
		}
		
		if(!tocandoSuelo) {
			princesa.despegar();
		}
		
		if(this.entorno.estaPresionada(this.entorno.TECLA_ARRIBA)) {
			princesa.saltar();
		};
		
		princesa.dibujarPrincesa(entorno, camaraX);
		princesa.dibujarVidas(entorno);
		
	}
	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
