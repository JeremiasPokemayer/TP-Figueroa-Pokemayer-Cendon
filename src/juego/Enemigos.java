package juego;
import java.awt.Color;
import entorno.Entorno;

public class Enemigos {
	
	private double x;
	private double y;
	private double alto;
	private double ancho;
	private int direccion;
	private double velocidad;
	
	public Enemigos(int x, int y) {
		this.ancho = 30; //tamaño
		this.alto = 30; //alto
		this.velocidad = 1.1; //lo rapido que va
		
		//los enemigos aparecen fueran del borde de la camarax
		this.direccion=-1;
		this.y = y;
		this.x = x;
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
	
	public void actualizar(Isla[] islas) {
		//movimiento 
		this.x= this.x + (this.velocidad * this.direccion);
	}
	

	public void dibujar(Entorno entorno, int camaraX) {
		entorno .dibujarRectangulo(this.x - camaraX, this.y, this.ancho, this.alto, 0, Color.pink);
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

}
