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
	
	private double proyectilX;
	private double proyectilY;
	private double velProyectilX;
	private double velProyectilY;
	
	private boolean proyectilActivo;
	private int contadorDisparo;
	
	public Boss(int x, int y) {
		this.ancho = 300; 
		this.alto = 250; 
		this.vida = 10;
		this.y = y;
		this.x = x;
		this.imagen = Herramientas.cargarImagen("imagenes/boss.png");
	    this.imagen = this.imagen.getScaledInstance(300, 250, Image.SCALE_SMOOTH);
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
	
	public void actualizar(Princesa princesa) {
		contadorDisparo++;
		
		if(contadorDisparo >= 120) {
			contadorDisparo = 0;
			
			if(!proyectilActivo) {
				disparar(princesa);
			}
		}
		
		actualizarProyectil();
	}
	
	public void dibujar(Entorno entorno) {
	    entorno.dibujarImagen(
	        this.imagen,
	        this.x ,
	        this.y,
	        0
	    );
	    
	    if(proyectilActivo) {
	    	entorno.dibujarCirculo(proyectilX, proyectilY, 10.0, Color.RED);
	    }
	}

	
	public int getVida() {
		return vida;
	}


	public void setVida(int vida) {
		this.vida = vida;
	}
}
