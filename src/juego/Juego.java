package juego;
import java.awt.Color;
import java.util.Random;
import entorno.Entorno;
import entorno.InterfaceJuego;
import java.awt.Image;
import entorno.Herramientas;

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
	private static final int MENU = 0;
	private static final int JUGANDO = 1;
	private static final int BOSS = 2;
	private static final int GAME_OVER= 3;
	
	private int estado;
	
	private Isla[] isla;
	private int camaraX; // variable para mover la camara conforme al personaje. 
	private Enemigos[] enemigos;
	private Boss boss;
	private Princesa princesa;
	private Castillo castillo;
	private Proyectil proyectil;
	private Image fondo;
	private Image fondoBoss;
	private Image fondoGO;
	
	
	// para la generacion de enemigos
	private int tiempoCreacionEnemigos; 
	private int frecuenciaAparicion = 100;
	private boolean turnoEnemigoArriba = true;
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
		
		this.fondo = Herramientas.cargarImagen("imagenes/fondo.jpg");
		this.fondo = this.fondo.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
		
		this.fondoBoss = Herramientas.cargarImagen("imagenes/fondo-boss.jpg");
		this.fondoBoss = this.fondoBoss.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
		
		this.fondoGO = Herramientas.cargarImagen("imagenes/fondoGO.jpg");
		this.fondoGO = this.fondoGO.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
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
		this.enemigos = new Enemigos[4];

		//princesa
		this.princesa =new Princesa(400,300);
		
		//castillo
		this.castillo = new Castillo(3870,365,300,350);
		
		//boss
		this.boss = new Boss(600,400);
		
		this.estado = MENU;
		
		// Inicia el juego!
		this.entorno.iniciar();
	}


	public void tick()
	{
		// Procesamiento de un instante de tiempo
		// ...
		
		switch(estado) {
			case MENU:
				pantallaInicio();
				break;
				
			case JUGANDO:
				jugar();
				break;
				
			case BOSS:
				pantallaBoss();
				break;
				
			case GAME_OVER:
				pantallaGameOver();
				break;
		}
	}
	
	//METODOS AUXILIARES
	private void pantallaInicio() {
		entorno.dibujarImagen(fondo, 400, 300, 0);
		
		entorno.cambiarFont("Arial",40, Color.CYAN);
		entorno.escribirTexto("SUPER ELIZABETH", 140,220);
		
		entorno.cambiarFont("Arial",20, Color.WHITE);
		entorno.escribirTexto("Presiona ENTER para comenzar", 220,320);
		
		if(entorno.estaPresionada(entorno.TECLA_ENTER)) {
			estado = JUGANDO;
		}
	}
	
	private void jugar() {
		entorno.dibujarImagen(fondo, 400, 300, 0);
		for(int i=0;i<isla.length;i++) {
			isla[i].dibujarIsla(entorno,camaraX);
		}
		
		//con esto hacemos que el enemigo tenga un tiempo para espaunear 
		this.tiempoCreacionEnemigos++; 
		if (this.tiempoCreacionEnemigos >= this.frecuenciaAparicion) {
			this.tiempoCreacionEnemigos = 0; 
			
			for (int i = 0; i < enemigos.length; i++) {
				if (this.enemigos[i] == null) {
					//creacion arriba y abajo estan en enemigos
					if(this.turnoEnemigoArriba) {
						this.enemigos[i] = Enemigos.crearArriba(entorno, camaraX);
					}else {
						this.enemigos[i] = Enemigos.crearAbajo(entorno, camaraX);
					}
					// creamos el enemigo arriba si es true o abajo si es false
					this.turnoEnemigoArriba = !this.turnoEnemigoArriba;
					
					break; // cortamos el bucle para crear por cantidad predeterminada
					}
				}
			}
		
		//MOVIMIENTO ENEMIGOS
		for (int i = 0; i < enemigos.length; i++) {
			if (this.enemigos[i] != null) {
				this.enemigos[i].actualizar(this.isla);
				
				if (this.enemigos[i].colisionConPrincesa(this.princesa)) {
					this.princesa.perderVida();
					if (!this.princesa.estaViva()) {
						estado = GAME_OVER;
//				        this.princesa = new Princesa(400, 300);
//				        this.camaraX = 0;
				    }

				    this.enemigos[i] = null;
				    continue;
				}
				
				// si el enemigo quedo muy atrás a (osea a la izquierda) lo volvemos null
				if (this.enemigos[i].SalioPantalla(entorno, camaraX)) {
					this.enemigos[i] = null;
				} else {
					this.enemigos[i].dibujar(entorno, camaraX);
				}
			}
		}
		//DISPARO
		if (this.entorno.sePresionoBoton(this.entorno.BOTON_IZQUIERDO)) {
		    if (this.proyectil == null) { // solo dispara si no hay uno en vuelo
		        this.proyectil = new Proyectil(
		            this.princesa.getX(),
		            this.princesa.getY(),
		            this.entorno.mouseX(),
		            this.entorno.mouseY(),
		            this.camaraX
		        );
		    }
		}

		if (this.proyectil != null) {
		    
		    this.proyectil.actualizar();
		    
		    // primero chequeamos colision con enemigos
		    for (int i = 0; i < this.enemigos.length; i++) {
		        if (this.enemigos[i] != null) {
		            if (this.proyectil.colisionaCon(this.enemigos[i])) {
		                this.enemigos[i] = null;
		                this.proyectil = null;
		                break;
		            }
		        }
		    }
		    
		    // despues de chequear colisiones, si sigue vivo lo movemos y dibujamos
		    if (this.proyectil != null) {
		        if (this.proyectil.salio(entorno, camaraX)) {
		            this.proyectil = null;
		        } else {
		            this.proyectil.dibujar(entorno, camaraX);
		        }
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
		        // game over
		    	estado = GAME_OVER;
//		        this.princesa = new Princesa(400, 300);
//		        this.camaraX = 0;
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

		castillo.dibujarCastillo(entorno, camaraX);
		
		princesa.dibujarPrincesa(entorno, camaraX);
		princesa.dibujarVidas(entorno);	
		
		
		if(this.castillo.colisionConPrincesa(princesa)) {
			estado=BOSS;
		}
	}
	
	private void pantallaBoss() {
		entorno.dibujarImagen(fondoBoss, 400, 300, 0);
		boss.dibujar(entorno);
		
		entorno.cambiarFont("Arial", 50, Color.RED);
		entorno.escribirTexto("BOSS FINAL", 250, 150);

		entorno.cambiarFont("Arial", 20, Color.WHITE);
		entorno.escribirTexto("Aqui ira la pelea final", 250, 250);
	}
	
	private void pantallaGameOver() {
		entorno.dibujarImagen(fondoGO, 400, 300, 0);

		entorno.cambiarFont("Arial",30, Color.WHITE);
		entorno.escribirTexto("Presiona ENTER para volver a jugar", 170,500);
		
		if(entorno.sePresiono(entorno.TECLA_ENTER)) {
			
			reiniciarJuego();
			estado = JUGANDO;
		}
		
	}
	
	private void reiniciarJuego() {
		this.princesa = new Princesa(400,300);
		this.camaraX = 0;
		this.enemigos = new Enemigos[4];
		this.proyectil = null;
		this.tiempoCreacionEnemigos = 0;
		this.turnoEnemigoArriba = true;
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
