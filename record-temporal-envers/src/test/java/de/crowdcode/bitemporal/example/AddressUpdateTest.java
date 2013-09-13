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

import org.junit.Before;
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
@ContextConfiguration(locations = { "classpath:META-INF/beans.xml" })
@TransactionConfiguration(defaultRollback = false)
@Transactional
public class AddressUpdateTest {

	@Inject
	@Named("addressService")
	private AddressService addressService;

	@Inject
	@Named("personService")
	private PersonService personService;

	@Before
	public void setUp() throws Exception {
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
}
