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

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.junit.runner.RunWith;
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
@ContextConfiguration(locations = { "classpath:de/crowdcode/bitemporal/example/spring-example.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class PersonTest {

	@Inject
	@Named("PersonServiceImpl")
	private PersonServiceImpl personServiceImpl;

	@Inject
	@Named("AddressServiceImpl")
	private AddressServiceImpl addressServiceImpl;

	@Test
	public void testCreateBitemporalAdressen() {
		PersonImpl person = new PersonImpl();
		person.setLastname("Jawa");
		person.setFirstname("Lofi");

		personServiceImpl.createPerson(person);

		Address firstAddress = new AddressImpl();
		firstAddress.setPerson(person);
		firstAddress.setStreet("Koeln 21");
		firstAddress.setCity("Koeln");
		firstAddress.setCode("50698");

		addressServiceImpl.createAddress(firstAddress);

		// First Adresse
		person.setAddress(firstAddress);

		Address secondAddress = new AddressImpl();
		secondAddress.setPerson(person);
		secondAddress.setStreet("Berlin 22");
		secondAddress.setCity("Berlin");
		secondAddress.setCode("10313");

		addressServiceImpl.createAddress(secondAddress);

		// Second Adresse supersedes the first one
		person.setAddress(secondAddress);
	}
}
