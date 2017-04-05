package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;
import java.util.Timer;
import java.util.TimerTask;

public class Canvas extends JPanel {

	private Color color;
	private ArrayList<RenderCallback> renderCallbacks = new ArrayList<>();

	private BufferedImage frameBuffer = null;

	public Canvas(int fps, Color color) {
		this.color = color;
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				render();
				
			}
		}, 0, (long) Math.ceil(1000 / (double) fps));

		setCursor(getToolkit().createCustomCursor(
				new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
				"null"));
	}

	public void addRenderCallback(RenderCallback rndr) {
		renderCallbacks.add(rndr);
	}

	public void removeRenderCallback(RenderCallback rndr) {
		renderCallbacks.remove(rndr);
	}

	private void render() {
		if (frameBuffer == null) {
			if (getWidth() == 0 || getHeight() == 0) {
				// Not yet layouted by parent comp.
				return;
			}
			frameBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		}
		final Graphics2D gr = (Graphics2D) frameBuffer.getGraphics(); // AWT legacy
		gr.setColor(color);
		gr.fillRect(0, 0, frameBuffer.getWidth(), frameBuffer.getHeight());

		CanvasPainter pntr = new CanvasPainter() {
			@Override
			public void drawImage(Image img, int x, int y) {
				gr.drawImage(img, x, y, null);
			}
			
			@Override
			public void drawImageBoundary(Image img, int x, int y) {
				gr.setColor(Color.MAGENTA);
				gr.drawRect(x, y, img.getWidth(null), img.getHeight(null));
			}
                        @Override
                        public void drawCircle(int x, int y, Color col) {
                            gr.setColor(col);
                            gr.fill(new Ellipse2D.Double(x, y, 10, 10));
                        }
		};

		for (RenderCallback cb : renderCallbacks) {
			cb.render(pntr);
		}

		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (frameBuffer == null) {
			return;
		}

		g.drawImage(frameBuffer, 0, 0, null);
	}

	public interface RenderCallback {
		public void render(CanvasPainter pntr);
	}

	public interface CanvasPainter {
		public void drawImage(Image img, int x, int y);
		public void drawImageBoundary(Image img, int x, int y);
                public void drawCircle(int x, int y, Color col);
	}
}
