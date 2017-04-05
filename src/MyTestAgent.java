import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import agents.MovableAgent;
import behaviours.PositionSender;

/**
 * Test agent that randomly moves across the screen
 * @author Tobias
 *
 */
public class MyTestAgent extends MovableAgent {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3527787841644822961L;


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
                addSubBehaviour(new PositionSender(MyTestAgent.this, 100));
                addSubBehaviour(new TickerBehaviour(myAgent, 100) {
                    @Override
                    protected void onTick() {
                        //System.out.println("Position: " + getPos());
                        ((MyTestAgent)myAgent).move(1.0);
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
    


        
    
}
