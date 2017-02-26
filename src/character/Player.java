package character;

import abilityInterfaces.Ability;
import abilityInterfaces.AttackAbility;
import abilityInterfaces.BuffAbility;
import characterInterfaces.Listener;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class Player extends Character {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3349758486478712145L;

//	private final Image IMAGE = new Image("images/Player");
	
	private final int COLUMNS = 5;
	private final int COUNT = 10;
    private final int WIDTH = 32;
    private final int HEIGHT = 32;

	public Player(String name, int tileSheetNum) {
		super(name, tileSheetNum);
		// TODO Auto-generated constructor stub
	}
	
	public Player() {
		super();
	}
	
	public PlayerSummary getPlayerSummary(){
		return new PlayerSummary(this);
	}

	@Override
	public int takeDmg(int dmg) {
		// TODO Come up with final implementation
		hitPoints -= dmg;
		hitPoints = hitPoints < 0 ? 0 : hitPoints;
		hpProperty.set(hitPoints);
		return dmg;
	}

	@Override
	public int attack(Character target) {
		// TODO Come up with final implementation
		return 10;
	}

	public void ability(Ability ability, Character... targets) {

		if (ability instanceof AttackAbility) {
			for (int i = 0; i < targets.length; i++) {
				ability.use(targets[i]);
			}
		} else if (ability instanceof BuffAbility) {
			ability.use(this);
		}
		
	}
	
	public void move() {
//		 final ImageView imageView = new ImageView(IMAGE);
//	        imageView.setViewport(new Rectangle2D(WIDTH, HEIGHT, WIDTH, HEIGHT));

	}




}
