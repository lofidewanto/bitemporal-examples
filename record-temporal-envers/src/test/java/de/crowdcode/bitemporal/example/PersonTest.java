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
@TransactionConfiguration(defaultRollback = false)
@Transactional
public class PersonTest {

	private final static Logger logger = LoggerFactory
			.getLogger(PersonTest.class);

	@Inject
	@Named("personService")
	private PersonService personService;

	@Inject
	@Named("addressService")
	private AddressService addressService;

	@Test
	public void testCreateRecordTemporalAddresses() {
		Person person = new PersonImpl();
		person.setLastname("Mueller");
		person.setFirstname("Hans");

		assertNull(person.getId());
		person = personService.createPerson(person);
		assertNotNull(person.getId());

		Address firstAddress = new AddressImpl();
		firstAddress.setPerson(person);
		firstAddress.setStreet("Hauptstr. 21");
		firstAddress.setCity("Koeln");
		firstAddress.setCode("50698");

		// First address
		assertNull(firstAddress.getId());
		Address createdAddress1 = addressService.createAddressWithPerson(
				firstAddress, person);
		assertNotNull(createdAddress1.getId());

		// Update person for the relation to the address
		Person updatedPerson = personService.findPersonById(person.getId());

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
		Address createdAddress2 = addressService.createAddressWithPerson(
				secondAddress, person);
		assertNotNull(createdAddress2.getId());

		Address thirdAddress = new AddressImpl();
		thirdAddress.setPerson(person);
		thirdAddress.setStreet("Aaasee str. 1");
		thirdAddress.setCity("Muenster");
		thirdAddress.setCode("43425");

		// Third address supersedes the first one
		// The person has only ONE current address
		assertNull(thirdAddress.getId());
		Address createdAddress3 = addressService.createAddressWithPerson(
				thirdAddress, person);
		assertNotNull(createdAddress3.getId());

		Address fourthAddress = new AddressImpl();
		fourthAddress.setPerson(person);
		fourthAddress.setStreet("Weyerstr. 1");
		fourthAddress.setCity("Solingen");
		fourthAddress.setCode("47144");

		// Fourth address supersedes the first one
		// The person has only ONE current address
		assertNull(fourthAddress.getId());
		Address createdAddress4 = addressService.createAddressWithPerson(
				fourthAddress, person);
		assertNotNull(createdAddress4.getId());

		// Update person for the relation to the address
		updatedPerson = personService.findPersonById(person.getId());

		// Assert
		Address secondCheckedAddress = updatedPerson.getAddress();
		assertEquals(fourthAddress.getCity(), secondCheckedAddress.getCity());

		Address secondCheckedAddressMethod = updatedPerson.address();
		assertEquals(fourthAddress.getCity(),
				secondCheckedAddressMethod.getCity());

		Address secondCheckedAddressAlive = updatedPerson.alive();
		assertEquals(fourthAddress.getCity(),
				secondCheckedAddressAlive.getCity());

		// Assert amount of object
		// One person and four addresses but the person has only one current
		// address
		Integer amountOfPerson = personService.getAmountOfPerson();
		assertEquals(1, amountOfPerson.intValue());
		Integer amountOfAddress = addressService.getAmountOfAddress();
		assertEquals(4, amountOfAddress.intValue());

		Address currentAddress = updatedPerson.getAddress();
		assertEquals("Solingen", currentAddress.getCity());

		// Show in logger
		Collection<Person> persons = personService.findAllPersons();
		for (Person person2 : persons) {
			logger.info("XXX - Person.firstname: " + person2.getFirstname());
			logger.info("XXX - Person.address.city: "
					+ person2.getAddress().getCity());
		}
		Collection<Address> addresses = addressService.findAllAddresses();
		for (Address address : addresses) {
			logger.info("YYY - Address.city: " + address.getCity());
			logger.info("YYY - Address.person.firstname: "
					+ address.getPerson().getFirstname());
		}
	}

	@Test
	public void testEmptyAddresses() {
		// No address object because we make a rollback
		Integer amountOfAddress = addressService.getAmountOfAddress();
		assertEquals(4, amountOfAddress.intValue());
	}

	@Test
	public void testAuditedAmountOfAddresses1() {
		// Audit information...
		Collection<Address> auditedAddresses = addressService
				.findAuditedAdressesWithRevision(1);

		assertEquals(4, auditedAddresses.size());

		for (Address address : auditedAddresses) {
			logger.info("YYY - Address.city: " + address.getCity());
			logger.info("YYY - Address.street: " + address.getStreet());
			logger.info("YYY - Address.person.firstname: "
					+ address.getPerson().getFirstname());
		}
	}

	@Test
	public void testAddSomeAddresses() {
		Person person = personService.findPersonByLastname("Mueller");

		Address firstAddress = new AddressImpl();
		firstAddress.setPerson(person);
		firstAddress.setStreet("Burgstr. 21");
		firstAddress.setCity("Hamburg");
		firstAddress.setCode("11003");

		assertNull(firstAddress.getId());
		Address createdAddress1 = addressService.createAddressWithPerson(
				firstAddress, person);
		assertNotNull(createdAddress1.getId());
	}

	@Test
	public void testUpdateSomeAddresses() {
		Person person = personService.findPersonByLastname("Mueller");

		Address address = person.getAddress();
		address.setStreet("Hamburgstr. 1");

		addressService.updateAddress(address);
	}

	@Test
	public void testAuditedAmountOfAddresses2() {
		// Audit information...
		Collection<Address> auditedAddresses = addressService
				.findAuditedAdressesWithRevision(2);

		assertEquals(5, auditedAddresses.size());
	}

	@Test
	public void testAuditedAddressesRevision1() {
		Person person = personService.findPersonByLastname("Mueller");
		Address address = person.getAddress();

		Long addressId = address.getId();
		assertEquals(4L, addressId.longValue());

		String result = addressService
				.findRevisionNumberByAddressIdAndRevisionNumber(addressId, 1);
		assertEquals("2", result);
	}

	@Test
	public void testAddSomeAddresses2() {
		Person person = personService.findPersonByLastname("Mueller");

		Address firstAddress = new AddressImpl();
		firstAddress.setPerson(person);
		firstAddress.setStreet("Deutschstr. 21");
		firstAddress.setCity("Hannover");
		firstAddress.setCode("30159");

		assertNull(firstAddress.getId());
		Address createdAddress1 = addressService.createAddressWithPerson(
				firstAddress, person);
		assertNotNull(createdAddress1.getId());
	}

	@Test
	public void testAuditedAddressesRevision2() {
		Person person = personService.findPersonByLastname("Mueller");
		Address address = person.getAddress();

		Long addressId = address.getId();
		assertEquals(5L, addressId.longValue());

		String result = addressService
				.findRevisionNumberByAddressIdAndRevisionNumber(addressId, 2);
		assertEquals("3", result);
	}

	@Test
	public void testAuditedAddressesChangedByRevision1() {
		Collection<Address> addresses = addressService
				.findAddressesChangedByRevisionNumber(1);
		logger.info("testAuditedAddressesChangedByRevision1: ...");
		for (Address address2 : addresses) {
			logger.info("Address.street: " + address2.getStreet());
			logger.info("Address.code: " + address2.getCode());
			logger.info("Address.city: " + address2.getCity());
			assertEquals("Burgstr. 21", address2.getStreet());
		}

		assertEquals(1, addresses.size());
	}

	@Test
	public void testAuditedAddressesChangedByRevision2() {
		Collection<Address> addresses = addressService
				.findAddressesChangedByRevisionNumber(2);
		logger.info("testAuditedAddressesChangedByRevision2: ...");
		for (Address address2 : addresses) {
			logger.info("Address.street: " + address2.getStreet());
			logger.info("Address.code: " + address2.getCode());
			logger.info("Address.city: " + address2.getCity());
			assertEquals("Hamburgstr. 1", address2.getStreet());
		}

		assertEquals(1, addresses.size());
	}

	@Test
	public void testAuditedAddressesChangedByRevision3() {
		Collection<Address> addresses = addressService
				.findAddressesChangedByRevisionNumber(3);
		logger.info("testAuditedAddressesChangedByRevision3: ...");
		for (Address address2 : addresses) {
			logger.info("Address.street: " + address2.getStreet());
			logger.info("Address.code: " + address2.getCode());
			logger.info("Address.city: " + address2.getCity());
			assertEquals("Deutschstr. 21", address2.getStreet());
		}

		assertEquals(1, addresses.size());
	}
}
