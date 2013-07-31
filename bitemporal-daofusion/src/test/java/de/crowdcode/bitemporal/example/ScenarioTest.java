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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.anasoft.os.daofusion.bitemporal.TimeUtils;

/**
 * Scenario Unit test for Bitemporality. Taken from
 * https://svn.ervacon.com/public/projects/bitemporal/trunk
 * 
 * @author Lofi Dewanto
 * @since 1.0.0
 * @version 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/beans.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional(propagation = Propagation.REQUIRED)
public class ScenarioTest {

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

	/**
	 * The example scenarion described on Wikipedia:
	 * http://en.wikipedia.org/wiki/Temporal_database
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testScenario() {
		// 3/4/1975 John Doe is born
		// nothing happens

		// 4/4/1975 John's father registers the baby
		TimeUtils.setReference(TimeUtils.day(4, 4, 1975));

		Person johnDoe = new PersonImpl();
		johnDoe.setFirstname("John");
		johnDoe.setLastname("Doe");

		personService.createPerson(johnDoe);

		johnDoe.alive().set(true, TimeUtils.from(TimeUtils.day(3, 4, 1975)));

		Address address1 = new AddressImpl();
		address1.setPerson(johnDoe);
		address1.setCity("Smallville");
		address1.setCode("FL, USA");
		address1.setStreet("Some Street 8");

		addressService.createAddress(address1);

		johnDoe.address().set(address1,
				TimeUtils.from(TimeUtils.day(3, 4, 1975)));

		// 26/8/1994 John moves to Bigtown, but forgets to register
		// nothing happens

		// 27/12/1994 John registers his move
		Address address2 = new AddressImpl();
		address2.setPerson(johnDoe);
		address2.setCity("Bigtown");
		address2.setCode("FL, USA");
		address2.setStreet("Some Avenue 773");

		addressService.createAddress(address2);

		TimeUtils.setReference(TimeUtils.day(27, 12, 1994));
		johnDoe.address().set(address2,
				TimeUtils.from(TimeUtils.day(26, 8, 1994)));

		// 1/4/2001 John is killed in an accident, reported by the coroner that
		// same day
		TimeUtils.setReference(TimeUtils.day(1, 4, 2001));
		johnDoe.alive().set(false);

		// Asserts...
		TimeUtils.setReference(TimeUtils.day(1, 1, 2007));

		Address addressCheck1 = new AddressImpl();
		addressCheck1.setCity("Smallville");
		addressCheck1.setCode("FL, USA");
		addressCheck1.setStreet("Some Street 8");

		Address addressCheck2 = new AddressImpl();
		addressCheck2.setCity("Bigtown");
		addressCheck2.setCode("FL, USA");
		addressCheck2.setStreet("Some Avenue 773");

		// Alive checks...
		assertFalse(johnDoe.alive().hasValueOn(TimeUtils.day(1, 1, 1975)));
		assertTrue((Boolean) johnDoe.alive().on(TimeUtils.day(3, 4, 1975)));
		assertFalse(johnDoe.alive().hasValueOn(TimeUtils.day(3, 4, 1975),
				TimeUtils.day(3, 4, 1975)));
		assertFalse((Boolean) johnDoe.alive().now());

		// Addresses checks...
		Address addressValue1 = (Address) johnDoe.address().on(
				TimeUtils.day(3, 4, 1975));
		assertEquals(addressCheck1.getCity(), addressValue1.getCity());

		Address addressValue2 = (Address) johnDoe.address().on(
				TimeUtils.day(26, 8, 1994));
		assertEquals(addressCheck2.getCity(), addressValue2.getCity());

		Address addressValue3 = (Address) johnDoe.address().on(
				TimeUtils.day(26, 8, 1994), TimeUtils.day(26, 8, 1994));
		assertEquals(addressCheck1.getCity(), addressValue3.getCity());

		Address addressValue4 = (Address) johnDoe.address().on(
				TimeUtils.day(26, 8, 1994), TimeUtils.day(27, 12, 1994));
		assertEquals(addressCheck2.getCity(), addressValue4.getCity());

		Address addressValue5 = (Address) johnDoe.address().now();
		assertEquals(addressCheck2.getCity(), addressValue5.getCity());

		System.out.println("The database now looks like this:\n");
		System.out.println(johnDoe.address().getTrace().toString());
		System.out.println(johnDoe.alive().getTrace().toString());
	}
}
