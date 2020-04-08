package kane.physics.contacts.generators;

import kane.physics.ShapePair;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;

public class NoContactGenerator implements ContactGenerator{
//This is a dummy for body types which cannot collide (i.e. 2 Planes)

	@Override
	public void generate(ShapePair shapePair, ContactAcceptor acceptor) {
		shapePair.setCollideable(false);
	}

}
