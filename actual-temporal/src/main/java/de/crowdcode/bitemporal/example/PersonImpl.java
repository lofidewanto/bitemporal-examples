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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.NotImplementedException;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * Person implementation.
 * 
 * @author Lofi Dewanto
 * @since 1.0.0
 * @version 1.0.0
 */
@Configurable(preConstruction = true)
@Entity
@Table(name = "person")
public class PersonImpl implements Person, Serializable {

	private static final long serialVersionUID = 2909659022959145389L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "firstname")
	private String firstname;

	@Column(name = "lastname")
	private String lastname;

	@OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.ALL })
	private final Collection<AddressImpl> addresses = new ArrayList<AddressImpl>();

	@Transient
	@Inject
	@Named("addressRepository")
	private AddressRepository addressRepository;

	@Override
	public String getFirstname() {
		return firstname;
	}

	@Override
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@Override
	public Collection<Address> getAddresses() {
		Collection<Address> newAddresses = new ArrayList<Address>();
		for (Address address : addresses) {
			newAddresses.add(address);
		}
		return newAddresses;
	}

	@Override
	public void addAddress(Address address) {
		// Add the new address
		addresses.add((AddressImpl) address);
	}

	@Override
	public String getLastname() {
		return lastname;
	}

	@Override
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@Override
	public Address address() {
		// Return the actual address for validFrom. There can
		// be one and only one actual address
		Date validDate = new Date();
		Address currentAddress = getSingleAddress(validDate);
		return currentAddress;
	}

	/**
	 * Get one and only one address.
	 * 
	 * @param validDate
	 *            the validity date
	 * @return the single address
	 */
	Address getSingleAddress(Date validDate) {
		Collection<AddressImpl> currentAddresses = addressRepository
				.findByPersonIdAndValidity(this.id, validDate);
		if (currentAddresses.size() == 0) {
			return null;
		} else if (currentAddresses.size() == 1) {
			Address currentAddress = currentAddresses.iterator().next();
			return currentAddress;
		} else {
			// We found more than one, we need the latest one!
			Address latestAddress = Collections.max(currentAddresses);
			return latestAddress;
		}
	}

	@Override
	public Address address(Date valid) {
		Address currentAddressOnDate = getSingleAddress(valid);
		return currentAddressOnDate;
	}

	@Override
	public Boolean alive() {
		// Not implemented
		throw new NotImplementedException("Person.alive() is not implemented!");
	}

	@Override
	public Boolean alive(Date valid) {
		// Not implemented
		throw new NotImplementedException(
				"Person.alive(Date valid) is not implemented!");
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
}
