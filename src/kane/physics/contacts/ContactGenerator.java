package kane.physics.contacts;

import kane.physics.ShapePair;

public interface ContactGenerator {
//This is the interface for the differen Contact Generators.

	public void generate(ShapePair shapePair, ContactAcceptor acceptor);
}
