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

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
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
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional(propagation = Propagation.REQUIRED)
public class PersonTest {

	@Inject
	@Named("personService")
	private PersonService personService;

	@Inject
	@Named("addressService")
	private AddressService addressService;

	@Test
	public void testCreateBitemporalAddress() {
		PersonImpl person = new PersonImpl();
		person.setLastname("Mueller");
		person.setFirstname("Hans");

		personService.createPerson(person);

		Address firstAddress = new AddressImpl();
		firstAddress.setPerson(person);
		firstAddress.setStreet("Koeln 21");
		firstAddress.setCity("Koeln");
		firstAddress.setCode("50698");

		addressService.createAddress(firstAddress);

		// Known on...
		TimeUtils.setReference(TimeUtils.day(1, 1, 2010));

		// First Address will be valid from now on (1-Jan-2010 .. end_of_time)
		person.address().set(firstAddress);

		Address secondAddress = new AddressImpl();
		secondAddress.setPerson(person);
		secondAddress.setStreet("Berlin 22");
		secondAddress.setCity("Berlin");
		secondAddress.setCode("10313");

		addressService.createAddress(secondAddress);

		// Second Address supersedes the first one:
		// - First Address valid in [1-Jan-2010 .. 10-Feb-2010]
		// - Second Address valid in [10-Feb-2010 .. end_of_time]
		person.address().set(secondAddress, TimeUtils.from(TimeUtils.day(10, 2, 2010)));

		Address thirdAddress = new AddressImpl();
		thirdAddress.setPerson(person);
		thirdAddress.setStreet("Muenster 12");
		thirdAddress.setCity("Muenster");
		thirdAddress.setCode("43744");

		addressService.createAddress(thirdAddress);

		// Known on...
		TimeUtils.setReference(TimeUtils.day(27, 7, 2010));

		// Third Address supersedes the second one but known later:
		// - First Address valid in [1-Jan-2010 .. 10-Feb-2010]
		// - Second Address valid in [10-Feb-2010 .. 13-July-2010]
		// - Third Address valid in [13-July-2010 .. end_of_time]
		person.address().set(thirdAddress, TimeUtils.from(TimeUtils.day(13, 7, 2010)));

		// Doing some asserts for the scenes...
		Address addressValue1 = person.address().on(TimeUtils.day(3, 2, 2010), TimeUtils.day(1, 1, 2010));
		assertEquals("Koeln", addressValue1.getCity());

		Address addressValue2 = person.address().on(TimeUtils.day(10, 7, 2010), TimeUtils.day(1, 1, 2010));
		assertEquals("Berlin", addressValue2.getCity());

		// Known on 1-Jan-2010
		Address addressValue3 = person.address().on(TimeUtils.day(15, 7, 2010), TimeUtils.day(1, 1, 2010));
		assertEquals("Berlin", addressValue3.getCity());

		// Known on from 27-July-2010
		Address addressValue4 = person.address().on(TimeUtils.day(15, 7, 2010), TimeUtils.day(31, 7, 2010));
		assertEquals("Muenster", addressValue4.getCity());
	}
}
