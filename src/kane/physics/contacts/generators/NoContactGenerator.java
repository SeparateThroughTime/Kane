package kane.physics.contacts.generators;

import kane.physics.ShapePair;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;

/**
 * This is a dummy for body types which cannot collide (i.e. 2 Planes)
 */
public class NoContactGenerator implements ContactGenerator{

	@Override
	public void generate(ShapePair shapePair, ContactAcceptor acceptor) {
		shapePair.setCollideable(false);
	}

}
