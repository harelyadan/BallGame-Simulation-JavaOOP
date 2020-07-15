public class Player extends Thread {
	private Game game;
	private int name;
	private int timeMS; // the active time in miliseconds.
	private boolean owner;
	private boolean receiver;
	private boolean active;
	private long startTime;
	private Players players;
	private Display display;

	/**
	 * @param name
	 *            - the name of the player
	 * @param game
	 *            - the game the player participate
	 * @param timeActiveM
	 *            - the time (minutes) the player will play
	 */
	public Player(int name, int timeActiveM, Game game, Display display, Players players ) {
		super();
		this.name = name;
		this.game = game;
		this.timeMS = timeActiveM;
		this.receiver = false;
		this.owner = false;
		this.active = true;
		this.players = players;
		this.display = display;
	}

	public void run() {
		startTime = System.currentTimeMillis();
		while( System.currentTimeMillis() - startTime < timeMS && game.isActive() ) 
			if ( owner ) throwTheBall();
		if( game.isActive() ) { 
			if( receiver ) throwTheBall(); 
			players.removePlayer( this ); 
		}
		
	}
	
	public synchronized void shutdown( ) {
		active = false;
	}
	
	public void setOwner( ) { display.setNewOwner( this ); owner = true; receiver = false;  }
	
	public void setReceiver( ) { receiver = true; display.setNewReceiver( this ); }
	
	public boolean isOwner() { return owner; }
	
	public boolean isReceiver() { return receiver; }
	
	public int getNameP( ) { return name; }
	
	public boolean getActiveStatus( ) { return active; }
	
	public synchronized void throwTheBall() {
		while( ( players.getCount() < 2 && game.isActive() ) || receiver) {
			try {
				wait( 1 );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if( !game.isActive() ) return;
		players.getOther( this ).setReceiver();
		owner = false;
	}
	
}
