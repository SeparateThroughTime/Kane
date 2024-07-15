package kane.physics.contacts.generators;

import kane.physics.ShapePair;
import kane.physics.contacts.ContactAcceptor;
import kane.physics.contacts.ContactGenerator;

public class NoContactGenerator implements ContactGenerator{

    @Override
    public void generate(ShapePair shapePair, ContactAcceptor acceptor){
        shapePair.collideable = false;
    }

}
