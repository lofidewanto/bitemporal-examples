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

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.anasoft.os.daofusion.bitemporal.TimeUtils;

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

	@Inject
	@Named("personService")
	private PersonService personService;

	@Inject
	@Named("addressService")
	private AddressService addressService;

	@After
	public void tearDown() throws Exception {
		TimeUtils.clearReference();
	}

	@Test
	public void testCreateBitemporalAddresses() {
		Person person = new PersonImpl();
		person.setLastname("Mueller");
		person.setFirstname("Hans");

		assertNull(person.getId());
		Person createdPerson = personService.createPerson(person);
		assertNotNull(createdPerson.getId());

		Address firstAddress = new AddressImpl();
		firstAddress.setPerson(person);
		firstAddress.setStreet("Koeln 21");
		firstAddress.setCity("Koeln");
		firstAddress.setCode("50698");

		// First Address will be valid from now on (1-Jan-2010 .. end_of_time)
		// Known on...
		addressService.setTimeReference(TimeUtils.day(1, 1, 2010));

		assertNull(firstAddress.getId());
		Address createdAddress1 = addressService.createAddressWithPerson(firstAddress, createdPerson);
		assertNotNull(createdAddress1.getId());

		Address secondAddress = new AddressImpl();
		secondAddress.setPerson(person);
		secondAddress.setStreet("Berlin 22");
		secondAddress.setCity("Berlin");
		secondAddress.setCode("10313");

		// Second Address supersedes the first one:
		// - First Address valid in [1-Jan-2010 .. 10-Feb-2010]
		// - Second Address valid in [10-Feb-2010 .. end_of_time]
		assertNull(secondAddress.getId());
		Address createdAddress2 = addressService.createAddressWithPerson(secondAddress, createdPerson,
				TimeUtils.from(TimeUtils.day(10, 2, 2010)));
		assertNotNull(createdAddress2.getId());

		Address thirdAddress = new AddressImpl();
		thirdAddress.setPerson(person);
		thirdAddress.setStreet("Muenster 12");
		thirdAddress.setCity("Muenster");
		thirdAddress.setCode("43744");

		// Third Address supersedes the second one but known later:
		// - First Address valid in [1-Jan-2010 .. 10-Feb-2010]
		// - Second Address valid in [10-Feb-2010 .. 13-July-2010]
		// - Third Address valid in [13-July-2010 .. end_of_time]
		// Known on...
		addressService.setTimeReference(TimeUtils.day(27, 7, 2010));

		assertNull(thirdAddress.getId());
		Address createdAddress3 = addressService.createAddressWithPerson(thirdAddress, createdPerson,
				TimeUtils.from(TimeUtils.day(13, 7, 2010)));
		assertNotNull(createdAddress3.getId());

		// Update person for the relation to the address
		Person updatedPerson = personService.findPersonById(createdPerson.getId());

		// Doing some asserts for the scenes...
		Address addressValue1 = personService.findAddressByPersonIdOnDates(updatedPerson.getId(),
				TimeUtils.day(3, 2, 2010), TimeUtils.day(1, 1, 2010));
		assertEquals("Koeln", addressValue1.getCity());

		Address addressValue2 = personService.findAddressByPersonIdOnDates(updatedPerson.getId(),
				TimeUtils.day(10, 7, 2010), TimeUtils.day(1, 1, 2010));
		assertEquals("Berlin", addressValue2.getCity());

		// Known on 1-Jan-2010
		Address addressValue3 = personService.findAddressByPersonIdOnDates(updatedPerson.getId(),
				TimeUtils.day(15, 7, 2010), TimeUtils.day(1, 1, 2010));
		assertEquals("Berlin", addressValue3.getCity());

		// Known on from 27-July-2010
		Address addressValue4 = personService.findAddressByPersonIdOnDates(updatedPerson.getId(),
				TimeUtils.day(15, 7, 2010), TimeUtils.day(31, 7, 2010));
		assertEquals("Muenster", addressValue4.getCity());
	}
}
