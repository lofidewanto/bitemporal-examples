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

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Named("addressService")
public class AddressServiceImpl implements AddressService {

	private final static Logger logger = LoggerFactory
			.getLogger(AddressServiceImpl.class);

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
		Collection<Address> addresses = addressRepository.findAll();
		return addresses;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Address createAddressWithPerson(Address address, Person person) {
		Address addressCreated = addressRepository.save(address);
		Person personFound = personService.findPersonById(person.getId());
		personFound.setAddress(addressCreated);
		return addressCreated;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Collection<Address> findAuditedAdressesWithRevision(
			Integer revisionNumber) {
		Collection<Address> addresses = addressRepository
				.findAuditedAdressesWithRevision(revisionNumber);
		return addresses;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Address updateAddress(Address address) {
		Address updatedAddress = addressRepository.update(address);
		return updatedAddress;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String findRevisionNumberByAddressIdAndRevisionNumber(
			Long addressId, Integer revisionNumber) {
		Number number = addressRepository
				.findRevisionNumberByAddressIdAndRevisionNumber(addressId,
						revisionNumber);
		return number.toString();
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Collection<Address> findAddressesChangedByRevisionNumber(
			Integer revisionNumber) {
		Collection<Object> objects = addressRepository
				.findEntitiesChangedByRevisionNumber(revisionNumber);
		logger.info("findAddressesChangedByRevisionNumber: amount of objects: "
				+ objects.size());

		Collection<Address> addresses = new ArrayList<Address>();
		for (Object object : objects) {
			if (object instanceof Address) {
				addresses.add((Address) object);
			}
		}

		logger.info("findAddressesChangedByRevisionNumber: amount of addresses: "
				+ addresses.size());
		return addresses;
	}
}
