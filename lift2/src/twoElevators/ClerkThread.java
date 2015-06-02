package twoElevators;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

public class ClerkThread implements Runnable {
	private Clerks clerk;
	private Semaphore semaphore;
	private static Logger log = Logger.getLogger("ThreadClerk.Class");
	private boolean onStage = true;
	private boolean inElevator = false;

	public ClerkThread(Clerks clerk, Semaphore semaphore) {
		super();
		this.clerk = clerk;
		this.semaphore = semaphore;

	}

	@Override
	public void run() {

		log.info(clerk.toString() + " come to start stage");

		try {
			clerk.paintWaitElevator(onStage);
			while (onStage) {
				// Thread.sleep(500);
				// log.info(clerk.toString() + " on stage");

			}
            // ¬от тут идЄт первый перехват управлени€ потоком
			semaphore.acquire();
			clerk.setMovetoElevator(true);
			clerk.paintGoToElevator(onStage);
			// clerk.setInElevator(true);
			
			
			int i = 0;
			semaphore.release();
			inElevator = true;
			while (inElevator) {
				i++;
				if (i == 1)
					log.info(clerk.toString()
							+ " in to lift ///////////////////////");
				Thread.sleep(1000);
				
					
		//			if((i==20)&&(!clerk.isInElevator())) inElevator=false;
			}
			// “ут второй
			// Ќужно сделать так чтоб после первого отработал массив
			// ѕотом управление перешло на второй
			// пройди пошагово, € не ходил и посмотри точно как он ходит
			// но отрабатывают они сразу по очереди без задержки
			// € думаю пока повесить какое условие на второй acquire
			// и въебать цикл
			//if (!inElevator)
			clerk.paintInElevator(onStage);
			semaphore.acquire();
			
			clerk.setMoveFromElevator(true);
		//	log.info("I get true ------------------------------------");
			clerk.paintGoFromElevator(onStage);
			semaphore.release();
			log.info(clerk.toString() + " come from lift dest stage");
			clerk.setStartStage(0);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public Clerks getClerk() {
		return clerk;
	}

	public boolean isOnStage() {
		return onStage;
	}

	public void setOnStage(boolean onStage) {
		this.onStage = onStage;
	}

	public boolean isInElevator() {
		return inElevator;
	}

	public void setInElevator(boolean inElevator) {
		this.inElevator = inElevator;
	}

}
