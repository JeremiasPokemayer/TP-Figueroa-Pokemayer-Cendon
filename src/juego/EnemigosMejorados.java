package juego;
import java.awt.Color;
import entorno.Entorno;

public class EnemigosMejorados {

	private double x;
	private double y;
	private double alto;
	private double ancho;
	private int direccion;
	private double velocidad;
	private int vida; //vida propia
	
	
	
	public EnemigosMejorados(int x, int y) {
		this.ancho = 30;
		this.alto = 30;
		this.velocidad = 1.4; // mas rapido que el normal
		this.direccion = -1;
		this.y = y;
		this.x = x;
		this.vida = 2; // resiste hassta 2 tiros
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
		double izquierda2 = princesa.getX() - Princesa.ANCHO/2;
		double derecha2 = princesa.getX() + Princesa.ANCHO/2;	
	
		return derecha1 > izquierda2 &&
				izquierda1 < derecha2 &&
				arriba1 < abajo2 &&
				abajo1 > arriba2;
	}
	
	public void actualizar() {
		this.x = this.x + (this.velocidad * this.direccion);
	}
	
	public void dibujar(Entorno entorno, int camaraX) {
		entorno.dibujarRectangulo(this.x - camaraX, this.y, this.ancho, this.alto, 0, Color.YELLOW);
	}
	
	
	public boolean SalioPantalla(Entorno entorno, int camaraX) {
		if(this.x < (camaraX - this.ancho - 20)){
			return true;
		}
		if (this.x > (camaraX + entorno.ancho() + 20)) {
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


	public double getY() {
		return y;
	}


	public void setY(double y) {
		this.y = y;
	}


	public int getVida() {
		return vida;
	}


	public void setVida(int vida) {
		this.vida = vida;
	}
	
	
}
