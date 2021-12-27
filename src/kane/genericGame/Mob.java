package kane.genericGame;

import kane.math.Vec2f;
import kane.physics.Body;

public class Mob extends Body {
	private static final int INVULNERABILITY_TIME = 30;

	private int health;
	private int maxHealth;
	private int damage;

	private int invulnerabilityCooldown;

	public Mob(int posX, int posY, int maxHealth, int damage) {
		super(posX, posY);
		this.setMaxHealth(maxHealth);
		this.health = maxHealth;
		this.setDamage(damage);
	}

	public int getHealth() {
		return health;
	}

	public void addHealth(int amount) {
		health += amount;
		checkDeath();
	}

	private void checkDeath() {
		if (health <= 0) {
			kill();
		}
	}

	public void kill() {
		remove();
	}

	public void reduceHealth(int amount) {
		health -= amount;
		checkDeath();
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public void hit(int damage, Vec2f attackersPos) {
		if (invulnerabilityCooldown == 0) {
			reduceHealth(damage);
			invulnerabilityCooldown = INVULNERABILITY_TIME;
			bump(attackersPos);
		}
	}

	private void bump(Vec2f attackersPos) {
		float attackersRelPosX = attackersPos.getX() - pos.getX();
		if (attackersRelPosX < 0) {
			acc.add(200 / Game.DELTATIME, 200 / Game.DELTATIME);
		} else {
			acc.add(-200 / Game.DELTATIME, 200 / Game.DELTATIME);
		}
	}
	
	public void invulnerabilityCooldown() {
		if (invulnerabilityCooldown > 0) {
			invulnerabilityCooldown--;
		}
	}
}
