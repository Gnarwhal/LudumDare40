package com.gnarly.game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

import com.gnarly.engine.audio.ALManagement;
import com.gnarly.engine.display.Camera;
import com.gnarly.engine.display.Window;
import com.gnarly.engine.shaders.Shader;

public class Main {

	private int FPS = 60;
	
	public static double dtime;
	public static double ttime;
	
	private ALManagement al;
	
	private Window window;
	private Camera camera;
	
	private Menu menu;
	private GamePanel panel;
	
	private int state = 0;
	
	public void start() {
		long curTime, pastTime, startTime, nspf = 1000000000 / FPS;
		init();
		pastTime = System.nanoTime();
		startTime = pastTime;
		while(!window.shouldClose()) {
			curTime = System.nanoTime();
			if(curTime - pastTime > nspf) {
				dtime = (curTime - pastTime) / 1000000000d;
				ttime = (curTime - startTime) / 1000000000d;
				update();
				render();
				pastTime = curTime;
			}
		}
		al.destroy();
		Window.terminate();
	}
	
	private void init() {
		al = new ALManagement();
		window = new Window("Snake++", true);
		camera = new Camera(32, 18);
		Shader.init();
		menu = new Menu(window, camera);
		panel = new GamePanel(window, camera);
	}
	
	private void update() {
		window.update();
		if(window.keyPressed(GLFW_KEY_ESCAPE) == 1)
			window.close();
		if(state == 0) {
			menu.update();
			state = menu.getState();
			if(state == 1)
				panel.setActive();
		}
		else {
			panel.update();
			state = panel.getState();
		}
		camera.update();
	}
	
	private void render() {
		window.clear();
		if(state == 0)
			menu.render();
		else
			panel.render();
		window.swap();
	}
	
	public static void main(String[] args) {
		new Main().start();
	}
}
