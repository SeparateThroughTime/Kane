package kane.genericGame;

import java.util.HashMap;

import kane.genericGame.gameEvent.mob.WalkAwayFromPos;
import kane.genericGame.gameEvent.mob.GumbaWalk;
import kane.genericGame.gameEvent.mob.Jump;
import kane.genericGame.gameEvent.mob.WalkingLeft;
import kane.genericGame.gameEvent.mob.WalkingRight;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.renderer.SpriteState;

public class Mob extends Body {
	private static final int INVULNERABILITY_TIME = 30;

	private int health;
	private int maxHealth;
	private int damage;
	private Vec2f walkAcc;
	private int walkSpeed;
	private boolean canJump;
	private Vec2f jumpAcc;
	private HashMap<MobActions, Boolean> activeActions;
	private WalkingLeft currentWalkingLeftEvent;
	private WalkingRight currentWalkingRightEvent;
	private GameEvent currentWalkingAI;
	private AIs ai;

	public HashMap<MobActions, Boolean> getActiveActions() {
		return activeActions;
	}

	private Game g;

	private int invulnerabilityCooldown;

	public Mob(Game g, int posX, int posY, int maxHealth, int damage) {
		super(posX, posY);
		this.setMaxHealth(maxHealth);
		this.health = maxHealth;
		this.setDamage(damage);
		this.g = g;
		canJump = true;
		walkAcc = new Vec2f();

		activeActions = new HashMap<MobActions, Boolean>();
		activeActions.put(MobActions.WALK_LEFT, false);
		activeActions.put(MobActions.WALK_RIGHT, false);
		activeActions.put(MobActions.JUMPING, false);
		activeActions.put(MobActions.ATTACKING, false);
		activeActions.put(MobActions.GUMBA_WALK, false);
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

			if (ai != null) {
				switch (ai) {
				case GUMBA:
					g.addEvent(new WalkAwayFromPos(g, this, attackersPos));
				default:
					break;
				}
			}
		}

	}

	private void bump(Vec2f attackersPos) {
		float attackersRelPosX = attackersPos.getX() - pos.getX();
		if (attackersRelPosX < 0) {
			vel.set(200, 100);
		} else {
			vel.set(-200, 100);
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
		this.walkAcc.set(walkAcc);
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
	
	private void killWalkingEvents() {
		if (activeActions.get(MobActions.WALK_RIGHT)) {
			currentWalkingRightEvent.killEvent();
		}
		if (activeActions.get(MobActions.WALK_LEFT)) {
			currentWalkingLeftEvent.killEvent();
		}
	}

	public void walkRight() {
		killWalkingEvents();
		currentWalkingRightEvent = new WalkingRight(g, this);
		g.addEvent(currentWalkingRightEvent);
	}

	public void stopWalkRight() {
		if (currentWalkingRightEvent != null) {
			currentWalkingRightEvent.killEvent();
		}
	}
	
	
	public void walkLeft() {
		killWalkingEvents();
		currentWalkingLeftEvent = new WalkingLeft(g, this);
		g.addEvent(currentWalkingLeftEvent);
	}

	public void stopWalkLeft() {
		if (currentWalkingLeftEvent != null) {
			currentWalkingLeftEvent.killEvent();
		}
	}

	public void jump() {
		if (canJump) {
			g.addEvent(new Jump(g, this));
		}
	}

	public boolean getCanJump() {
		return canJump;
	}

	public void setCanJump(boolean canJump) {
		this.canJump = canJump;
	}

	public void startWalkingAI() {
		switch (ai) {
		case GUMBA:
			g.addEvent(new GumbaWalk(g, this));
			break;

		default:
			break;
		}
	}

	public void refreshSpriteStates() {
		if (activeActions.get(MobActions.STAND_LEFT)) {
			setCurrentSpriteState(SpriteState.STANDING_LEFT);
		} else if (activeActions.get(MobActions.STAND_RIGHT)) {
			setCurrentSpriteState(SpriteState.STANDING_RIGHT);
		} else if (activeActions.get(MobActions.WALK_LEFT)) {
			setCurrentSpriteState(SpriteState.RUNNING_LEFT);
		} else if (activeActions.get(MobActions.WALK_RIGHT)) {
			setCurrentSpriteState(SpriteState.RUNNING_RIGHT);
		}
	}

	public void setAI(AIs ai) {
		this.ai = ai;
		startWalkingAI();
	}
}
