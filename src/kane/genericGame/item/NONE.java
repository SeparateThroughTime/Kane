package kane.genericGame.item;

import kane.genericGame.Item;

// This is used as a static outer-class. Therefore, that's not possible in java, I decided to write it uppercase...
// The only generation of this is in Inventory.
public class NONE extends Item {


	public NONE() {
        super("None", "json\\items\\none\\itemSpriteController.json", "json\\items\\none\\playerSpriteController.json");
        // addAmount adds the None-Item without collecting it to the inventory.
        addAmount(1);
    }

    @Override
	public void attack() {

	}

	@Override
	public void react() {

	}

	@Override
	public void jump() {

	}

	@Override
	public void move() {

	}

}
