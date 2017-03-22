/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.awt.Color;

/**
 *
 * @author Tobias Winkler <tobiwink@ucm.es>
 */
public class Simulation extends AbstractGame {
    
    private final int width, height;
    
    public int x,y = 0;

    public Simulation(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void update(PlayerInput input) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void render(Canvas.CanvasPainter pntr) {
        pntr.drawCircle(x, y, Color.red);
    }
    
    public static Simulation create(int width, int height) {
        Simulation res = new Simulation(width, height);

        SimpleWindow window = SimpleWindow.create(width, height, 60, Color.BLACK);
        AbstractGame.attach(res, window, 60);

        return res;
    }
    
}
