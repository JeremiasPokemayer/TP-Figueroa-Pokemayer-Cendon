package juego;
import java.awt.Color;
import entorno.Entorno;
import java.awt.Image;
import entorno.Herramientas;

public class Enemigos {
	
	private double x;
	private double y;
	private double alto;
	private double ancho;
	private int direccion;
	private double velocidad;
	private Image imagen;
	//mismo metodo de disparo del boss pero con distintos parametros
	private double proyectilX;
	private double proyectilY;
	private double velProyectilX;
	private double velProyectilY;
	private boolean proyectilActivo;
	private int contadorDisparo;
	private Image imagenProyectil;
	
	public Enemigos(int x, int y) {
		this.ancho = 30; //tamaño
		this.alto = 30; //alto
		this.velocidad = 1.1; //lo rapido que va
		
		//los enemigos aparecen fueran del borde de la camarax
		this.direccion=-1;
		this.y = y;
		this.x = x;
		this.imagen = Herramientas.cargarImagen("imagenes/enemigo.png");
	    this.imagen = this.imagen.getScaledInstance(80, 70, Image.SCALE_SMOOTH);
	    this.imagenProyectil = Herramientas.cargarImagen("imagenes/bola-fuego.png");
	    this.imagenProyectil = this.imagenProyectil.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
	}
	
	
	public boolean colisionConPrincesa(Princesa princesa) {
		if(princesa == null) {
			return false;
		}
		//nuestros npc
		double izquierda1=this.x -this.ancho/2;
		double arriba1 =this.y - this.alto/2;		
		double abajo1 =this.y + this.alto/2;
		double derecha1 =this.x +this.ancho/2;	
		
		//princesa
		double abajo2= princesa.getY() + Princesa.ALTO/2;
		double arriba2 = princesa.getY() - Princesa.ALTO/2;
		double izquierda2 = princesa.getX() - Princesa.ANCHO/2;	//izq pricesa
		double derecha2 = princesa.getX() + Princesa.ANCHO/2;		//der pricesa
		
		return derecha1 > izquierda2 &&
				izquierda1 < derecha2 &&
				arriba1 < abajo2 &&
				abajo1 > arriba2;
	}
	private void disparar(Princesa princesa, int camaraX) {
	    this.proyectilX = this.x;
	    this.proyectilY = this.y;
	    
	    double dx = princesa.getX() - this.x;
	    double dy = princesa.getY() - this.y;
	    double distancia = Math.sqrt(dx * dx + dy * dy);
	    
	    this.velProyectilX = dx / distancia * 5;
	    this.velProyectilY = dy / distancia * 5;
	    this.proyectilActivo = true;
	}

	public boolean proyectilGolpea(Princesa princesa) {
	    if (!proyectilActivo) return false;
	    
	    double dx = proyectilX - princesa.getX();
	    double dy = proyectilY - princesa.getY();
	    
	    if (Math.sqrt(dx * dx + dy * dy) < 15) {
	        proyectilActivo = false;
	        return true; // en Juego esto resta 1 vida
	    }
	    return false;
	}
	
	public void actualizar(Isla[] islas, Princesa princesa, int camaraX) {
	    // movimiento
	    this.x = this.x + (this.velocidad * this.direccion);
	    
	    // disparo
	    this.contadorDisparo++;
	    if (this.contadorDisparo >= 200) {
	        this.contadorDisparo = 0;
	        if (!this.proyectilActivo) {
	            disparar(princesa, camaraX);
	        }
	    }
	    
	    if (this.proyectilActivo) {
	        this.proyectilX += this.velProyectilX;
	        this.proyectilY += this.velProyectilY;
	    }
	}
	

	public void dibujar(Entorno entorno, int camaraX) {
	    entorno.dibujarImagen(
	        this.imagen,
	        this.x - camaraX,
	        this.y,
	        0
	    );
	    if (this.proyectilActivo) {
	        entorno.dibujarImagen(this.imagenProyectil, 
	            this.proyectilX - camaraX, this.proyectilY, 0);
	    }
	}
	
	public boolean SalioPantalla(Entorno entorno, int camaraX) {
		//verifico si el enemigo esta fuera del rango de vista de camarraX
		if(this.x < (camaraX - this.ancho - 50)){
			return true;
		}
		
		if ( this.x >(camaraX + entorno.ancho() + 50)) {
			return true;
		}
		return false;
	}
	
	// metodos para generar enemigos en las islas
	public static Enemigos crearArriba(Entorno entorno, int camaraX) {
	    int posicionX = camaraX + entorno.ancho() + 50;
	    int posicionY = 150; // isla arriba y fija
	    return new Enemigos(posicionX, posicionY);
	}
	public static Enemigos crearAbajo(Entorno entorno, int camaraX) {
	    int posicionX = camaraX + entorno.ancho() + 50;
	    int posicionY = 460; // isla tierra y fija
	    return new Enemigos(posicionX, posicionY);
	}
	
	
	
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}


	public int getAncho() {
		return (int)this.ancho;
	}


	public double getY() {
		return this.y;
	}

}
