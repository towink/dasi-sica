
import graphics.Simulation;


public class Main {
    
    public static void main(String[] args) {
        
        args = new String[3];
        args[0] = "-agents";
        args[1] = "testAgent1:MyTestAgent;drawer:DrawingAgent";
        args[2] = "-gui";
        jade.Boot.main(args);
    }
    
}
