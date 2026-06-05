package juego;
import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Isla {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private java.awt.Image imagen;
	
	public Isla(int x,int y,int ancho,int alto) {
		this.x=x;
		this.y=y;
		this.ancho=ancho;
		this.alto=alto;
		this.imagen=Herramientas.cargarImagen("imagenes/pasto2.png");
		this.imagen = this.imagen.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
	}
	
	public void dibujarIsla(Entorno entorno, int camaraX) {
//		entorno.dibujarRectangulo(this.x - camaraX, this.y, this.ancho, this.alto, 0, Color.BLUE);
		entorno.dibujarImagen (this.imagen, this.x - camaraX,this.y - 3 , 0);
	}
	
	public boolean alejaDe(Isla otra) {
		int margenX = 80;
		int margenY = 110;
		
		return
			this.x - this.ancho / 2 - margenX < otra.x + otra.ancho / 2 &&
			this.x + this.ancho / 2 + margenX > otra.x - otra.ancho / 2 &&
			this.y - this.alto / 2 - margenY < otra.y + otra.alto / 2 &&
			this.y + this.alto / 2 + margenY > otra.y - otra.alto / 2;
	}

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getAncho() {
		return ancho;
	}
	public void setAncho(int ancho) {
		this.ancho = ancho;
	}
	public int getAlto() {
		return alto;
	}
	public void setAlto(int alto) {
		this.alto = alto;
	}
	
}
