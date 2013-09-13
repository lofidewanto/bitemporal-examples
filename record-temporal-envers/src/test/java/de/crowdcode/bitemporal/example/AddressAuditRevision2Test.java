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
public class AddressAuditRevision2Test {

	private final static Logger logger = LoggerFactory
			.getLogger(AddressAuditRevision2Test.class);

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
