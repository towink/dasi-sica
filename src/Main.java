
import graphics.Simulation;


public class Main {
    
    public static void main(String[] args) {
        
        args = new String[3];
        args[0] = "-agents";
        args[1] = "testAgent1:MyTestAgent;testAgent2:MyTestAgent;testAgent3:MyTestAgent;testAgent4:MyTestAgent;testAgent5:MyTestAgent;testAgent6:MyTestAgent;drawer:DrawingAgent";
        args[2] = "-gui";
        jade.Boot.main(args);
    }
    
}
