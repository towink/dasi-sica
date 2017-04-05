/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.awt.Color;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import jade.core.AID;
import util.TwoDVector;

/**
 * Controller for displaying agents on screen
 * You can add new agents giving the agent's
 * AID and a position, or update the position of an already
 * existing agent in the same fashion.
 * You can also remove agents from the screen so that
 * they are no longer displayed.
 * @author Tobias Winkler <tobiwink@ucm.es>
 * @author Daniel Báscones
 */
public class Simulation extends AbstractGame {
    
    private final int width, height;
    
    private ConcurrentHashMap<AID, TwoDVector> objects;

    public void addObject(AID object, TwoDVector position) {
    	this.objects.put(object, position);
    }
    
    public void removeObject(AID object) {
    	this.objects.remove(object);
    }
    

    public Simulation(int width, int height) {
        this.width = width;
        this.height = height;
        objects = new ConcurrentHashMap<AID, TwoDVector>();
    }

    @Override
    public void update(PlayerInput input) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void render(Canvas.CanvasPainter pntr) {
    	for (Entry<AID, TwoDVector> e: objects.entrySet()) {
    		int x = (int) ((width / 2) + e.getValue().x * (width / 2));
    		int y = (int) ((height / 2) + e.getValue().y * (height / 2));
    		pntr.drawCircle(x, y, Color.red);
    	}
    }
    
    public static Simulation create(int width, int height) {
        Simulation res = new Simulation(width, height);

        SimpleWindow window = SimpleWindow.create(width, height, 60, Color.BLACK);
        AbstractGame.attach(res, window, 60);

        return res;
    }
    
}
