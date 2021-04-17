package kane.genericGame;

import kane.math.Vec2f;
import kane.physics.Body;

public class Mob extends Body{
	private int health;
	private int maxHealth;
	
	private int invulnerabilityTime;
	
	public Mob(int posX, int posY, int maxHealth) {
		super(posX, posY);
		this.maxHealth = maxHealth;
		this.health = maxHealth;
	}
}
