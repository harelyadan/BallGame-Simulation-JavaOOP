import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Players implements Iterable<Player> {
	private static Random random = new Random();
	private List<Player> players;
	
	public Players(  ) {
		players = new LinkedList < Player > ();
		
	}

	public void startAll() {
		for( int i = 0; i < players.size(); i ++ )
			players.get( i ).start();
	}
	
	public synchronized void shutdownPlayers() {
		for ( int i = 0; i < players.size(); i++ ) 
			players.get( i ).shutdown();
		
	}
	/**
	 * add player p
	 */
	public synchronized void addPlayer(Player p) {
		players.add( p );
	}
	
	/**
	 * remove player p
	 */
	
	public synchronized void removePlayer(Player p) {
		players.remove( p );
	}
	
	/**
	 * @return a player which is not p.
	 */
	public Player getOther( Player p ) {
		int index = 0;
		while( true ) {
			index = random.nextInt( players.size() );
			try {
				if( players.get(index).getNameP() != p.getNameP() )
					return players.get(index);
			} catch (IndexOutOfBoundsException e) {
				// TODO: handle exception
			}
		}
		
	}
	
	public boolean isExist( int name ) {
		for( int i = 0; i < players.size(); i++ )
			if( name == players.get( i ).getNameP() ) return true;
		return false;
	}
	
	public Player getPlayer( int name ) {
		for( int i = 0; i < players.size(); i ++ )
			if( players.get( i ).getNameP() == name ) return players.get( i );
		return null;
	}
	
//	public Player getPlayerByIndex( int i ) {
//		return players.get( i );
//	}

	/**
	 * @return the number of players
	 */
	public synchronized int getCount() {
		return players.size();
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Player> iterator() {
		return players.iterator();
	}

}
