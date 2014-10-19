package ClickerHeroes;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;




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
	private JLabel phaseInfoLabel;
	private JLabel worldLabel;
	private JProgressBar phaseProgressBar;
	private JProgressBar worldProgressBar;
	private static GUI klikk;
	
	
	public GUI() throws AWTException {

		initGUI();
		System.out.println("a");
		runClicks = false;
	}
	
	public static void main(String args[]){
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
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
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		
		final JButton clickButton = new JButton("Start");
		clickButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	if(runClicks == false){
            		runClicks = true;
            		try {
						newBot();
					} catch (AWTException e) {e.printStackTrace();}
            		clickButton.setText("Cancel");     	
						clickButton.requestFocus();
            	}
            	else{
            		clickButton.setText("Start");
            		runClicks = false;
            		cancelBot();
            	}
            }
        });
		
		currentTimeLabel = new JLabel("current time");
		totalTimeLabel = new JLabel("total time");
		phaseLabel = new JLabel("Phase");
		phaseInfoLabel = new JLabel("phaseInfo");
		worldLabel = new JLabel("World");
		phaseProgressBar = new JProgressBar();
		worldProgressBar = new JProgressBar();
		
		pane.add(totalTimeLabel);
		pane.add(worldLabel);
		pane.add(currentTimeLabel);
		pane.add(worldProgressBar);
		worldProgressBar.setMaximumSize(new Dimension(100,15));
		worldProgressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		pane.add(phaseLabel);
		pane.add(phaseProgressBar);
		phaseProgressBar.setMaximumSize(new Dimension(100,15));
		phaseProgressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		pane.add(phaseInfoLabel);
		pane.add(clickButton);
	
		pack();
		
		setTitle("Hero Clicker Automaton");
		setSize(400,130);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setAlwaysOnTop(true);
		this.setResizable(false);
		this.setMaximizedBounds(getBounds());
		
	}
	
	public void newBot() throws AWTException{
		if(bot == null){
			bot = new BotThread();
			bot.addPropertyChangeListener( new PropertyChangeListener(){
					public void propertyChange(PropertyChangeEvent evt){

						 if(evt.getPropertyName().equals("timeSpentTotal")){
							totalTimeLabel.setText("Running time: " + evt.getNewValue().toString());
						}
						 else if(evt.getPropertyName().equals("timeSpentCurrent")){
							currentTimeLabel.setText("Current world time: "+ evt.getNewValue().toString());
						}
						else if(evt.getPropertyName().equals("phase")){
							phaseLabel.setText("Phase: " + evt.getNewValue() + " / " + evt.getOldValue());
						}
						else if(evt.getPropertyName().equals("phaseInfo")){
							phaseInfoLabel.setText(evt.getNewValue().toString());
						}
						else if(evt.getPropertyName().equals("phaseProgress")){
							phaseProgressBar.setValue((int) evt.getNewValue());
						}
						else if(evt.getPropertyName().equals("world")){
							worldLabel.setText("Current World: " + evt.getNewValue().toString());
						}
						 worldProgressBar.setValue(bot.getProgress());
					}
			});
			bot.execute();
		}
		else{
			bot.startBot();
		}
	}
	
	public void cancelBot(){
		if(bot != null){
			bot.cancelBot();
		}
	}
}
