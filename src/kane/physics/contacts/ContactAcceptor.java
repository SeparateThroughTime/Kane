package kane.physics.contacts;

/**
 * This adds a criteria for Contact Generators if a contact is created or not.
 * This criteria is inside of the Contact Solver, because the criteria is depending on the Solver.
 */
public interface ContactAcceptor {
	boolean accept(Contact contact);
}
