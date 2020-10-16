package kane.physics.contacts;

import kane.physics.ShapePair;

/**
 * This is the interface for the different Contact Generators.
 */
public interface ContactGenerator {
	public void generate(ShapePair shapePair, ContactAcceptor acceptor);
}
