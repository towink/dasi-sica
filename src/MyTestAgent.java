import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.vecmath.Vector2d;

public class MyTestAgent extends Agent {
    
    private Pos pos = new Pos();
    
    private double vx = 0.0;
    private double vy = 0.0;
    private double ax = 0.0001;
    private double ay = 0.0001;
    
    private Random r = new Random();
    
    public static class Pos implements Serializable {
        public double x,y = 0;
    }
    
    private class PositionSender extends TickerBehaviour {

        public PositionSender(Agent a, long period) {
            super(a, period);
        }
        @Override
        public void onTick() {
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("position-drawer");
            template.addServices(sd);
            AID drawerID = null;
            try {
                DFAgentDescription[] result = DFService.search(myAgent, template);
                if(result.length != 0) {
                    drawerID = result[0].getName();
                }
            }
            catch (FIPAException fe) {
                fe.printStackTrace();
            }
            
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(drawerID);
            try {
                msg.setContentObject(pos);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //msg.setContent(pos.x + "," + pos.y);
            send(msg);
        }
        
    }
    
    protected void setup() {
        // Printout a welcome message
        System.out.println("Hello! Test agent " + getAID().getName() + " is ready!");
        System.out.println(getAMS());
        
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(this.getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("publish-position");
        sd.setName("publish-position");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch(FIPAException e) {
            e.printStackTrace();
        }
        
        addBehaviour(new ParallelBehaviour(this, ParallelBehaviour.WHEN_ALL) {
            @Override
            public void onStart() {
                addSubBehaviour(new PositionSender(myAgent, 30));
                addSubBehaviour(new TickerBehaviour(myAgent, 30) {
                    @Override
                    protected void onTick() {
                        System.out.println("(" + pos.x + ", " + pos.y + ")");
                        ((MyTestAgent)myAgent).move();
                    }
                 });
                addSubBehaviour(new TickerBehaviour(myAgent, 1000) {
                    @Override
                    protected void onTick() {
                        ((MyTestAgent)myAgent).randomlyAccelerate();
                    }
                 });
            }
        });
        
    }
    
    public void move() {
        vx += ax;
        vy += ay;
        double max = 0.01;
        double norm = Math.sqrt(vx * vx + vy * vy);
        if(norm > max) {
            vx /= norm;
            vx *= max;
            vy /= norm;
            vy *= max;
        }
        pos.x += vx;
        pos.y += vy;
        if(Math.abs(pos.x) > 1.0) {
            pos.x = 0;
        }
        if(Math.abs(pos.y) > 1.0) {
            pos.y = 0;
        }
        //pos.x++;
        //pos.y++;
        //pos.x += 0.01 * (r.nextDouble() * 2 - 1);
        //pos.y += 0.01 * (r.nextDouble() * 2 - 1);
    }
    
    public void randomlyAccelerate() {
        /*vx = 0;
        vy = 0;*/
        double max = 0.001;
        ax = max * (r.nextDouble() * 2 - 1);
        ay = max * (r.nextDouble() * 2 - 1);
        double norm = Math.sqrt(ax * ax + ay * ay);
        if(norm > max) {
            ax /= norm;
            ax *= max;
            ay /= norm;
            ay *= max;
        }
        System.out.println(ax);
        System.out.println(ay);
    }

        
    
}
