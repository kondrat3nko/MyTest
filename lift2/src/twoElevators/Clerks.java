package twoElevators;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JPanel;

public class Clerks {
	private JPanel panel;
	private Lock lock = new ReentrantLock();
	private Condition isFree = lock.newCondition();
	private Random rand = new Random();

	private int side;
	private int startStage;
	private int destStage;
	private int idClerk;
	// Markers
	private boolean movetoElevator = false;
	private boolean inElevator = false;
	private boolean moveFromElevator = false;
	private boolean delivered = false;
	// default position for first stage;
	private int posX = 35;
	private int posY = 485;
	private final int CLERK_SIZE = 90;
	private final int STAGE_SIZE = 105;

	public Clerks(JPanel panel) {

		idClerk = rand.nextInt(1000);
		this.panel = panel;
		side = rand.nextInt(2) + 1;

	}

	public int getSide() {
		return side;
	}

	public int getStartStage() {
		return startStage;
	}

	public void setStartStage(int startStage) {
		this.startStage = startStage;
	}

	public int getDestStage() {
		return destStage;
	}

	public void setDestStage(int destStage) {
		this.destStage = destStage;
	}

	public int getIdClerk() {
		return idClerk;
	}

	public Condition getIsFree() {
		return isFree;
	}

	public Lock getLock() {
		return lock;
	}

	public String getTrend() {
		if ((destStage - startStage) > 0) {
			return "up";
		} else
			return "down";
	}

	public boolean isMovetoElevator() {
		return movetoElevator;
	}

	public void setMovetoElevator(boolean movetoElevator) {
		this.movetoElevator = movetoElevator;
	}

	public boolean isInElevator() {
		return inElevator;
	}

	public void setInElevator(boolean inElevator) {
		this.inElevator = inElevator;
	}

	public boolean isMoveFromElevator() {
		return moveFromElevator;
	}

	public void setMoveFromElevator(boolean moveFromElevator) {
		this.moveFromElevator = moveFromElevator;
	}

	public boolean isDelivered() {
		return delivered;
	}

	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}

	@Override
	public String toString() {
		return "Clerk " + idClerk + " have trend " + getTrend() + " in side "
				+ side + " come to " + startStage + " route to " + destStage
				+ " stage ";
	}

	public void paintWaitElevator(boolean onStage) throws InterruptedException {
		// while (true){
		// доделать
		if (side == 1)
			posX = 5;
		else
			posX = 440;
		switch (startStage) {
		case 2:
			posY = posY - STAGE_SIZE;
			break;
		case 3:
			posY = posY - 2 * (STAGE_SIZE);
			break;
		case 4:
			posY = posY - 3 * (STAGE_SIZE);
			break;
		case 5:
			posY = posY - 4 * (STAGE_SIZE);
			break;

		}
		paint(panel.getGraphics(), onStage);
		// }
	}

	public void paintInElevator(boolean onStage) throws InterruptedException {
		// while (true){
		// доделать

		posY = 468;
		switch (destStage) {
		case 2:
			posY = posY - STAGE_SIZE;
			break;
		case 3:
			posY = posY - 2 * (STAGE_SIZE);
			break;
		case 4:
			posY = posY - 3 * (STAGE_SIZE);
			break;
		case 5:
			posY = posY - 4 * (STAGE_SIZE);
			break;

		}
		paint(panel.getGraphics(), onStage);
		// }
	}

	public void paintGoToElevator(boolean onStage) throws InterruptedException {
		// movetoElevator=true;
		int finish;
		int move;
		lock.lock();
		try {

			if (side == 1) {
				finish = 175;
				move = 5;
			} else {
				finish = 275;
				move = -5;
			}
			while (posX != finish) {

				paint(panel.getGraphics(), onStage);
				posX += move;

				if (posX == finish) {
					movetoElevator = false;

					isFree.signalAll();
				}

			}
		} finally {
			lock.unlock();
		}
	}

	public void paintGoFromElevator(boolean onStage)
			throws InterruptedException {
		// moveFromElevator=true;
		int finish;
		int move;
		lock.lock();
		try {
			if (side == 1) {
				finish = 5;
				move = -5;
			} else {
				finish = 440;
				move = 5;
			}
			while (posX != finish) {

				paint(panel.getGraphics(), onStage);
				posX += move;

				if (posX == finish) {
					moveFromElevator = false;
					// inElevator=false;
					isFree.signalAll();

				}

			}
		} finally {
			lock.unlock();
		}
	}

	public void paint(Graphics g, boolean onStage) throws InterruptedException {

		
		paintClerk(Color.BLACK, g);
		Thread.sleep(50);
		if (!onStage) {
			paintClerk(Color.WHITE, g);
		}
	
		
	}

	private void paintClerk(Color color, Graphics g) {
		g.setColor(color);
		int clerk_part = Math.round(CLERK_SIZE / 4);

		// body
		g.drawLine(posX, posY, posX, posY + CLERK_SIZE);
		// legs
		g.drawLine(posX, posY + 3 * clerk_part, posX + clerk_part, posY
				+ CLERK_SIZE);
		g.drawLine(posX, posY + 3 * clerk_part, posX - clerk_part, posY
				+ CLERK_SIZE);
		// arms
		g.drawLine(posX, posY + clerk_part, posX + Math.round(clerk_part / 2),
				posY + 2 * clerk_part);
		g.drawLine(posX, posY + clerk_part, posX - Math.round(clerk_part / 2),
				posY + 2 * clerk_part);
		// head
		g.drawOval(posX - 5, posY - 10, 10, 10);

	}
}
