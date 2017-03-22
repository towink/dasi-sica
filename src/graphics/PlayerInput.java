package graphics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

/**
 * This class represents the different commands that the player performed during
 * one iteration of the main loop of the game. Every command can only be
 * performed once and thus it is modeled as a set, i.e. The only information
 * that can be gathered is if the command was sent, not how often.
 */
public class PlayerInput implements KeyListener {

	private final HashSet<Command> commands = new HashSet<>();

	/**
	 * This enum represents every possible command the player can perform.
	 */
	public enum Command {

		LEFT, RIGHT, SHOOT
	}

	/**
	 * Returns true if the given command was performed.
	 */
	public boolean containsCommand(Command cmd) {
		return commands.contains(cmd);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int vk = e.getKeyCode();
		if (vk == KeyEvent.VK_LEFT) {
			commands.add(Command.LEFT);
		} else if (vk == KeyEvent.VK_RIGHT) {
			commands.add(Command.RIGHT);
		} else if (vk == KeyEvent.VK_SPACE) {
			commands.add(Command.SHOOT);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int vk = e.getKeyCode();
		if (vk == KeyEvent.VK_LEFT) {
			commands.remove(Command.LEFT);
		} else if (vk == KeyEvent.VK_RIGHT) {
			commands.remove(Command.RIGHT);
		} else if (vk == KeyEvent.VK_SPACE) {
			commands.remove(Command.SHOOT);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Not needed
	}
}
