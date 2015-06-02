package twoElevators;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.logging.Logger;

import javax.swing.*;

import elevators.ElevatorThread;

public class MainFrame extends JFrame {
	private static int CAPASITY = 3;
	private static int QUANTITY = 10;
	private static int LEFT_SIDE = 1;
	private static int RIGHT_SIDE = 2;

	private static final long serialVersionUID = -4147509637590791420L;
	JPanel panel = new JPanel();
	//Graphics g;
	private static Logger log = Logger.getLogger("MainFrame.Class");
	ExecutorService executor = Executors.newFixedThreadPool(QUANTITY);
	private static List<ClerkThread> clerksList = new LinkedList<ClerkThread>();

	public MainFrame() throws HeadlessException, InterruptedException,
			ExecutionException {
		super();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		setBounds(400, 50, 500, 700);
		
		Container contentPane = getContentPane();
		contentPane.setLayout(null);
		
		JButton btn = new JButton("Start");
		btn.setBounds(50, 10, 160, 20);
		
		contentPane.add(btn);
		panel.setBounds(20, 40, 445, 580);
		panel.setBackground(Color.WHITE);
		contentPane.add(panel);
		
		Semaphore semaphore = new Semaphore(1);
		Semaphore semaphore2 = new Semaphore(1);
		// Button Start
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {

				ElevatorThread firstElevator = new ElevatorThread(panel, 150,
						468);
				firstElevator.setSide(LEFT_SIDE);
				firstElevator.setS2(semaphore2);
				new Thread(firstElevator).start();

				ElevatorThread secondElevator = new ElevatorThread(panel, 250,
						468);
				secondElevator.setSide(RIGHT_SIDE);
				secondElevator.setS2(semaphore2);
				new Thread(secondElevator).start();
				
				Random rand = new Random();
				int first;
				int dest;
				for (int i = 0; i < QUANTITY; i++) {

					first = rand.nextInt(5) + 1;
					dest = rand.nextInt(5) + 1;
					if (first == dest) {
						i--;
						continue;
					}
					Clerks clerk = new Clerks(panel);
					clerk.setStartStage(first);
					clerk.setDestStage(dest);
					clerksList.add(new ClerkThread(clerk, semaphore));

					executor.execute(clerksList.get(i));

				}

			}

		});

	}

	public static int getCAPASITY() {
		return CAPASITY;
	}

	public static int getQUANTITY() {
		return QUANTITY;
	}

	public static List<ClerkThread> getClerksList() {
		return clerksList;
	}

	public static void setClerksList(List<ClerkThread> clerksList) {
		MainFrame.clerksList = clerksList;
	}

	/*
	public void paint(Graphics g) {
		super.paint(g);

		g.drawLine(28, 648, 465, 648);
		int stageHeight = 648;

		for (int i = 1; i <= 5; i++) {

			g.drawLine(28, stageHeight, 472, stageHeight);
			stageHeight -= 110;

		}
		
		
	}*/

}
