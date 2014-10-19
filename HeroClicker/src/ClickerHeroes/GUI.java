package ClickerHeroes;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ActionMap;
import javax.swing.GroupLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;




public class GUI extends JFrame{

	/**
	 * GUI for hero clicker bot
	 */
	private static final long serialVersionUID = 1L;
	private BotThread bot;
	boolean runClicks;
	private JLabel totalTimeLabel;
	private JLabel currentTimeLabel;
	private JLabel phaseLabel;
	private JLabel worldLabel;
	
	
	public GUI() throws AWTException {

		initGUI();
		System.out.println("a");
		runClicks = false;
	}
	
	public static void main(String args[]){
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					GUI klikk;
					try {
						klikk = new GUI();
						klikk.setVisible(true);
					} catch (AWTException e) {e.printStackTrace();}
				}
			});	
		 
	}
	private void initGUI()
	{

		Container  pane = getContentPane();
		FlowLayout gl = new FlowLayout();
		pane.setLayout(gl);
		
		final JButton clickButton = new JButton("Start clicking");
		clickButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	if(runClicks == false){
            		runClicks = true;
            		newBot();
            		clickButton.setText("Stop clicking");     	
						clickButton.requestFocus();
            	}
            	else{
            		clickButton.setText("Start clicking");
            		runClicks = false;			
            	}
            }
        });
		
		currentTimeLabel = new JLabel("current time");
		totalTimeLabel = new JLabel("total time");
		phaseLabel = new JLabel("Phase");
		worldLabel = new JLabel("World");
		
		//gl.setAutoCreateContainerGaps(true);
		//gl.setHorizontalGroup(gl.createSequentialGroup().addComponent(clickButton));
		//gl.setVerticalGroup(gl.createSequentialGroup().addComponent(clickButton));
		pane.add(clickButton);
		pane.add(currentTimeLabel);
		pane.add(phaseLabel);
		pane.add(totalTimeLabel);
		pane.add(worldLabel);
		pack();
		
		setTitle("Hero Clicker Automaton");
		setSize(150,75);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setAlwaysOnTop(true);
		this.setResizable(false);
		this.setMaximizedBounds(getBounds());
		
	}
	
	public void newBot(){
		if(bot == null){
			bot = new BotThread();
			bot.addPropertyChangeListener( new PropertyChangeListener(){
					public void propertyChange(PropertyChangeEvent evt){

						 if(evt.getPropertyName().equals("timeSpentTotal")){
							totalTimeLabel.setText("Time spent: " + evt.getNewValue().toString());
						}
						 else if(evt.getPropertyName().equals("timeSpentCurrent")){
							currentTimeLabel.setText("Time spent in this world: "+ evt.getNewValue().toString());
						}
						else if(evt.getPropertyName().equals("phase")){
							phaseLabel.setText("Phase: " + evt.getNewValue() + " / " + evt.getOldValue());
						}
						else if(evt.getPropertyName().equals("world_nr")){
							worldLabel.setText("Current World: " + evt.getNewValue());
						}
						
					}
			});
		}
		bot.execute();
	}
	

}
