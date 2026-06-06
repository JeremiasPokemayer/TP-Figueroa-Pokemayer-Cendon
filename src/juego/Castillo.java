package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Castillo {
		private double x;
		private double y;
		private double ancho;
		private double alto;
		private java.awt.Image imagen;
		
		public Castillo(int x,int y,int ancho,int alto) {
			this.x=x;
			this.y=y;
			this.ancho=ancho;
			this.alto=alto;
			this.imagen=Herramientas.cargarImagen("imagenes/castillo.png");
			this.imagen = this.imagen.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
		}
		
		public void dibujarCastillo(Entorno entorno, int camaraX) {
//			entorno.dibujarRectangulo(this.x - camaraX, this.y, this.ancho, this.alto, 0, Color.YELLOW);
			entorno.dibujarImagen (this.imagen, this.x - camaraX,this.y - 3 , 0);
		}
		
		public boolean colisionConPrincesa(Princesa princesa) {
			if(princesa == null) {
				return false;
			}
			//castillo
			double puertaIzquierda=this.x - 40;
			double puertaDerecha =this.x + 40;
			double puertaArriba =this.y + 80;
			double puertaAbajo =this.y + 180;
			
			//princesa
			double abajoP= princesa.getY() + Princesa.ALTO/2;
			double arribaP = princesa.getY() - Princesa.ALTO/2;
			double izquierdaP = princesa.getX() - Princesa.ANCHO/2;
			double derechaP = princesa.getX() + Princesa.ANCHO/2;
			
			return puertaDerecha > izquierdaP &&
					puertaIzquierda < derechaP &&
					puertaArriba < abajoP &&
					puertaAbajo > arribaP;
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}
}
