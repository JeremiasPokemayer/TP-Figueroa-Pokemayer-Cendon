package juego;
import java.awt.Color;
import java.util.Random;
import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	public static class Aleatorio {
		private static Random random= new Random();
		
		public static int numAleatX() {
			return(random.nextInt(3300));
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
	private Castillo castillo;
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
		// Inicializar lo que haga falta para el juego
		this.isla = new Isla[15];
		
		this.isla[0]= new Isla(200,565,400,100);	// islas base
		this.isla[1]= new Isla(650,565,300,100);
		this.isla[2]= new Isla(1200,565,500,100);
		this.isla[3]= new Isla(1700,565,300,100);
		this.isla[4]= new Isla(2100,565,300,100);
		this.isla[5]= new Isla(2550,565,400,100);
		this.isla[6]= new Isla(3000,565,200,100);
		this.isla[7]= new Isla(3650,565,900,100);	//isla base final de castillo
		this.isla[8]= new Isla(400,355,400,40);		// primer isla
		for(int i = 9;i<this.isla.length;i++) {
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
		this.enemigos[0] = new Enemigos(1100, 460);
		this.enemigos[1] = new Enemigos(900, 460);
		
		//princesa
		this.princesa =new Princesa(400,300);
		
		//castillo
		this.castillo = new Castillo(3870,365,200,300);
		
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
	        if (this.enemigos[i] != null) {
	            //para que se mueva el npc pixel por pixel en isla
	        	this.enemigos[i].actualizar(this.isla);
	            // chequeamos si el enemigo toco a la princesa
	            if (this.enemigos[i].colisionConPrincesa(this.princesa)) {
	            	this.princesa.perderVida();
	            	this.enemigos[i] = null;
	            	continue;
	            }
	            	//esto dibuja a los enemigos siempre
	            this.enemigos[i].dibujar(entorno, camaraX);
	        }
	    }
		
		//MOVIMIENTO PRINCESA
		if(this.entorno.estaPresionada(this.entorno.TECLA_DERECHA)) {
			princesa.moverDerecha();
		};
		
		if(this.entorno.estaPresionada(this.entorno.TECLA_IZQUIERDA)) {
		    princesa.moverIzquierda(this.camaraX);
		}

		this.camaraX = (int)(this.princesa.getX() - 400);
		int limiteMapa = 4000;
		
		if(this.camaraX > limiteMapa - this.entorno.ancho()){	//limite derecho del mapa
			this.camaraX = limiteMapa - this.entorno.ancho();	
		}
		
		if(this.camaraX < 0) {
		    this.camaraX = 0;
		    // forzamos que la princesa no pase del borde visible
		    if(this.princesa.getX() - Princesa.ANCHO / 2 < 0) {
		    	this.princesa.setX((double)(Princesa.ANCHO / 2));
		    }
		}
		
		if(this.princesa.getX() > limiteMapa - Princesa.ANCHO / 2 ) {		//la princesa no pasa del limite derecho
			this.princesa.setX((double)(limiteMapa - Princesa.ANCHO / 2));
		}
		
		princesa.actualizarFisica();
		
		if (this.princesa.getY() > this.entorno.alto()) {
		    this.princesa.perderVida();
		    if (!this.princesa.estaViva()) {
		        // reinicia todo el juego
		        this.princesa = new Princesa(400, 300);
		        this.camaraX = 0;
		    } else {
		        // solo reinicia posicion
		        this.princesa.reiniciar();
		        this.camaraX = 0;
		    }
		}
		
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
		
		castillo.dibujarCastillo(entorno, camaraX);
		
		if(this.castillo.colisionConPrincesa(princesa)) {
			entorno.cambiarFont("Arial", 50, Color.CYAN);
			entorno.escribirTexto("GANASTE", 350, 300);
		}
		}
	
	
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
