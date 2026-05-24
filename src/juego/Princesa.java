package juego;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Princesa {

private double x;
private double y;
private double velY; //velocidad vertical (gravedad y salto)
private int vidas;
private boolean enSuelo; //true cuando esta parada sobre algo
private Image imagen;

private static final double VELOCIDAD = 5;
private static final double GRAVEDAD= 0.5;
private static final double FUERZA_SALTO= -12;
public static final int ANCHO= 40;
public static final int ALTO= 60;

public Princesa(double x, double y) {
	this.x = x;
	this.y = y;
	this.velY = 0; 
	this.vidas = 4;
	this.enSuelo = false;
	this.imagen = Herramientas.cargarImagen(null);
}

//MOVIMIENTO//

public void moverIzquierda() {
	this.x -= VELOCIDAD;
}
public void moverDerecha() {
	this.x += VELOCIDAD;
}
public void saltar() {
	if (this.enSuelo) { //solo puede saltar si esta en el suelo
		this.velY = FUERZA_SALTO;
		this.enSuelo = false; //hasta que toque una isla es false
	}
	
}
//FISICAS
public void actualizarFisica() {
	if (!this.enSuelo) {
		this.velY += GRAVEDAD;
	}
	this.y += this.velY;
} 

//LLama a la funcion cuando los pies de la princesa tocan el suelo de una isla
public void aterrizaEn(double pisoY) {
	this.y =pisoY - ALTO/2.0;
	this.velY = 0;
	this.enSuelo = true;
}

//LLama a la funcion cuando la princesa no esta sobre ninguna isla
public void despegar() {
	this.enSuelo = false;
}



}
