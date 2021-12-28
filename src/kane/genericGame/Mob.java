package kane.genericGame;

import java.util.HashMap;

import kane.genericGame.gameEvent.WalkingLeft;
import kane.genericGame.gameEvent.WalkingRight;
import kane.math.Vec2f;
import kane.physics.Body;

public class Mob extends Body {
	private static final int INVULNERABILITY_TIME = 30;

	private int health;
	private int maxHealth;
	private int damage;
	private Vec2f walkAcc;
	private int walkSpeed;
	private Vec2f jumpAcc;
	private HashMap<MobActions, Boolean> activePlayerActions;
	private Game g;

	private int invulnerabilityCooldown;

	public Mob(Game g, int posX, int posY, int maxHealth, int damage) {
		super(posX, posY);
		this.setMaxHealth(maxHealth);
		this.health = maxHealth;
		this.setDamage(damage);
		this.g = g;
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

	public Vec2f getWalkAcc() {
		return walkAcc;
	}

	public void setWalkAcc(Vec2f walkAcc) {
		this.walkAcc = walkAcc;
	}

	public int getWalkSpeed() {
		return walkSpeed;
	}

	public void setWalkSpeed(int wlakSpeed) {
		this.walkSpeed = wlakSpeed;
	}

	public Vec2f getJumpAcc() {
		return jumpAcc;
	}

	public void setJumpAcc(Vec2f jumpAcc) {
		this.jumpAcc = jumpAcc;
	}
	
	public void walkRight() {
		g.addEvent(new WalkingRight(g, this));
	}
	
	public void walkLeft() {
		g.addEvent(new WalkingLeft(g, this));
	}
}
