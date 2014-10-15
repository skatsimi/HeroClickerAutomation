package klikker;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.SwingWorker;
import javax.swing.Timer;


public class WorkerThread extends Thread implements ActionListener{

	static Boolean runClicks;
	private int phase;
	private int world_nr;
	private Timer performActionTimer;
	private Timer ProgressCheckTimer;
	private Timer phase1Timer;
	private Timer phase2Timer;
	private Timer phase4Timer;

	Robot robert;
	
	public WorkerThread(){
		super();
	}


	public void run() {
		
		runClicks = true;
		phase = 0;
		world_nr = 1;
		//Start Timers that will fire no matter which phase we are in.
		performActionTimer = new Timer(500, this);	
		performActionTimer.setActionCommand("doAction");
		performActionTimer.start();
		ProgressCheckTimer = new Timer(120000, this);
		ProgressCheckTimer.setActionCommand("checkProgress");
		ProgressCheckTimer.start();	
		try {
			robert = new Robot();
		} catch (AWTException e) {e.printStackTrace();}
	}
	@Override
	public void actionPerformed(ActionEvent eventArg) {
		try{
			if(runClicks){	
				if(eventArg.getActionCommand().contentEquals("nextPhase")){
						phase++;
						
						if(phase1Timer.isRunning()){
							phase1Timer.stop();
							if(phase2Timer == null){
								phase2Timer = new Timer(TimeToMilli(3,30), this);
								phase2Timer.setActionCommand("nextPhase");
								phase2Timer.start();
							}else{
								phase2Timer.restart();
							}
						}
						
						if(phase == 2){
							System.out.println("Phase(2): Buying Frostleafs entered,		World("+ world_nr +")");
						}
						else if(phase == 3){
							System.out.println("Phase(3): Mass leveling/upgrade entered,		World("+ world_nr +")");
							phase2Timer.stop();
						}
						else if(phase == 5){
							System.out.println("Phase(5): World Ascention entered,		World("+ world_nr +")");
							//Time for a new word, stop all phaseTimers
							phase1Timer.stop();
							phase2Timer.stop();
							phase4Timer.stop();			
						}
					}
					else if(eventArg.getActionCommand().contentEquals("checkProgress")){
						CheckProgressOn();
					}
			
					
					if(phase == 0){
						System.out.println("Phase(0): New world entered,		World("+ world_nr +")");
						//Turn on Progression mode
						CheckProgressOn();
						//Initial clicking to get enough gold to buy a hero
						robert.mouseMove(1044, 632);
						AutoClick(55,4);

						BuyTreeleaf();
						
						if(phase1Timer == null){
							phase1Timer = new Timer(TimeToMilli(11,0), this);
							phase1Timer.setActionCommand("nextPhase");
							phase1Timer.start();
						}else{
								phase1Timer.restart();
						}
						
						phase1Timer.setActionCommand("nextPhase");
						phase1Timer.start();
		
						System.out.println("Phase: First run(1) entered,		World("+ world_nr +")");
						phase = 1;
					}
					if(phase == 1){
						//Hire/level up the 3 most expensive heroes available until phaseTimer1 fires
						BuyBottom3();
					}
					else if(phase == 2){
						//buy only frostleafs
						BuyFrostLeaf();
						buyUpgrades();
					}
					else if(phase == 3){
						MassLevelUp();
						phase =  4;
						System.out.println("Phase(4): TreeBeast, Ivan and Brittany entered,		World("+ world_nr +")");
						if(phase4Timer == null){
							phase4Timer = new Timer(TimeToMilli(25,0), this);
							phase4Timer.setActionCommand("nextPhase");
							phase4Timer.start();
						}else{
							phase4Timer.restart();
						}
						
						this.sleep(750,0);
					}
					else if(phase == 4){
						BuyTreeBeastIVanBrittany();
					}
					else if(phase == 5){
						AscendTheWorld();
						phase = 0;
						
					}
				}
			
		}catch(Exception e){e.printStackTrace();}
	}
		
		public void ScrollDown(int nr_of_taps) throws InterruptedException{

				robert.mouseMove(785, 920);
				for(int i = 0; i < nr_of_taps; ++i){
					Click();
					this.sleep(300, 0);
				}		
		}
		
		public void ScrollUp(int nr_of_taps) throws InterruptedException{
			
			this.sleep(300, 0);
			robert.mouseMove(785, 537);
			for(int i = 0; i < nr_of_taps; ++i){
				Click();
				this.sleep(300, 0);
				robert.mouseMove(785, 537);
			}		
	}
	
		public int TimeToMilli(int minutes, int seconds){
			int milliseconds = 0;
			milliseconds += (minutes * 60 * 1000);
			milliseconds += (seconds * 1000);
			return milliseconds;
		}
		
		public void CheckProgressOn(){
			robert.mouseMove(1285, 562);
			if(ComparePixelColor(new Color(196,160,72),1285, 562)){
				//System.out.println("Progess mode was on, no action taken");
			}
			else{
				Click();
				System.out.println("Progress mode was off, progress mode activated");
				System.out.println("continue to check progress at " +  milliToString(ProgressCheckTimer.getInitialDelay()) + " intervalls");
			}
		}
		
		public String milliToString(int milli){
			int min = 0;
			int sec = 0;
			sec = (milli / 1000);
			min = ((sec - sec%60)/60);
			sec = sec%60;
			String time = "";
			time = Integer.toString(min) + "min " + Integer.toString(sec) + "seconds";
			
			return time;
			
		}
		
		public void AutoClick(int seconds, int clicks_per_sec) throws InterruptedException{
				for(int i = 0; i < seconds; ++i)
				{
					for(int j = 0; j < clicks_per_sec;  ++j)
					{
						Click();
					}
					sleep(1000);
				}
		}
		
		public void Click(){
			robert.mousePress(InputEvent.BUTTON1_MASK);
			robert.mouseRelease(InputEvent.BUTTON1_MASK);
		}
		
		public void CtrlClick(){
		
			robert.keyPress(KeyEvent.VK_CONTROL);
			robert.mousePress(InputEvent.BUTTON1_MASK);
			robert.mouseRelease(InputEvent.BUTTON1_MASK);
			robert.keyRelease(KeyEvent.VK_CONTROL);
		}
		
		public void ZClick(){
			
			robert.keyPress(KeyEvent.VK_Z);
			robert.mousePress(InputEvent.BUTTON1_MASK);
			robert.mouseRelease(InputEvent.BUTTON1_MASK);
			robert.keyRelease(KeyEvent.VK_Z);
		}
		
		public boolean ComparePixelColor(Color rgb, int xPos, int yPos){
			
			if(rgb.equals((robert.getPixelColor(xPos, yPos)))){
				return true;
			}
			return false;
		}
	
		public void BuyBottom3() throws InterruptedException{
			
			robert.mouseMove(784, 904);
			this.sleep(1000, 0);
			Click();
			this.sleep(50, 0);
			robert.mouseMove(784, 537);
			Click();
			this.sleep(500, 0);
			//buy most expensive
			robert.mouseMove(375, 825);
			Click();
			this.sleep(500, 0);
			//buy second most expensive
			robert.mouseMove(375, 735);
			Click();
			this.sleep(500, 0);
			//buy third most expensive
			robert.mouseMove(375, 638);
			Click();
			this.sleep(500, 0);
		}
		
		public void BuyTreeBeastIVanBrittany() throws InterruptedException{
			this.sleep(500,0);
			robert.mouseMove(784, 551);
			Click();
			this.sleep(500,0);
			
			robert.mouseMove(377, 662);
			CtrlClick();
			this.sleep(750,0);
			
			robert.mouseMove(377, 754);
			CtrlClick();
			this.sleep(750,0);
			
			this.sleep(750,0);
			robert.mouseMove(377, 852);
			CtrlClick();
			this.sleep(750,0);
			
			
		}

		public void BuyMostExpensive() throws InterruptedException{
			
			this.sleep(1000,0);
			robert.mouseMove(784, 904);
			Click();
			
			//Hire/Buy Most expensive Hero
			this.sleep(750,0);
			for(int i = 0; i < 5 ; ++i){
				robert.mouseMove(383, 784);
				Click();
			}
			//Buy upgrades
			this.sleep(750,0);
			robert.mouseMove(588, 856);
			Click();
		}
	
		public void MassLevelUp() throws InterruptedException{
			
			this.sleep(1000,0);
			robert.mouseMove(784, 551);
			Click();
			this.sleep(500,0);
			
			//Level up Cid
			this.sleep(750,0);
			robert.mouseMove(381, 573);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			//Level up Treebeast
			this.sleep(750,0);
			robert.mouseMove(377, 664);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			//Level up Ivan
			this.sleep(750,0);
			robert.mouseMove(381, 759);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			//Level up brittany
			this.sleep(750,0);
			robert.mouseMove(378, 850);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
		
			ScrollDown(2);
			//Level up fisherman
			this.sleep(750,0);
			robert.mouseMove(377, 839);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
		
			
			ScrollDown(2);
			//Level up betty
			this.sleep(750,0);
			robert.mouseMove(381, 846);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up samurai
			this.sleep(750,0);
			robert.mouseMove(381, 834);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up leon
			this.sleep(750,0);
			robert.mouseMove(381, 843);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up seer
			this.sleep(750,0);
			robert.mouseMove(381, 827);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(1);
			//Level up alexa
			this.sleep(750,0);
			robert.mouseMove(381, 865);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up natalia
			this.sleep(750,0);
			robert.mouseMove(381, 889);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up mercedes
			this.sleep(750,0);
			robert.mouseMove(381, 877);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up bobby
			this.sleep(750,0);
			robert.mouseMove(381, 884);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up broyle
			this.sleep(750,0);
			robert.mouseMove(381, 873);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up george
			this.sleep(750,0);
			robert.mouseMove(381, 878);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up midas
			this.sleep(750,0);
			robert.mouseMove(381, 873);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up referi
			this.sleep(750,0);
			robert.mouseMove(381, 877);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up abaddon
			this.sleep(750,0);
			robert.mouseMove(381, 867);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up ma zhu
			this.sleep(750,0);
			robert.mouseMove(381, 869);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up amenhotep
			this.sleep(750,0);
			robert.mouseMove(381, 862);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up beastlord
			this.sleep(750,0);
			robert.mouseMove(381,856);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up athena
			this.sleep(750,0);
			robert.mouseMove(381, 855);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up aphrodite
			this.sleep(750,0);
			robert.mouseMove(381, 842);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up shinatobe
			this.sleep(750,0);
			robert.mouseMove(381, 851);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up grant
			this.sleep(750,0);
			robert.mouseMove(381, 840);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			//buy all upgrades
			ScrollDown(6);
			this.sleep(750,0);
			System.out.println("Buyin all uppgrades\n");
			robert.mouseMove(558, 855);
			Click();
			this.sleep(750,0);
			
		}
	
		public void buyUpgrades() throws InterruptedException{
			
			ScrollDown(45);
			robert.mouseMove(556, 852);
			Click();
		}
		
		
		public void BuyCid() throws InterruptedException{
			
			ScrollUp(45);
			this.sleep(750,0);
			robert.mouseMove(377, 567);
			CtrlClick();
			this.sleep(750,0);
		}

		public void BuyTreeleaf() throws InterruptedException{
			
			ScrollUp(45);
			this.sleep(750,0);
			robert.mouseMove(377, 662);
			CtrlClick();
			this.sleep(750,0);
		}
		
		public void BuyIvan() throws InterruptedException{
	
			ScrollUp(45);
			this.sleep(750,0);
			robert.mouseMove(377, 754);
			CtrlClick();
			this.sleep(750,0);
		}
		
		public void BuyBrittany() throws InterruptedException{
			
			ScrollUp(45);
			this.sleep(750,0);
			robert.mouseMove(377, 852);
			CtrlClick();
			this.sleep(750,0);
		}
		
		public void BuyFisherman() throws InterruptedException{
			//TODO: implement late game purchase
		}
		
		public void BuyBetty() throws InterruptedException{
			//TODO: implement late game purchase
		}
		
		public void BuySamurai() throws InterruptedException{
			//TODO: implement late game purchase
		}
		
		public void BuyLeon() throws InterruptedException{
			//TODO: implement late game purchase
		}
		
		public void BuySeer() throws InterruptedException{
			//TODO: implement late game purchase
		}
		
		public void BuyAlexa() throws InterruptedException{
			//TODO: implement late game purchase
		}
		
		public void BuyNatalia() throws InterruptedException{
			//TODO: implement late game purchase
		}
		
		public void BuyMercedes() throws InterruptedException{
			//TODO: implement late game purchase
		}
		
		public void BuyBobby() throws InterruptedException{
			//TODO: implement late game purchase
		}
		
		public void BuyBroyle() throws InterruptedException{
			//TODO: implement late game purchase
		}
		
		public void BuyGeorge() throws InterruptedException{
			//TODO: implement late game purchase
		}
		
		public void BuyMidas() throws InterruptedException{
			//TODO: implement late game purchase
		}
		
		public void BuyReferi() throws InterruptedException{
			//TODO: implement late game purchase
		}

		public void BuyAbaddon() throws InterruptedException{
			//TODO: implement late game purchase
		}

		public void BuyMaZhu() throws InterruptedException{
			//TODO: implement late game purchase
		}

		public void BuyAmenhotep() throws InterruptedException{
			//TODO: implement late game purchase
		}

		public void BuyBeastlord() throws InterruptedException{
			//TODO: implement late game purchase
		}

		public void BuyAthena() throws InterruptedException{
			//TODO: implement late game purchase
		}

		public void BuyAphrodite() throws InterruptedException{
			//TODO: implement late game purchase
		}

		public void BuyShinatobe() throws InterruptedException{
			
		}

		public void BuyGrant() throws InterruptedException{
			//TODO: implement late game purchase
		}

		public void BuyFrostLeaf() throws InterruptedException{

			ScrollDown(45);
			this.sleep(750,0);
			robert.mouseMove(377, 778);
			CtrlClick();
			this.sleep(750,0);
		}
		
		
		public void AscendTheWorld() throws InterruptedException{
			System.out.println("\n Preparing to ascend the world!\n");
			this.sleep(1000);
			robert.mouseMove(784, 548);
			Click();
			this.sleep(1000,0);
			ScrollDown(31);
			this.sleep(500,0);
			System.out.println("\n ASCENDING THE WORDL\n");
			System.out.println("\n ASCENDING THE WORDL\n");
			System.out.println("\n ASCENDING THE WORDL\n");
			robert.mouseMove(573, 873);
			Click();
			this.sleep(1000,0);
			robert.mouseMove(735, 732);
			Click();
			
			this.sleep(5000,0);
			System.out.println("\n World ascended, starting over\n" + "Current World is numberr:" + world_nr);
			
			++world_nr;
		}



	}

