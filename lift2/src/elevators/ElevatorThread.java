package elevators;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import javax.swing.JPanel;

import twoElevators.ClerkThread;
import twoElevators.MainFrame;

public class ElevatorThread implements Runnable {
    
	private int stage = 1;
	private String trend = "up";
	private JPanel panel;
	private String name;
	private int side;
	private Semaphore s2;
	private int posX, posY;
	private final int LIFT_SIZE = 106;
	private int SPEED = 10;
	
	// private int capasity = MainFrame.getCAPASITY();
	private Map<Integer, Integer> mapElevator = new ConcurrentHashMap<Integer, Integer>();
	Lock lock = new ReentrantLock();
	Condition waiting = lock.newCondition();
	private static Logger log = Logger.getLogger("ElevatorThread.Class");

	public ElevatorThread(JPanel p, int x, int y) {
		this.panel = p;

		// задание начальной позиции
		posX = x;
		posY = y;

	}
    
	public void setSide(int side) {
		this.side = side;
	}

	public void setS2(Semaphore s2) {
		this.s2 = s2;
	}

	public void paint(Graphics g)  {

		
		g.setColor(Color.BLACK);
		g.drawRect(posX, posY + 2, 50, 106);
		g.drawLine(posX + 25, posY + 2, posX + 25, posY + LIFT_SIZE);
		

	}	
	public void clear(Graphics g)  {	
		// удаление 
		g.setColor(panel.getBackground());
		g.drawRect(posX, posY + 2, 50, 106);
		g.drawLine(posX + 25, posY + 2, posX + 25, posY + LIFT_SIZE);
	}

	@Override
	public void run() {

		System.out.println("Lift starting");
		log.info("Lift starting");
        
		// Create side for elevators
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// paint(panel.getGraphics());
		boolean check = true;
		while (true) {

			System.out.println("Now stage is " + stage);
			 
			try {
				paint(panel.getGraphics());
				Thread.sleep(500);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
				 
	//		for (ClerkThread clerkThread : MainFrame.getClerksList()) {
				// Exit from lift
	//	try {
	//		s2.acquire();
		lock.lock();	
		for (Map.Entry<Integer, Integer> entry : mapElevator.entrySet()) {
			if (stage == entry.getValue()) {
			  	

			for (ClerkThread clerksThread : MainFrame
			.getClerksList()) {
			if (entry.getKey() == clerksThread.getClerk()
				.getIdClerk()){
					clerksThread.getClerk().getLock().lock();
					
	         		   try {
	         			  clerksThread.setInElevator(false);
					   System.out.println(entry.getKey() + " "
						+ clerksThread.getClerk().getIdClerk());
					/*   
						while(clerksThread.getClerk().isMoveFromElevator()==false){
						Thread.sleep(1000);
							System.out.println("fuck " + entry.getKey() + " " +clerksThread.getClerk().getIdClerk());
								}*/
						clerksThread.getClerk().getIsFree().await();
						   } catch (InterruptedException e) {
							e.printStackTrace();
							}
							
						
								
							
						
						mapElevator.remove(entry.getKey(), entry.getValue());
						System.out.println("Leave lift " + entry.getKey());
			}}
					}
				}
		//	}
				// Enter to lift
			for (ClerkThread clerkThread : MainFrame.getClerksList()) {
				if ((stage == clerkThread.getClerk().getStartStage())
						&& (mapElevator.size() + 1 <= MainFrame.getCAPASITY())
						&& (clerkThread.isInElevator()==false)
						&& (clerkThread.getClerk().getTrend()==trend)
						&& (clerkThread.getClerk().getSide()==side)) {
				
					clerkThread.getClerk().getLock().lock();
					try {
					clerkThread.setOnStage(false);
	
				
						clerkThread.getClerk().getIsFree().await();
						clerkThread.setInElevator(true);	
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					finally{
				
						clerkThread.getClerk().getLock().unlock();
					}
					mapElevator.put(clerkThread.getClerk().getIdClerk(),
							clerkThread.getClerk().getDestStage());
					
					System.out.println("Enter to lift "
							+ clerkThread.getClerk().getIdClerk());
				}
        
			}
			lock.unlock();

			
			System.out.println("Free space "
					+ (MainFrame.getCAPASITY() - mapElevator.size()));
			System.out.println("In the elevator ");
			for (Map.Entry<Integer, Integer> entry : mapElevator.entrySet()) {
				System.out.println(entry.getKey() + " " + entry.getValue());
			}
			clear(panel.getGraphics());			
			if (check) {
				stage++;
				posY -= 110;
			} else {
				
				stage--;
				posY += 110;
			}
			if (stage == 5) {
				trend = "down";
				check = false;
			}
			if (stage == 1) {
				trend = "up";
				check = true;
			}
				
	
			
		}

		

	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

}
