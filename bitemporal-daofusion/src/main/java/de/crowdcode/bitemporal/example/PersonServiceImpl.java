/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.crowdcode.bitemporal.example;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.anasoft.os.daofusion.bitemporal.WrappedBitemporalProperty;

@Named("personService")
public class PersonServiceImpl implements PersonService {

	@Inject
	@Named("personRepository")
	private PersonRepository personRepository;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Person createPerson(Person person) {
		Person personCreated = personRepository.save(person);
		return personCreated;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Integer getAmountOfPerson() {
		Integer amount = personRepository.getAmount();
		return amount;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Collection<Person> findAllPersons() {
		Collection<Person> persons = personRepository.findAll();
		return persons;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Person findPersonById(Long id) {
		Person person = personRepository.findById(id);
		return person;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Address findAddressByPersonIdOnDates(Long personId, DateTime validOn, DateTime knownOn) {
		Person person = personRepository.findById(personId);
		Address address = (Address) person.address().on(validOn, knownOn);
		return address;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Person createPerson(Person person, Boolean isAlive, Interval interval) {
		Person personCreated = personRepository.save(person);
		personCreated.alive().set(isAlive, interval);
		return personCreated;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setAliveByPerson(Person person, Boolean isAlive, Interval interval) {
		Person personFound = personRepository.findById(person.getId());
		personFound.alive().set(isAlive, interval);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setAliveByPerson(Person person, Boolean isAlive) {
		Person personFound = personRepository.findById(person.getId());
		personFound.alive().set(isAlive);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Boolean isAliveOnByPerson(Person person, DateTime validOn) {
		Person personFound = personRepository.findById(person.getId());
		Boolean isAlive = (Boolean) personFound.alive().on(validOn);
		return isAlive;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Boolean isAliveOnByPerson(Person person, DateTime validOn, DateTime knownOn) {
		Person personFound = personRepository.findById(person.getId());
		Boolean isAlive = (Boolean) personFound.alive().on(validOn, knownOn);
		return isAlive;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public WrappedBitemporalProperty<Boolean, BitemporalBooleanImpl> getAlivesByPerson(Person person) {
		Person personFound = personRepository.findById(person.getId());
		return personFound.alive();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public WrappedBitemporalProperty<Address, BitemporalAddressImpl> getAdressesByPerson(Person person) {
		Person personFound = personRepository.findById(person.getId());
		return personFound.address();
	}
}
