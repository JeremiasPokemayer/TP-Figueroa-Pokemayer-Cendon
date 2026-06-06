package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Boss {
	private double x;
	private double y;
	private double alto;
	private double ancho;
	private int vida;
	private int direccion;
	private double velocidad;
	private Image imagen;
	
	public Boss(int x, int y) {
		this.ancho = 300; //tamaño
		this.alto = 250; //alto
		this.vida = 10;
		this.velocidad = 1.1; //lo rapido que va
		
		//los enemigos aparecen fueran del borde de la camarax
		this.direccion=-1;
		this.y = y;
		this.x = x;
		this.imagen = Herramientas.cargarImagen("imagenes/boss.png");
	    this.imagen = this.imagen.getScaledInstance(300, 250, Image.SCALE_SMOOTH);
	}
	
	
	public boolean colisionConPrincesa(Princesa princesa) {
		if(princesa == null) {
			return false;
		}
		//boss
		double izquierdaB=this.x -this.ancho/2;
		double arribaB =this.y - this.alto/2;		
		double abajoB =this.y + this.alto/2;
		double derechaB =this.x +this.ancho/2;	
		
		//princesa
		double abajo2= princesa.getY() + Princesa.ALTO/2;
		double arriba2 = princesa.getY() - Princesa.ALTO/2;
		double izquierda2 = princesa.getX() - Princesa.ANCHO/2;	//izq pricesa
		double derecha2 = princesa.getX() + Princesa.ANCHO/2;		//der pricesa
		
		return derechaB > izquierda2 &&
				izquierdaB < derecha2 &&
				arribaB < abajo2 &&
				abajoB > arriba2;
	}
	
	public void actualizar(Isla[] islas) {
		//movimiento 
		this.x= this.x + (this.velocidad * this.direccion);
	}
	

	public void dibujar(Entorno entorno) {
	    entorno.dibujarImagen(
	        this.imagen,
	        this.x ,
	        this.y,
	        0
	    );
	}
}
