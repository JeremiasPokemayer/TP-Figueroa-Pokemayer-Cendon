package juego;

import java.awt.Color;

import entorno.Entorno;

public class Castillo {
		private double x;
		private double y;
		private double ancho;
		private double alto;
		
		public Castillo(int x,int y,int ancho,int alto) {
			this.x=x;
			this.y=y;
			this.ancho=ancho;
			this.alto=alto;
		}
		
		public void dibujarCastillo(Entorno entorno, int camaraX) {
			entorno.dibujarRectangulo(this.x - camaraX, this.y, this.ancho, this.alto, 0, Color.YELLOW);
		}
		
		public boolean colisionConPrincesa(Princesa princesa) {
			if(princesa == null) {
				return false;
			}
			//castillo
			double izquierda=this.x -this.ancho/2;
			double arriba =this.y - this.alto/2;
			double abajo =this.y + this.alto/2;
			double derecha =this.x +this.ancho/2;
			
			//princesa
			double abajoP= princesa.getY() + Princesa.ALTO/2;
			double arribaP = princesa.getY() - Princesa.ALTO/2;
			double izquierdaP = princesa.getX() - Princesa.ANCHO/2;
			double derechaP = princesa.getX() + Princesa.ANCHO/2;
			
			return derecha > izquierdaP &&
					izquierda < derechaP &&
					arriba < abajoP &&
					abajo > arribaP;
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}
}
