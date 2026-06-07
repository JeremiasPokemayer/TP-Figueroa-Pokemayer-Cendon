package juego;
import java.awt.Color;
import entorno.Entorno;


public class Proyectil {
	private double x;
    private double y;
    private double velX;
    private double velY;
    
    private static final double VELOCIDAD = 10;
    private static final int RADIO = 8;
    
    public Proyectil(double xPrincesa, double yPrincesa, int mouseX, int mouseY, int camaraX) {
        this.x = xPrincesa;
        this.y = yPrincesa;
        
        // direccion hacia el mouse
        double dx = (mouseX + camaraX) - xPrincesa;
        double dy = mouseY - yPrincesa;
        
        // normalizamos para que la velocidad sea constante
        double distancia = Math.sqrt(dx * dx + dy * dy);
        this.velX = (dx / distancia) * VELOCIDAD;
        this.velY = (dy / distancia) * VELOCIDAD;
    }
    public void actualizar() {
        this.x += this.velX;
        this.y += this.velY;
    }
    
    public void dibujar(Entorno entorno, int camaraX) {
        entorno.dibujarCirculo(this.x - camaraX, this.y, RADIO, Color.YELLOW);
    }
    
    public boolean salio(Entorno entorno, int camaraX) {
        return this.x < camaraX || this.x > camaraX + entorno.ancho() ||
               this.y < 0 || this.y > entorno.alto();
    }
    
    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public int getRadio() { return RADIO; }
    
    
    
    //colisiones con enemigos
    public boolean colisionaCon(Enemigos enemigo) {
        double dx = this.x - enemigo.getX();
        double dy = this.y - enemigo.getY();
        double distancia = Math.sqrt(dx * dx + dy * dy);
        return distancia < RADIO + enemigo.getAncho() / 2;
    }
    
    public boolean colisionaConMejorado(EnemigosMejorados enemigo) {
        double dx = this.x - enemigo.getX();
        double dy = this.y - enemigo.getY();
        double distancia = Math.sqrt(dx * dx + dy * dy);
        
        return distancia < RADIO + enemigo.getAncho()/2;
    }
    
}



