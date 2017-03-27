package behaviours;

import java.io.IOException;

import agents.MovableAgent;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

/**
 * Behavior applicable to a MoveableAgent that sends its
 * position to the drawer every time it is ticked
 * @author Tobias
 *
 */
public class PositionSender extends TickerBehaviour {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6360475674447886004L;
	private MovableAgent myAgent;

    public PositionSender(MovableAgent a, long period) {
        super(a, period);
        this.myAgent = a;
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
        	msg.setSender(this.myAgent.getAID());
            msg.setContentObject(this.myAgent.getPos());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //msg.setContent(pos.x + "," + pos.y);
        this.myAgent.send(msg);
    }
    
}