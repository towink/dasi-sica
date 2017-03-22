import graphics.Simulation;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.util.logging.Level;
import java.util.logging.Logger; 


public class DrawingAgent extends Agent {
    
    Simulation sim;
    final static int width = 1000;
    final static int height = 1000;
    
    long timeLastReceived = System.currentTimeMillis();
    
    private class PositionReceiveHandler extends CyclicBehaviour {
        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(
                ACLMessage.INFORM);
            ACLMessage msg = myAgent.receive(mt);
            MyTestAgent.Pos pos = null;
            if(msg != null) {
                //System.out.println(System.currentTimeMillis() - timeLastReceived);
                timeLastReceived = System.currentTimeMillis();
                try {
                    //System.out.println(msg.getContent());
                    pos = (MyTestAgent.Pos)msg.getContentObject();
                } catch (UnreadableException ex) {
                    Logger.getLogger(DrawingAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
                sim.x = (int) ((width / 2) + pos.x * (width / 2));
                sim.y = (int) ((height / 2) + pos.y * (height / 2));
                
            }
            else {
                block();
            }
        }
    }
    
    @Override
    protected void setup() {
        System.out.println("Drawing agent with AID " + getAID() + " is setting up.");
        sim = Simulation.create(width, height);
        
        addBehaviour(new PositionReceiveHandler());
        
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(this.getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("position-drawer");
        sd.setName("position-drawer-NAME");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch(FIPAException e) {
            e.printStackTrace();
        }
        
        
        
    }
    
}
