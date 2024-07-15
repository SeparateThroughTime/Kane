package kane.physics.contacts;

import kane.physics.ShapePair;

public interface ContactGenerator{
    void generate(ShapePair shapePair, ContactAcceptor acceptor);
}
