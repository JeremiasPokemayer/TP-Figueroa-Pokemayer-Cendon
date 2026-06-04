package juego;
import java.awt.Color;
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
private Image VIDAS;

private static int VELOCIDAD = 6;
private static double GRAVEDAD= 0.6;
private static double FUERZA_SALTO= -16.5;
public static int ANCHO= 40;
public static double ALTO= 60;

public Princesa(int x, int y) {
	this.x = x;
	this.y = y;
	this.velY = 0; 
	this.vidas = 4;
	this.enSuelo = false;
	this.imagen = Herramientas.cargarImagen("imagenes/PRINCESA.png");
	this.imagen = this.imagen.getScaledInstance(50, 70, Image.SCALE_SMOOTH);
	this.VIDAS = Herramientas.cargarImagen("imagenes/VIDAS.png");
    this.VIDAS = this.VIDAS.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
}

public void dibujarPrincesa(Entorno entorno, int camaraX) {
    entorno.dibujarImagen(this.imagen, this.x - camaraX, this.y, 0);
    
}

//MOVIMIENTO//

public void moverIzquierda(int camaraX) {
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
public boolean colisionaCon(Isla isla) {
	double arribaI= isla.getY() - isla.getAlto()/2; 	//piso de la isla
	double abajoI = isla.getY() + isla.getAlto()/2;		//techo de la isla
	double izquierdaI = isla.getX() - isla.getAncho()/2;//izq de la isla
	double derechaI = isla.getX() + isla.getAncho()/2;	//der de la isla
	
	double abajoP= this.y + ALTO/2;			//pies pricesa
	double arribaP = this.y - ALTO/2;		//cabeza pricesa
	double izquierdaP = this.x - ANCHO/2;	//izq pricesa
	double derechaP = this.x + ANCHO/2;		//der pricesa

	boolean colision =
			derechaP > izquierdaI &&
			izquierdaP < derechaI &&
			abajoP > arribaI &&
			arribaP < abajoI;
			
	if(colision && velY>=0 && abajoP - velY<= arribaI) {
		this.y = arribaI - ALTO/2;
		this.velY = 0;
		this.enSuelo = true;
		
		return true;
	}
	
	else if(colision && velY < 0 && arribaP - velY >= abajoI) {
		this.y = abajoI + ALTO/2;
		this.velY = 0;
	}
	
	else if(colision && this.x > isla.getX()) {
		this.x = derechaI + ANCHO/2;
	}
	
	else if(colision && this.x< isla.getX()) {
		this.x = izquierdaI - ANCHO/2;
	}
	
	return false;
}

//LLama a la funcion cuando la princesa no esta sobre ninguna isla
public void despegar() {
	this.enSuelo = false;
}

public boolean isEnSuelo() {
	return enSuelo;
}
public void perderVida() {
	this.vidas= this.vidas -1;
}
public void dibujarVidas(Entorno entorno) {
    for(int i = 0; i < this.vidas; i++) {
        entorno.dibujarImagen(
            this.VIDAS,
            30 + i * 40,
            30,
            0
        );
    }
}
public void reiniciar() {
    this.x = 400;
    this.y = 300;
    this.velY = 0;
    this.enSuelo = false;
}

public double getX() {
	return x;
}

public double getY() {
	return y;
}

public int getVidas() {
	return vidas;
}
public boolean estaViva() {
	return this.vidas>0;
}
public void setX(double x) {
    this.x = x;
}

}
