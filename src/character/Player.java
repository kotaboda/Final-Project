package character;

import characterInterfaces.Listener;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Player extends Character {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3349758486478712145L;

	private final Image IMAGE = new Image("images/Player");
	
	private final int COLUMNS = 5;
	private final int COUNT = 10;
    private final int WIDTH = 32;
    private final int HEIGHT = 32;

	public Player(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int takeDmg(int dmg) {
		//TODO Come up with final implementation
		hitPoints -= dmg;
		hitPoints = hitPoints < 0 ? 0 : hitPoints;
		return dmg;
	}

	@Override
	public int attack() {
		// TODO Come up with final implementation
		return 10;
	}
	
	public int attack(AttackType TYPE){
		//TODO Come up with final implementation
		switch(TYPE){
		
		}
		
		return 15;
	}
	
	public void move() {
		 final ImageView imageView = new ImageView(IMAGE);
	        imageView.setViewport(new Rectangle2D(WIDTH, HEIGHT, WIDTH, HEIGHT));

	        final Animation animation = new SpriteAnimation(
	                imageView,
	                Duration.millis(1000),
	                COUNT, COLUMNS,
	                WIDTH, HEIGHT
	        );
	        animation.setCycleCount(1);
	        animation.play();
	}

	@Override
	public void addSubscriber(Listener<Character> sub) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSubscriber(Listener<Character> sub) {
		// TODO Auto-generated method stub
		
	}

}
