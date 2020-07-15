import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Display extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private final int radius = 150;
	private final int ballSpeed = 10;
	private final  Point center = new Point( 210, 210 );
	private Point ballPos;
	private Point des;
	private Map< Integer, Point > map;
	private Players players;
	private JFrame jframe;
	private Iterator<Player> playersIterator;
	private Player receiver;
	private Player owner;
	private boolean ballFlying;

	public Display( Game game ) {
		map = new HashMap< Integer, Point>();
		players =  game.getPlayers();
		ballFlying = false;
		ballPos = new Point( center );
		des = new Point( center );
		jframe = new JFrame();
	}

	public void invokeInitWindow() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				init();
			}
			
		});
	}

	public void init() {
	      jframe.setSize(new Dimension( 500, 500 ));
	      jframe.setTitle( "Ball dedication" );
	      jframe.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
	      jframe.setBackground( Color.WHITE );
	      jframe.add( this );
	      jframe.setResizable(false);
	      jframe.addWindowListener( new WindowAdapter() {
	    	  public void windowClosing(WindowEvent e) {
	    		  JOptionPane.showMessageDialog(null, "Game Over!", "Ball dedication", JOptionPane.INFORMATION_MESSAGE);
	    		  JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(Display.this);
	    		  topFrame.setVisible(false);
	    		  topFrame.dispose();
	    	  	  jframe.setVisible(false);
	    	      jframe.dispose();
	    	    }
	      });
	      //jframe.setVisible( true );
	   }
	
	public synchronized boolean isFrameVisible() { return jframe.isVisible(); }
	
	public void setFrameInvisible( ) {
		if( jframe.isVisible()) {
			JOptionPane.showMessageDialog(null, "Game Over!", "Ball dedication", JOptionPane.INFORMATION_MESSAGE);
			  JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(Display.this);
			  topFrame.setVisible(false);
			  topFrame.dispose();
		  	  jframe.setVisible(false);
		      jframe.dispose();
		}
	}
	
	public void setFramVisible() {
		jframe.setVisible( true );
	}
	
	public void setNewReceiver( Player player ) {
		receiver = player;
		try {
			Thread.sleep( 50 );
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		moveBall();
		
	}
	
	public void setNewOwner( Player player ) {
		owner = player;
	}
	
	private void moveBall( ) {
		ballFlying = true;
		while( !ballPos.equals( des ) ) {
			double x;
			double dis = distance() - ballSpeed;
			if ( dis < 0 ) dis = 0;
			if( des.getX() != ballPos.getX() ) {
				double incline = incline();
				if( des.getX() > ballPos.getX() ) x = des.getX() - dis / Math.sqrt( 1 + incline * incline );
				else x = des.getX() + dis / Math.sqrt( 1 + incline * incline );
				ballPos.setLocation( x , incline * ( x - des.getX() ) + des.getY() );
			}
			else {
				if( des.getY() > ballPos.getY() ) ballPos.setLocation( ballPos.getX() , des.getY() - dis );
				else ballPos.setLocation( ballPos.getX() , des.getY() + dis );
			}
			
			try {
				Thread.sleep( 50 );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		receiver.setOwner( );
		
		ballFlying = false;
		
	}
	
	public double incline( ) {
		return ( des.getY() - ballPos.getY() ) / ( des.getX() - ballPos.getX() );
	}
	
	public double distance( ) {
		return Math.sqrt( Math.pow(des.getY() - ballPos.getY(), 2) + Math.pow(des.getX() - ballPos.getX(), 2) );
	}
	
	public void setFlyingBallMode( boolean mode ) { ballFlying = mode; }
	
	public void paintComponent( Graphics g ) {
		super.paintComponent(g);
		if( !ballFlying ) {
			map.clear();
			int slices = players.getCount();
			int i = 0;
			playersIterator = players.iterator();
			while( playersIterator.hasNext() ) {
				Point p = new Point();
				Player pl = playersIterator.next();
				p.setLocation(center.x + radius * Math.cos( ( 2 * Math.PI / slices ) * i ) ,
						center.y + radius * Math.sin( ( 2 * Math.PI / slices ) * i++ ) ) ;
				if( pl == owner ) ballPos.setLocation( p.x , p.y  );
				else if( pl == receiver ) des.setLocation( p.x , p.y  );
				else g.setColor( Color.BLACK );
				map.put(pl.getNameP() , p);
			}
		}
		for( int name : map.keySet()  ) {
			Point point = map.get( name );
			if( name == owner.getNameP() ) g.setColor( Color.GREEN );
			else if( name == receiver.getNameP() ) g.setColor( Color.RED );
			else g.setColor( Color.BLACK );
			g.drawOval( point.x ,  point.y , 50, 30 );
			g.drawString( Integer.toString( name ), point.x + 20 , point.y + 20 );
		}
		g.setColor( Color.BLUE );
		g.fillOval( (int) ballPos.getX()  , (int) ballPos.getY()  , 30, 30);
		
		repaint();
	}

}
