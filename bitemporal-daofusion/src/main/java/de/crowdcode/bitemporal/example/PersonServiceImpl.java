package de.crowdcode.bitemporal.example;

import javax.inject.Inject;
import javax.inject.Named;

@Named("PersonServiceImpl")
public class PersonServiceImpl implements PersonService {

	@Inject
	@Named("PersonRepository")
	private PersonRepository personRepository;

	public Person createPerson(PersonImpl person) {
		Person personCreated = personRepository.save(person);
		return personCreated;
	}

	@Override
	public Person createPerson() {
		// TODO Auto-generated method stub
		return null;
	}
}
