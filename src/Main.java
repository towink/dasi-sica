


public class Main {
    
    public static void main(String[] args) {
        
        args = new String[3];
        args[0] = "-agents";
        
        String s = "";
        for (int i = 0; i < 500; i++) {
        	s += "testAgent" + i + ":MyTestAgent;";
        }
        
        args[1] = s + "drawer:DrawingAgent";
        args[2] = "-gui";
        jade.Boot.main(args);
    }
    
}
