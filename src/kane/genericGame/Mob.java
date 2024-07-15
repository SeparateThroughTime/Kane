package kane.genericGame;

import java.util.HashMap;

import kane.genericGame.gameEvent.mob.*;
import kane.math.Vec2f;
import kane.physics.Body;
import kane.renderer.SpriteState;

import static kane.Kane.GAME;

public class Mob extends Body{
    private static final int INVULNERABILITY_TIME = 30;

    private int health;
    private int maxHealth;
    private int damage;
    private final Vec2f walkAcc;
    private int walkSpeed;
    private boolean onGround;
    private Vec2f jumpAcc;
    private final HashMap<MobActions, Boolean> activeActions;
    private WalkingLeft currentWalkingLeftEvent;
    private WalkingRight currentWalkingRightEvent;
    private AIs ai;
    private GameEvent walkingAI;
    private MobDirection direction;

    public void putActiveActions(MobActions action, boolean val){
        activeActions.put(action, val);
        refreshSpriteStates();
    }

    private int invulnerabilityCooldown;

    public Mob(int posX, int posY, int maxHealth, int damage, MobDirection direction){
        super(posX, posY);
        this.setMaxHealth(maxHealth);
        this.health = maxHealth;
        this.setDamage(damage);
        this.direction = direction;
        onGround = true;
        walkAcc = new Vec2f();

        activeActions = new HashMap<>();
        activeActions.put(MobActions.WALK, false);
        activeActions.put(MobActions.JUMPING, false);
        activeActions.put(MobActions.ATTACKING, false);
        activeActions.put(MobActions.FALLING, false);
        activeActions.put(MobActions.HIT, false);
        activeActions.put(MobActions.GUMBA_WALK, false);
        activeActions.put(MobActions.STAND, true);
    }

    public int getHealth(){
        return health;
    }

    public void addHealth(int amount){
        health += amount;
        checkDeath();
    }

    private void checkDeath(){
        if (health <= 0){
            GAME.addEvent(new Death(this));
        }
    }

    public void reduceHealth(int amount){
        health -= amount;
        checkDeath();
    }

    public int getMaxHealth(){
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth){
        this.maxHealth = maxHealth;
    }

    public int getDamage(){
        return damage;
    }

    public void setDamage(int damage){
        this.damage = damage;
    }

    public void hit(int damage, Vec2f attackersPos){
        if (invulnerabilityCooldown == 0){
            reduceHealth(damage);
            invulnerabilityCooldown = INVULNERABILITY_TIME;
            bump(attackersPos);

            if (ai != null){
                switch (ai){
                    case GUMBA:
                        GAME.addEvent(new WalkAwayFromPos(this, attackersPos));
                    default:
                        break;
                }
            }
        }

    }

    private void bump(Vec2f attackersPos){
        float attackersRelPosX = attackersPos.x - pos.x;
        if (attackersRelPosX < 0){
            vel.set(200, 100);
        } else{
            vel.set(-200, 100);
        }
    }

    public void invulnerabilityCooldown(){
        if (invulnerabilityCooldown > 0){
            invulnerabilityCooldown--;
        }
    }

    public Vec2f getWalkAcc(){
        return walkAcc;
    }

    public void setWalkAcc(Vec2f walkAcc){
        this.walkAcc.set(walkAcc);
    }

    public int getWalkSpeed(){
        return walkSpeed;
    }

    public void setWalkSpeed(int wlakSpeed){
        this.walkSpeed = wlakSpeed;
    }

    public Vec2f getJumpAcc(){
        return jumpAcc;
    }

    public void setJumpAcc(Vec2f jumpAcc){
        this.jumpAcc = jumpAcc;
    }

    private void killWalkingEvents(){
        if (currentWalkingRightEvent != null){
            currentWalkingRightEvent.killEvent();
        }
        if (currentWalkingLeftEvent != null){
            currentWalkingLeftEvent.killEvent();
        }
    }

    public void walkRight(){
        killWalkingEvents();
        currentWalkingRightEvent = new WalkingRight(this);
        GAME.addEvent(currentWalkingRightEvent);
    }

    public void stopWalkRight(){
        if (currentWalkingRightEvent != null){
            currentWalkingRightEvent.killEvent();
        }
    }

    public void walkLeft(){
        killWalkingEvents();
        currentWalkingLeftEvent = new WalkingLeft(this);
        GAME.addEvent(currentWalkingLeftEvent);
    }

    public void stopWalkLeft(){
        if (currentWalkingLeftEvent != null){
            currentWalkingLeftEvent.killEvent();
        }
    }

    public void jump(){
        if (onGround){
            GAME.addEvent(new Jump(this));
        }
    }

    public boolean isOnGround(){
        return onGround;
    }

    public void setOnGround(boolean canJump){
        this.onGround = canJump;
    }

    public void startWalkingAI(){
        switch (ai){
            case GUMBA:
                walkingAI = new GumbaWalk(this);
                GAME.addEvent(walkingAI);
                break;

            default:
                break;
        }
    }

    public void refreshSpriteStates(){
        if (direction.equals(MobDirection.LEFT)){
            if (activeActions.get(MobActions.STAND)){
                setCurrentSpriteState(SpriteState.STANDING_LEFT);
            } else if (activeActions.get(MobActions.WALK)){
                setCurrentSpriteState(SpriteState.RUNNING_LEFT);
            }

            if (activeActions.get(MobActions.JUMPING)){
                setCurrentSpriteState(SpriteState.JUMP_LEFT);
            } else if (activeActions.get(MobActions.FALLING)){
                setCurrentSpriteState(SpriteState.FALL_LEFT);
            }

            if (activeActions.get(MobActions.ATTACKING)){
                setCurrentSpriteState(SpriteState.ATTACK_LEFT);
            }

            if (activeActions.get(MobActions.HIT)){
                setCurrentSpriteState(SpriteState.HIT_LEFT);
            }

        } else if (direction.equals(MobDirection.RIGHT)){

            if (activeActions.get(MobActions.STAND)){
                setCurrentSpriteState(SpriteState.STANDING_RIGHT);
            } else if (activeActions.get(MobActions.WALK)){
                setCurrentSpriteState(SpriteState.RUNNING_RIGHT);
            }

            if (activeActions.get(MobActions.JUMPING)){
                setCurrentSpriteState(SpriteState.JUMP_RIGHT);
            } else if (activeActions.get(MobActions.FALLING)){
                setCurrentSpriteState(SpriteState.FALL_RIGHT);
            }

            if (activeActions.get(MobActions.ATTACKING)){
                setCurrentSpriteState(SpriteState.ATTACK_RIGHT);
            }

            if (activeActions.get(MobActions.HIT)){
                setCurrentSpriteState(SpriteState.HIT_RIGHT);
            }
        }
    }

    public void setAI(AIs ai){
        this.ai = ai;
        startWalkingAI();
    }

    public MobDirection getDirection(){
        return direction;
    }

    public void setDirection(MobDirection direction){
        this.direction = direction;
        refreshSpriteStates();
    }

    public void remove(){
        super.remove();
        if (walkingAI != null){
            walkingAI.killEvent();
        }
    }
}
