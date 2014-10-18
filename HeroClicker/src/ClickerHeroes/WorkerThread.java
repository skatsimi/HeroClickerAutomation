package ClickerHeroes;

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
	private Timer phase5Timer;
	private Timer phase6Timer;
	private Timer phase7Timer;
	private Timer phase8Timer;
	

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
		ProgressCheckTimer = new Timer(TimeToMilli(2,30), this);
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
								phase2Timer = new Timer(TimeToMilli(2,0), this);
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
							System.out.println("Phase(5): Frostleaf entered,		World("+ world_nr +")");
							phase4Timer.stop();
							if(phase5Timer == null){
								phase5Timer = new Timer(TimeToMilli(1,10), this);
								phase5Timer.setActionCommand("nextPhase");
								phase5Timer.start();
							}else{
								phase5Timer.restart();
							}
						}
						else if(phase == 6){
							System.out.println("Phase(6): Samurai and seer entered,		World("+ world_nr +")");
							phase5Timer.stop();
							if(phase6Timer == null){
								phase6Timer = new Timer(TimeToMilli(3,0), this);
								phase6Timer.setActionCommand("nextPhase");
								phase6Timer.start();
							}else{
								phase6Timer.restart();
							}
						}
						else if(phase == 7){
							System.out.println("Phase(7): Natalia and Mercedes entered,		World("+ world_nr +")");
							phase6Timer.stop();
							if(phase7Timer == null){
								phase7Timer = new Timer(TimeToMilli(2,0), this);
								phase7Timer.setActionCommand("nextPhase");
								phase7Timer.start();
							}else{
								phase7Timer.restart();
							}
						}
						else if(phase == 8){
							System.out.println("Phase(8): TreebeastIvanBrittany again entered,		World("+ world_nr +")");
							phase7Timer.stop();
							if(phase8Timer == null){
								phase8Timer = new Timer(TimeToMilli(2,0), this);
								phase8Timer.setActionCommand("nextPhase");
								phase8Timer.start();
							}else{
								phase8Timer.restart();
							}
						}
						else if(phase == 9){
							System.out.println("Phase(7): World Ascention entered,		World("+ world_nr +")");
							//Time for a new word, stop all phaseTimers
							phase1Timer.stop();
							phase2Timer.stop();
							phase4Timer.stop();			
							phase5Timer.stop();
							phase6Timer.stop();
							phase7Timer.stop();
							phase8Timer.stop();
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
						robert.mouseMove(1382, 600);
						AutoClick(25,4);

						BuyTreeleaf();
						
						if(phase1Timer == null){
							phase1Timer = new Timer(TimeToMilli(3,30), this);
							phase1Timer.setActionCommand("nextPhase");
							phase1Timer.start();
						}else{
								phase1Timer.restart();
						}
						
						//phase1Timer.setActionCommand("nextPhase");
						//phase1Timer.start();
		
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
						QuickLevelUp();
						phase =  4;
						System.out.println("Phase(4): TreeBeast, Ivan and Brittany entered,		World("+ world_nr +")");
						if(phase4Timer == null){
							phase4Timer = new Timer(TimeToMilli(10,0), this);
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
						BuyFrostLeaf();
					}
					else if(phase == 6){
						BuySamuraiSeer();
					}
					else if(phase == 7){
						BuyNataliaMercedes();
					}
					else if(phase == 8){
						BuyTreeBeastIVanBrittany();
					}
					else if(phase == 9){
						QuickLevelUp();
						AscendTheWorld();
						phase = 0;
						
					}
				}
			
		}catch(Exception e){e.printStackTrace();}
	}
		
		public void ScrollDown(int nr_of_taps) throws InterruptedException{

				robert.mouseMove(929, 1021);
				for(int i = 0; i < nr_of_taps; ++i){
					Click();
					this.sleep(300, 0);
				}		
		}
		
		public void ScrollUp(int nr_of_taps) throws InterruptedException{
			
			this.sleep(300, 0);
			robert.mouseMove(931, 374);
			for(int i = 0; i < nr_of_taps; ++i){
				Click();
				this.sleep(300, 0);
			}		
	}
	
		public int TimeToMilli(int minutes, int seconds){
			int milliseconds = 0;
			milliseconds += (minutes * 60 * 1000);
			milliseconds += (seconds * 1000);
			return milliseconds;
		}
		
		public void CheckProgressOn(){
			robert.mouseMove(1784, 422);
			if(ComparePixelColor(new Color(255,222,101),1784, 422)){
				System.out.println("Progess mode was on, no action taken");
			}
			else{
				System.out.println((robert.getPixelColor(1784, 422).toString()) +" != " + (new Color(255,222,101).toString()) );
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
		
		public void CtrlClick() throws InterruptedException{
		
			robert.keyPress(KeyEvent.VK_CONTROL);
			this.sleep(200);
			robert.mousePress(InputEvent.BUTTON1_MASK);
			robert.mouseRelease(InputEvent.BUTTON1_MASK);
			this.sleep(200);
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
			
			ScrollDown(5);
			
			this.sleep(500, 0);
			//buy most expensive
			robert.mouseMove(245, 778);
			Click();
			this.sleep(500, 0);
			//buy second most expensive
			robert.mouseMove(245, 625);
			Click();
			this.sleep(500, 0);
			//buy third most expensive
			robert.mouseMove(245, 468);
			Click();
			this.sleep(500, 0);
		}
		
		public void BuyTreeBeastIVanBrittany() throws InterruptedException{
			this.sleep(500,0);
			robert.mouseMove(932, 403);
			Click();
			this.sleep(500,0);
			
			robert.mouseMove(245, 590);
			CtrlClick();
			this.sleep(750,0);
			
			robert.mouseMove(245, 747);
			CtrlClick();
			this.sleep(750,0);
			
			this.sleep(750,0);
			robert.mouseMove(245, 902);
			CtrlClick();
			this.sleep(750,0);	
		}
		
		public void BuySamuraiSeer() throws InterruptedException{
			this.sleep(500,0);
			robert.mouseMove(932, 403);
			Click();
			this.sleep(500,0);
			ScrollDown(10);
			
			robert.mouseMove(245, 546);
			CtrlClick();
			this.sleep(750);

			robert.mouseMove(245, 873);
			CtrlClick();
			this.sleep(750);
		}
		
		public void BuyNataliaMercedes() throws InterruptedException{
			this.sleep(500,0);
			robert.mouseMove(932, 403);
			Click();
			this.sleep(500,0);
			ScrollDown(15);
			
			robert.mouseMove(245, 793);
			CtrlClick();
			this.sleep(750);

			robert.mouseMove(245, 953);
			CtrlClick();
			this.sleep(750);
		}
		
		//desktop values
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
			
			ScrollUp(46);
			this.sleep(500,0);
			
			//Level up Cid
			this.sleep(750,0);
			robert.mouseMove(240, 432);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			//Level up Treebeast
			this.sleep(750,0);
			robert.mouseMove(240, 593);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			//Level up Ivan
			this.sleep(750,0);
			robert.mouseMove(240, 756);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			//Level up brittany
			this.sleep(750,0);
			robert.mouseMove(240, 906);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
		
			ScrollDown(2);
			//Level up fisherman
			this.sleep(750,0);
			robert.mouseMove(240, 891);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
		
			
			ScrollDown(2);
			//Level up betty
			this.sleep(750,0);
			robert.mouseMove(240, 901);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up samurai
			this.sleep(750,0);
			robert.mouseMove(240, 880);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up leon
			this.sleep(750,0);
			robert.mouseMove(240, 898);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up seer
			this.sleep(750,0);
			robert.mouseMove(240, 867);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(1);
			//Level up alexa
			this.sleep(750,0);
			robert.mouseMove(240, 961);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up natalia
			this.sleep(750,0);
			robert.mouseMove(240, 972);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up mercedes
			this.sleep(750,0);
			robert.mouseMove(240, 960);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up bobby
			this.sleep(750,0);
			robert.mouseMove(240, 977);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up broyle
			this.sleep(750,0);
			robert.mouseMove(240, 949);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up george
			this.sleep(750,0);
			robert.mouseMove(240, 959);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up midas
			this.sleep(750,0);
			robert.mouseMove(240, 940);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up referi
			this.sleep(750,0);
			robert.mouseMove(240, 963);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up abaddon
			this.sleep(750,0);
			robert.mouseMove(240, 935);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up ma zhu
			this.sleep(750,0);
			robert.mouseMove(240, 950);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up amenhotep
			this.sleep(750,0);
			robert.mouseMove(240, 920);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up beastlord
			this.sleep(750,0);
			robert.mouseMove(240,908);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up athena
			this.sleep(750,0);
			robert.mouseMove(240, 907);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up aphrodite
			this.sleep(750,0);
			robert.mouseMove(240, 894);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up shinatobe
			this.sleep(750,0);
			robert.mouseMove(240, 905);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up grant
			this.sleep(750,0);
			robert.mouseMove(240, 893);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			//buy all upgrades
			ScrollDown(10);
			this.sleep(750,0);
			System.out.println("Buyin all uppgrades\n");
			robert.mouseMove(539, 918);
			Click();
			this.sleep(750,0);
			
		}
	
		public void buyUpgrades() throws InterruptedException{
			
			ScrollDown(45);
			robert.mouseMove(539, 918);
			Click();
		}
		
		//desktop values
		public void BuyCid() throws InterruptedException{
			
			ScrollUp(45);
			this.sleep(750,0);
			robert.mouseMove(377, 567);
			CtrlClick();
			this.sleep(750,0);
		}
		//desktop values
		public void BuyTreeleaf() throws InterruptedException{
			
			ScrollUp(45);
			this.sleep(750,0);
			robert.mouseMove(377, 662);
			CtrlClick();
			this.sleep(750,0);
		}
		//desktop values
		public void BuyIvan() throws InterruptedException{
	
			ScrollUp(45);
			this.sleep(750,0);
			robert.mouseMove(377, 754);
			CtrlClick();
			this.sleep(750,0);
		}
		//desktop values
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
			for(int i = 0; i < 7; ++i)
			{
				this.sleep(750,0);
				robert.mouseMove(240, 796);
				CtrlClick();
				this.sleep(750,0);
			}
		}
		
		
		public void AscendTheWorld() throws InterruptedException{
			System.out.println("\n Preparing to ascend the world!\n");
			this.sleep(1000);
			ScrollUp(46);
			this.sleep(1000,0);
			ScrollDown(31);
			this.sleep(500,0);
			System.out.println("\n ASCENDING THE WORDL\n");
			System.out.println("\n ASCENDING THE WORDL\n");
			System.out.println("\n ASCENDING THE WORDL\n");
			robert.mouseMove(563, 952);
			Click();
			this.sleep(1000,0);
			robert.mouseMove(848, 718);
			Click();
			
			this.sleep(5000,0);
			System.out.println("\n World ascended, starting over\n" + "Current World is numberr:" + world_nr);
			
			++world_nr;
		}

		public void QuickLevelUp() throws InterruptedException{
			
			//same functionality as MassLevelUp but only buys the minimum amount required to get all upgrades 
			ScrollUp(46);
			this.sleep(500,0);
			
			//Level up Cid
			this.sleep(750,0);
			robert.mouseMove(240, 432);
			for(int i = 0; i < 2; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			//Level up Treebeast
			this.sleep(750,0);
			robert.mouseMove(240, 593);
			for(int i = 0; i < 7; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			//Level up Ivan
			this.sleep(750,0);
			robert.mouseMove(240, 756);
			for(int i = 0; i < 7; ++i){
				CtrlClick();
				this.sleep(300,0);
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			//Level up brittany
			this.sleep(750,0);
			robert.mouseMove(240, 906);
			for(int i = 0; i < 7; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
		
			ScrollDown(2);
			//Level up fisherman
			this.sleep(750,0);
			robert.mouseMove(240, 891);
			for(int i = 0; i < 1; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
		
			
			ScrollDown(2);
			//Level up betty
			this.sleep(750,0);
			robert.mouseMove(240, 901);
			for(int i = 0; i < 1; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up samurai
			this.sleep(750,0);
			robert.mouseMove(240, 880);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up leon
			this.sleep(750,0);
			robert.mouseMove(240, 898);
			for(int i = 0; i < 1; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up seer
			this.sleep(750,0);
			robert.mouseMove(240, 867);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(1);
			//Level up alexa
			this.sleep(750,0);
			robert.mouseMove(240, 961);
			for(int i = 0; i < 2; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up natalia
			this.sleep(750,0);
			robert.mouseMove(240, 972);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up mercedes
			this.sleep(750,0);
			robert.mouseMove(240, 960);
			for(int i = 0; i < 8; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up bobby
			this.sleep(750,0);
			robert.mouseMove(240, 977);
			for(int i = 0; i < 2; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up broyle
			this.sleep(750,0);
			robert.mouseMove(240, 949);
			for(int i = 0; i < 2; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up george
			this.sleep(750,0);
			robert.mouseMove(240, 959);
			for(int i = 0; i < 2; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up midas
			this.sleep(750,0);
			robert.mouseMove(240, 940);
			for(int i = 0; i < 2; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up referi
			this.sleep(750,0);
			robert.mouseMove(240, 963);
			for(int i = 0; i < 2; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up abaddon
			this.sleep(750,0);
			robert.mouseMove(240, 935);
			for(int i = 0; i < 1; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up ma zhu
			this.sleep(750,0);
			robert.mouseMove(240, 950);
			for(int i = 0; i < 1; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up amenhotep
			this.sleep(750,0);
			robert.mouseMove(240, 920);
			for(int i = 0; i < 3; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up beastlord
			this.sleep(750,0);
			robert.mouseMove(240,908);
			for(int i = 0; i < 2; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up athena
			this.sleep(750,0);
			robert.mouseMove(240, 907);
			for(int i = 0; i < 2; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			ScrollDown(2);
			//Level up aphrodite
			this.sleep(750,0);
			robert.mouseMove(240, 894);
			for(int i = 0; i < 2; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up shinatobe
			this.sleep(750,0);
			robert.mouseMove(240, 905);
			for(int i = 0; i < 2; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);

			ScrollDown(2);
			//Level up grant
			this.sleep(750,0);
			robert.mouseMove(240, 893);
			for(int i = 0; i < 1; ++i){
				CtrlClick();
				this.sleep(300,0);
			}
			this.sleep(750,0);
			
			//buy all upgrades
			ScrollDown(10);
			this.sleep(750,0);
			System.out.println("Buyin all uppgrades\n");
			robert.mouseMove(539, 918);
			Click();
			this.sleep(750,0);
			
		}

	}

