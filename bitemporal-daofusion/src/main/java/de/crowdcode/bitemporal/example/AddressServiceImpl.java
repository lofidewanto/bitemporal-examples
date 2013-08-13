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

import org.joda.time.Interval;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Named("addressService")
public class AddressServiceImpl implements AddressService {

	@Inject
	@Named("addressRepository")
	private AddressRepository addressRepository;

	@Inject
	@Named("personService")
	private PersonService personService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Address createAddress(Address address) {
		Address addressCreated = addressRepository.save(address);
		return addressCreated;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Integer getAmountOfAddress() {
		Integer amount = addressRepository.getAmount();
		return amount;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Collection<Address> findAllAddresses() {
		return addressRepository.findAll();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Address createAddressWithPerson(Address address, Person person) {
		Address addressCreated = addressRepository.save(address);
		Person personFound = personService.findPersonById(person.getId());
		personFound.address().set(addressCreated);
		return addressCreated;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Address createAddressWithPerson(Address address, Person person, Interval interval) {
		Address addressCreated = addressRepository.save(address);
		Person personFound = personService.findPersonById(person.getId());
		personFound.address().set(addressCreated, interval);
		return addressCreated;
	}
}
