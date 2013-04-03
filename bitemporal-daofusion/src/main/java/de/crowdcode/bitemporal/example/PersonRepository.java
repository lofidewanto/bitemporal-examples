package de.crowdcode.bitemporal.example;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named("PersonRepository")
public class PersonRepository {

	@PersistenceContext
	private EntityManager em;

	public Person save(Person person) {
		em.persist(person);
		return person;
	}
}
