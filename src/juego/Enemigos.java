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
	
	public Enemigos(Entorno entorno, int camaraX) {
		this.ancho = 30; //tamaño
		this.alto = 30; //alto
		this.velocidad = 1.1; //lo rapido que va
		
		//los enemigos aparecen fueran del borde de la camarax
		this.x = camaraX + entorno.ancho() + this.ancho;
		this.direccion=-1;
		this.y = 150;
		
	}
	
	
	
	public void actualizar(Isla[] islas) {
		//movimiento 
		this.x= this.x + (this.velocidad * this.direccion);
	}
	

	public void dibujar(Entorno entorno, int camaraX) {
		entorno .dibujarRectangulo(this.x - camaraX, this.y, this.ancho, this.alto, 0, Color.pink);
	}
	
	public boolean SalioPantalla(Entorno entorno, int camaraX) {
		//verifico si acanzo lo suficiente hacia la izquierda o derecha para salir del mapa
		if(this.direccion == -1 && this.x < (camaraX - this.ancho - 100)){
			return true;
		}
		
		if ( this.direccion == 1 && this.x > (camaraX + entorno.ancho() + this.ancho + 100 )) {
			return true;
		}
		return false;
	}
	
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}

}
