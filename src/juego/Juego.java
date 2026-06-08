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
	private static final int GANASTE= 4;
	
	private int estado;
	
	private Isla[] isla;
	private Isla islaBoss;
	private int camaraX; // variable para mover la camara conforme al personaje. 
	private Enemigos[] enemigos;
	private EnemigosMejorados[] enemigosMejorados;
	private Boss boss;
	private Princesa princesa;
	private Castillo castillo;
	private Proyectil proyectil;
	private Image fondo;
	private Image fondoBoss;
	private Image fondoGO;
	private Image fondoWin;
	private Image fondoInicio;
	
	// para la generacion de enemigos
	private int tiempoCreacionEnemigos; 
	private int frecuenciaAparicion = 100;
	private boolean turnoEnemigoArriba = true;
	private int contadorEnemigos ;
	
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
		
		this.fondo = Herramientas.cargarImagen("imagenes/fondo.jpg");
		this.fondo = this.fondo.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
		
		this.fondoBoss = Herramientas.cargarImagen("imagenes/fondo-boss.jpg");
		this.fondoBoss = this.fondoBoss.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
		
		this.fondoGO = Herramientas.cargarImagen("imagenes/fondoGO.png");
		this.fondoGO = this.fondoGO.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
		
		this.fondoWin = Herramientas.cargarImagen("imagenes/win.png");
		this.fondoWin = this.fondoWin.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
		
		this.fondoInicio= Herramientas.cargarImagen("imagenes/fondo-inicio.png");
		this.fondoInicio= this.fondoInicio.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
		// Inicializar lo que haga falta para el juego
		
		//islas
		this.isla = new Isla[15];
		
		this.isla[0]= new Isla(200,565,400,100,"imagenes/pasto2.png");	// islas base
		this.isla[1]= new Isla(650,565,300,100,"imagenes/pasto2.png");
		this.isla[2]= new Isla(1200,565,500,100,"imagenes/pasto2.png");
		this.isla[3]= new Isla(1700,565,300,100,"imagenes/pasto2.png");
		this.isla[4]= new Isla(2100,565,300,100,"imagenes/pasto2.png");
		this.isla[5]= new Isla(2550,565,400,100,"imagenes/pasto2.png");
		this.isla[6]= new Isla(3000,565,200,100,"imagenes/pasto2.png");
		this.isla[7]= new Isla(3650,565,900,100,"imagenes/pasto2.png");	//isla base final de castillo
		this.isla[8]= new Isla(400,355,400,40,"imagenes/pasto2.png");		// primer isla
		for(int i = 9;i<this.isla.length;i++) {
			Isla nueva = new Isla(Aleatorio.numAleatX(),Aleatorio.numAleatY(),Aleatorio.numAleatAncho(),40,"imagenes/pasto2.png"); //islas flotantes
			boolean colisiona=true;
			while(colisiona) {
				colisiona=false;
				for(int j = 0; j < i;j++) {
					if(nueva.alejaDe(this.isla[j])) { //chequea que las islas no colisionen, si colisionan crea una nueva
						colisiona=true;
						nueva= new Isla(Aleatorio.numAleatX(),Aleatorio.numAleatY(),Aleatorio.numAleatAncho(),40,"imagenes/pasto2.png");
					}
				}
			}
			this.isla[i]=nueva;
		}

		this.islaBoss = new Isla(400,580,800,80,"imagenes/plataformaBoss2.jpg");
		
		
		//ENEMIGOS
		this.enemigos = new Enemigos[5];
		this.enemigosMejorados = new EnemigosMejorados[4];
		
		this.enemigosMejorados[0] = new EnemigosMejorados(3200, 460);
		this.enemigosMejorados[1] = new EnemigosMejorados(3200, 150);
		this.enemigosMejorados[2] = new EnemigosMejorados(3000, 150);
		this.enemigosMejorados[3] = new EnemigosMejorados(3000, 450);
		this.contadorEnemigos = 0;
		
		
		//princesa
		this.princesa =new Princesa(400,300);
		
		//castillo
		this.castillo = new Castillo(3870,365,300,350);
		
		//boss
		this.boss = new Boss(400,350);
		
		this.estado = MENU;
		
		// Inicia el juego
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
			
			case GANASTE:
				pantallaWin();
				break;
		}
	}
	
	//METODOS AUXILIARES
	private void pantallaInicio() {
		entorno.dibujarImagen(fondoInicio, 400, 300, 0);
		
		if(entorno.estaPresionada(entorno.TECLA_ENTER)) {
			estado = JUGANDO;
		}
	}
	
	private void jugar() {
		entorno.dibujarImagen(fondo, 400, 300, 0);
		for(int i=0;i<isla.length;i++) {
			isla[i].dibujarIsla(entorno,camaraX);
		}
		
		//SPAWN ENEMIGOS TIMER 
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
						this.princesa = new Princesa(400, 300);
				        this.camaraX = 0;
				    }

				    this.enemigos[i] = null;
				    continue;
				}
				
				// si el enemigo quedo muy atras o adelante de camaraX a lo volvemos null
				if (this.enemigos[i].SalioPantalla(entorno, camaraX)) {
					this.enemigos[i] = null;
				} else {
					this.enemigos[i].dibujar(entorno, camaraX);
				}
			}
		}
		//ENEMIGOS MEJORADOS mov
		for (int i = 0; i < enemigosMejorados.length; i++) {
			if (this.enemigosMejorados[i] != null) {
				
				// enemigo se actualiza
				this.enemigosMejorados[i].actualizar();
				this.enemigosMejorados[i].dibujar(entorno, camaraX);
				
				// si choca a la princesa le saca vida y null
				if (this.enemigosMejorados[i].colisionConPrincesa(this.princesa)) {
					this.princesa.perderVida();
					this.princesa.perderVida();
					//vemos si sigue viva
					if (!this.princesa.estaViva()) {
						estado = GAME_OVER;
					}
					this.enemigosMejorados[i] = null; 
					continue;
				}
				
				//si se quuda muy atras
				if (this.enemigosMejorados[i].getX() < (camaraX - 100)) {
					this.enemigosMejorados[i] = null;
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
		    boolean impactoE=false;
		    
		    // primero chequeamos colision con enemigos
		    for (int i = 0; i < this.enemigos.length; i++) {
		        if (this.enemigos[i] != null) {
		            if (this.proyectil.colisionaCon(this.enemigos[i])) {
		                this.enemigos[i] = null;
		                this.proyectil = null;
		                impactoE = true;
		                
		                this.contadorEnemigos ++;
		                if(this.contadorEnemigos == 5) {
		                	this.princesa.recuperarVida();
		                	this.contadorEnemigos = 0;
		                }
		                break;
		            }
		        }
		    }
		    
		 // chequeamos que haya golpeado con los Enemigos MEJORADOS
		    if (!impactoE && this.enemigosMejorados != null) {
		        for (int i = 0; i < this.enemigosMejorados.length; i++) {
		            if (this.enemigosMejorados[i] != null) {
		                
		                // usamos el nuevo metodo que agregue en proyectil
		                if (this.proyectil.colisionaConMejorado(this.enemigosMejorados[i])) {
		                    
		                    // le vamos a bajar una vida al enemigo mejorado
		                    int vidaActual = this.enemigosMejorados[i].getVida();
		                    this.enemigosMejorados[i].setVida(vidaActual - 1);
		                    
		                    // si se quedo sin vidas, lo borramos null
		                    if (this.enemigosMejorados[i].getVida() <= 0) {
		                        this.enemigosMejorados[i] = null;
		                    }
		                    
		                    this.proyectil = null; // la bala desaparece siempre al chocar
		                    break;
		                }
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
		
		if(this.entorno.estaPresionada(this.entorno.TECLA_ARRIBA)) {
			princesa.saltar();
		};

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

		castillo.dibujarCastillo(entorno, camaraX);
		
		princesa.dibujarPrincesa(entorno, camaraX);
		princesa.dibujarVidas(entorno);	
		this.dibujarContadorMuertes();
		
		if(this.castillo.colisionConPrincesa(princesa)) {
			princesa.setX(500);
			estado=BOSS;
		}
	}
	
	private void pantallaBoss() {
		entorno.dibujarImagen(fondoBoss, 400, 300, 0);

		entorno.cambiarFont("Impact", 60, Color.RED);
		entorno.escribirTexto("BOSS FINAL", 250, 150);
		
		islaBoss.dibujarIsla(entorno, 0);
		
		if(this.entorno.estaPresionada(this.entorno.TECLA_DERECHA)) {
			princesa.moverDerecha();
		};
		
		if(this.entorno.estaPresionada(this.entorno.TECLA_IZQUIERDA)) {
		    princesa.moverIzquierda(this.camaraX);
		}
		
		if(this.entorno.estaPresionada(this.entorno.TECLA_ARRIBA)) {
			princesa.saltar();
		};
		
		if(princesa.getX() < Princesa.ANCHO / 2) {
		    princesa.setX(Princesa.ANCHO / 2);
		}

		if(princesa.getX() > 800 - Princesa.ANCHO / 2) {
		    princesa.setX(800 - Princesa.ANCHO / 2);
		}
		
		princesa.actualizarFisica();
		
		boolean tocandoSuelo = princesa.colisionaCon(islaBoss);
		
		if(!tocandoSuelo) {
			princesa.despegar();
		}
		
		if(boss.proyectilGolpea(princesa)) {
			princesa.perderTresVidas();
			
			if(!princesa.estaViva()) {
				estado = GAME_OVER;
			}
		}
		
		if (this.entorno.sePresionoBoton(this.entorno.BOTON_IZQUIERDO)) {
		    if (this.proyectil == null) { // solo dispara si no hay uno en vuelo
		        this.proyectil = new Proyectil(
		            this.princesa.getX(),
		            this.princesa.getY(),
		            this.entorno.mouseX(),
		            this.entorno.mouseY(),
		            0
		        );
		    }
		}
		
		if (this.proyectil != null) {
		    
		    this.proyectil.actualizar();
		    
		    if(boss.colisionaCon(this.proyectil)) {
		    	boss.setVida(boss.getVida()-1);
		    	
		    	this.proyectil = null;
		    	
		    	if(boss.getVida() <= 0) {
		    		estado = GANASTE;
		    	}
		    }
		}
		
		if(this.proyectil != null) {
			if(this.proyectil.salio(entorno,0)) {
				this.proyectil = null;
			}else {
				this.proyectil.dibujar(entorno,0);
			}
		}
		
		entorno.cambiarFont("Arial", 25, Color.WHITE);
		entorno.escribirTexto(
		    "Vida Boss: " + boss.getVida(),
		    20,
		    80
		);
		
		boss.actualizar(princesa,entorno);
		boss.dibujar(entorno);
		
		princesa.dibujarPrincesa(entorno, 0);
		princesa.dibujarVidas(entorno);
	}
	
	
	//FINAL
	private void pantallaGameOver() {
		entorno.dibujarImagen(fondoGO, 400, 300, 0);
		
		if(entorno.sePresiono(entorno.TECLA_ENTER)) {
			
			reiniciarJuego();
			estado = JUGANDO;
		}
		
	}
	
	private void reiniciarJuego() {
		this.princesa = new Princesa(400,300);
		this.boss = new Boss(400,350);
		this.camaraX = 0;
		this.enemigos = new Enemigos[5];
		this.enemigosMejorados = new EnemigosMejorados[4];
		this.enemigosMejorados[0] = new EnemigosMejorados(3200, 460);
		this.enemigosMejorados[1] = new EnemigosMejorados(3200, 150);
		this.enemigosMejorados[2] = new EnemigosMejorados(3000, 150);
		this.enemigosMejorados[3] = new EnemigosMejorados(3000, 450);
		this.proyectil = null;
		this.tiempoCreacionEnemigos = 0;
		this.turnoEnemigoArriba = true;
		this.contadorEnemigos = 0;
	}
	
	private void pantallaWin() {
		entorno.dibujarImagen(fondoWin, 400, 300, 0);
		
		if(entorno.estaPresionada(entorno.TECLA_ENTER)) {
			reiniciarJuego();
			estado = JUGANDO;
		}
	}
	
	private void dibujarContadorMuertes() {
		entorno.cambiarFont("Arial", 14, java.awt.Color.black);
		String texto="kills Enemigos: " + this.contadorEnemigos + "/5";
		entorno.escribirTexto(texto, 580, 35);
		
	}
	
	
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
