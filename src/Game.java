import java.util.Random; 


public class Game extends Thread {

	private static Random random = new Random();

	/*
	 * Determines the ratio between the simulated time and running time.
	 * Specifically, this number states how many milliseconds should the program
	 * wait to simulate one minute, if periods in the constructor to store are
	 * specified in minutes. Thus, if 'TIME_SIMULATION_FACTOR' is set to 1, a
	 * service time of 1 minutes will be simulated as 1 milliseconds, and a service
	 * time of 10 minutes will be simulated as 10 milliseconds.
	 * 
	 * A good value is 1000, making the simulation clock run 60 faster than the
	 * processes it simulates. Then a service time of 1 minutes(=60 seconds) will be
	 * simulated as 1000 milliseconds(=1 second). Simulating a 1-hour game should
	 * take 1 minute.
	 * 
	 * For example, to simulate that a thread is sleeping x minutes. You should use
	 * sleep(x*TIME_SIMULATION_FACTOR);
	 */
	public static final int TIME_SIMULATION_FACTOR = 1000;
	private Display display;
	private int lastName;
	private long timeForGameM;
	private Players players;
	private int numPlayers;
	private double playerActiveMean;
	private double playerActiveVar;
	private double playerArrivalMean;
	private double playerArrivalVar;
	private long startTime;
	private long playerArrivalTime;
	private long lastArrivalTime;
	private boolean active;

	
	/**
	 * Constructor
	 *
	 */
	public Game(long timeForGameM, int numPlayers, double playerActiveMean, double playerActiveVar,
			double playerArrivalMean, double playerArrivalVar) throws Exception {
		this.timeForGameM = timeForGameM * TIME_SIMULATION_FACTOR;
		this.numPlayers = numPlayers;
		lastName = numPlayers;
		this.playerActiveMean = playerActiveMean;
		this.playerActiveVar = playerActiveVar;
		this.playerArrivalMean = playerArrivalMean;
		this.playerArrivalVar = playerArrivalVar;
		active = true;
	}

	public void run() {
		startTime = System.currentTimeMillis();
		lastArrivalTime = startTime;
		playerArrivalTime = gaussian( (int) playerArrivalMean, (int) playerArrivalVar) * TIME_SIMULATION_FACTOR;
		players = new Players( );
		display = new Display( this );
		for ( int i = 0; i < numPlayers; i ++ )
			players.addPlayer( new Player ( i, gaussian( (int) playerActiveMean, (int) playerActiveVar) * TIME_SIMULATION_FACTOR, this, display, players));
		players.getPlayer( random.nextInt( numPlayers ) ).setOwner( );
		display.invokeInitWindow();
		active();
		display.setFrameInvisible( );
	}
	
	public Players getPlayers() {
		return players;
	}

	public void active( ) {
		players.startAll();
		display.setFramVisible();
		while( System.currentTimeMillis() - startTime < timeForGameM && display.isFrameVisible() ) 
			if( System.currentTimeMillis() - lastArrivalTime >= playerArrivalTime ) addNewPlayer();
		active = false;
	}
	
	public void addNewPlayer( ) {
		lastArrivalTime = System.currentTimeMillis();
		playerArrivalTime = gaussian( (int) playerArrivalMean, (int) playerArrivalVar) * TIME_SIMULATION_FACTOR;
		Player p = new Player(lastName, gaussian( (int) playerActiveMean, (int) playerActiveVar ) * TIME_SIMULATION_FACTOR, this, display, players );
		lastName++;
		players.addPlayer( p  );
		p.start();
	}
	
	public synchronized boolean isActive( ) {
		return active;
	}

	/**
	 * gaussian - compute a random number drawn from a normal (Gaussian)
	 * distribution
	 *
	 * @param periodMean
	 *            - the mean of the distribution
	 * @param periodVar
	 *            - the variance of the distribution
	 * @return
	 */
	public synchronized static int gaussian(int periodMean, int periodVar) {
		double period = 0;
		while (period < 1)
			period = periodMean + periodVar * random.nextGaussian();
		return ((int) (period));
	}
}
