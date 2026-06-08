package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Boss {
	private double x;
	private double y;
	private double alto;
	private double ancho;
	private int vida;
	private Image imagen;
	private Image imagenProyectil;
	
	private double proyectilX;
	private double proyectilY;
	private double velProyectilX;
	private double velProyectilY;
	
	private boolean proyectilActivo;
	private int contadorDisparo;
	
	public Boss(int x, int y) {
		this.ancho = 2500; 
		this.alto = 200; 
		this.vida = 10;
		this.y = y;
		this.x = x;
		this.imagen = Herramientas.cargarImagen("imagenes/boss.png");
	    this.imagen = this.imagen.getScaledInstance(250, 200, Image.SCALE_SMOOTH);
	    
	    this.imagenProyectil = Herramientas.cargarImagen("imagenes/bola-fuego.png");
	    this.imagenProyectil= this.imagenProyectil.getScaledInstance(40,40, Image.SCALE_SMOOTH);
	}
	
	private void actualizarProyectil(){
		if(proyectilActivo) {
			proyectilX += velProyectilX;
			proyectilY += velProyectilY;
		}
	}
	
	public void disparar(Princesa princesa) {
		this.proyectilX = this.x;
		this.proyectilY = this.y;
		
		double dx = princesa.getX() - this.x;
		double dy = princesa.getY() - this.y;
		
		double distancia = Math.sqrt(dx * dx + dy * dy);
		
		double velocidad = 5;
		
		this.velProyectilX = dx / distancia * velocidad;
		this.velProyectilY = dy / distancia * velocidad;
		
		this.proyectilActivo = true;
	}
	
	public boolean proyectilSalio(Entorno entorno) {
        return proyectilX < 0 || proyectilX > entorno.ancho() ||
               proyectilY < 0 || proyectilY > entorno.alto();
    }
	
	public boolean proyectilGolpea(Princesa princesa) {
		if(!proyectilActivo){
			return false;
		}
		
		double dx = proyectilX - princesa.getX();
		double dy = proyectilY - princesa.getY();
		
		if(Math.sqrt(dx*dx + dy*dy) < 20) {
			proyectilActivo = false;
			return true;
		};
		
		return false;
	}
	
	public void actualizar(Princesa princesa, Entorno entorno) {
		contadorDisparo++;
		
		if(contadorDisparo >= 120) {
			contadorDisparo = 0;
			
			if(!proyectilActivo) {
				disparar(princesa);
			}
		}
		
		actualizarProyectil();
		
		if(proyectilActivo && proyectilSalio(entorno)) {
			proyectilActivo = false;
		}
	}	
	
	public boolean colisionaCon(Proyectil p) {
		double izquierda = this.x - this.ancho / 2;
		double derecha = this.x + this.ancho / 2;
		double arriba = this.y - this.alto / 2;
		double abajo = this.y + this.alto / 2;
		
		double px = p.getX();
		double py = p.getY();
		
		return px >= izquierda && px <= derecha && py >= arriba && py <= abajo;
	}
	
	public void dibujar(Entorno entorno) {
	    entorno.dibujarImagen(
	        this.imagen,
	        this.x ,
	        this.y,
	        0
	    );
	    
	    if(proyectilActivo) {
	    	entorno.dibujarImagen(this.imagenProyectil, proyectilX, proyectilY, 0);
	    }
	}

	
	public int getVida() {
		return vida;
	}


	public void setVida(int vida) {
		this.vida = vida;
	}
}
