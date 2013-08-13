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
import org.springframework.transaction.annotation.Propagation;
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
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional(propagation = Propagation.REQUIRED)
public class PersonTest {

	private final static Logger logger = LoggerFactory.getLogger(PersonTest.class);

	@Inject
	@Named("personService")
	private PersonService personService;

	@Inject
	@Named("addressService")
	private AddressService addressService;

	@Test
	public void testCreateNonTemporalAddress() {
		PersonImpl person = new PersonImpl();
		person.setLastname("Mueller");
		person.setFirstname("Hans");

		personService.createPerson(person);

		Address firstAddress = new AddressImpl();
		firstAddress.setPerson(person);
		firstAddress.setStreet("Hauptstr. 21");
		firstAddress.setCity("Koeln");
		firstAddress.setCode("50698");

		addressService.createAddress(firstAddress);

		// First address
		person.setAddress(firstAddress);

		// Assert
		Address firstCheckedAddress = person.getAddress();
		assertEquals(firstAddress, firstCheckedAddress);

		Address secondAddress = new AddressImpl();
		secondAddress.setPerson(person);
		secondAddress.setStreet("Grossmarkt 22");
		secondAddress.setCity("Berlin");
		secondAddress.setCode("10313");

		addressService.createAddress(secondAddress);

		// Second address supersedes the first one
		// The person has only ONE current address
		person.setAddress(secondAddress);

		// Assert
		Address secondCheckedAddress = person.getAddress();
		assertEquals(secondAddress, secondCheckedAddress);
		assertEquals(secondAddress.getCity(), secondCheckedAddress.getCity());

		Address secondCheckedAddressMethod = person.address();
		assertEquals(secondAddress, secondCheckedAddressMethod);
		assertEquals(secondAddress.getCity(), secondCheckedAddressMethod.getCity());

		Address secondCheckedAddressAlive = person.alive();
		assertEquals(secondAddress, secondCheckedAddressAlive);
		assertEquals(secondAddress.getCity(), secondCheckedAddressAlive.getCity());

		// Assert amount of object
		// One person and two addresses but the person has only one current address
		Integer amountOfPerson = personService.getAmountOfPerson();
		assertEquals(1, amountOfPerson.intValue());
		Integer amountOfAddress = addressService.getAmountOfAddress();
		assertEquals(2, amountOfAddress.intValue());

		Address currentAddress = person.getAddress();
		assertEquals("Berlin", currentAddress.getCity());

		// Show in logger
		Collection<Person> persons = personService.findAllPersons();
		for (Person person2 : persons) {
			logger.info("XXX - Person: " + person2.getFirstname());
			logger.info("XXX - Person.address: " + person2.getAddress().getCity());
		}
		Collection<Address> addresses = addressService.findAllAddresses();
		for (Address address : addresses) {
			logger.info("YYY - Address: " + address.getCity());
			logger.info("YYY - Address.person: " + address.getPerson().getFirstname());
		}
	}

	@Test
	public void testEmptyAddresses() {
		// No address object because we make a rollback
		Integer amountOfAddress = addressService.getAmountOfAddress();
		assertEquals(0, amountOfAddress.intValue());
	}
}
