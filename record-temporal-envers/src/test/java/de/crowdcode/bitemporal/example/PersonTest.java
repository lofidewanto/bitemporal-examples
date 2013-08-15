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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Unit test for PersonImpl class.
 * 
 * @author Lofi Dewanto
 * @since 1.0.0
 * @version 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/beans.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class PersonTest {

	private final static Logger logger = LoggerFactory.getLogger(PersonTest.class);

	@Inject
	@Named("personService")
	private PersonService personService;

	@Inject
	@Named("addressService")
	private AddressService addressService;

	@Test
	public void testCreateNonTemporalAddresses() {
		Person person = new PersonImpl();
		person.setLastname("Mueller");
		person.setFirstname("Hans");

		assertNull(person.getId());
		Person createdPerson = personService.createPerson(person);
		assertNotNull(createdPerson.getId());

		Address firstAddress = new AddressImpl();
		firstAddress.setPerson(person);
		firstAddress.setStreet("Hauptstr. 21");
		firstAddress.setCity("Koeln");
		firstAddress.setCode("50698");

		// First address
		assertNull(firstAddress.getId());
		Address createdAddress1 = addressService.createAddressWithPerson(firstAddress, createdPerson);
		assertNotNull(createdAddress1.getId());

		// Update person for the relation to the address
		Person updatedPerson = personService.findPersonById(createdPerson.getId());

		// Assert
		Address firstCheckedAddress = updatedPerson.getAddress();
		assertEquals(firstAddress.getCity(), firstCheckedAddress.getCity());

		Address secondAddress = new AddressImpl();
		secondAddress.setPerson(person);
		secondAddress.setStreet("Grossmarkt 22");
		secondAddress.setCity("Berlin");
		secondAddress.setCode("10313");

		// Second address supersedes the first one
		// The person has only ONE current address
		assertNull(secondAddress.getId());
		Address createdAddress2 = addressService.createAddressWithPerson(secondAddress, createdPerson);
		assertNotNull(createdAddress2.getId());

		// Update person for the relation to the address
		updatedPerson = personService.findPersonById(createdPerson.getId());

		// Assert
		Address secondCheckedAddress = updatedPerson.getAddress();
		assertEquals(secondAddress.getCity(), secondCheckedAddress.getCity());

		Address secondCheckedAddressMethod = updatedPerson.address();
		assertEquals(secondAddress.getCity(), secondCheckedAddressMethod.getCity());

		Address secondCheckedAddressAlive = updatedPerson.alive();
		assertEquals(secondAddress.getCity(), secondCheckedAddressAlive.getCity());

		// Assert amount of object
		// One person and two addresses but the person has only one current address
		Integer amountOfPerson = personService.getAmountOfPerson();
		assertEquals(1, amountOfPerson.intValue());
		Integer amountOfAddress = addressService.getAmountOfAddress();
		assertEquals(2, amountOfAddress.intValue());

		Address currentAddress = updatedPerson.getAddress();
		assertEquals("Berlin", currentAddress.getCity());

		// Show in logger
		Collection<Person> persons = personService.findAllPersons();
		for (Person person2 : persons) {
			logger.info("XXX - Person.firstname: " + person2.getFirstname());
			logger.info("XXX - Person.address.city: " + person2.getAddress().getCity());
		}
		Collection<Address> addresses = addressService.findAllAddresses();
		for (Address address : addresses) {
			logger.info("YYY - Address.city: " + address.getCity());
			logger.info("YYY - Address.person.firstname: " + address.getPerson().getFirstname());
		}
	}

	@Test
	public void testEmptyAddresses() {
		// No address object because we make a rollback
		Integer amountOfAddress = addressService.getAmountOfAddress();
		assertEquals(0, amountOfAddress.intValue());
	}
}
