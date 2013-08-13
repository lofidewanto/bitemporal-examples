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
import java.util.LinkedList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.NotImplementedException;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.joda.time.Interval;

import com.anasoft.os.daofusion.bitemporal.WrappedBitemporalProperty;
import com.anasoft.os.daofusion.bitemporal.WrappedValueAccessor;
import com.anasoft.os.daofusion.entity.MutablePersistentEntity;

/**
 * Person implementation.
 * 
 * @author Lofi Dewanto
 * @since 1.0.0
 * @version 1.0.0
 */
@Entity
@Table(name = "person")
public class PersonImpl extends MutablePersistentEntity implements Person {

	private static final long serialVersionUID = -7110031754812700550L;

	@Column(name = "firstname")
	private String firstname;

	@Column(name = "lastname")
	private String lastname;

	@OneToMany(fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@JoinColumn(name = "person")
	private final Collection<BitemporalAddressImpl> bitemporalAddresses = new LinkedList<BitemporalAddressImpl>();

	@OneToMany(fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@JoinColumn(name = "person")
	private final Collection<BitemporalBooleanImpl> bitemporalAlives = new LinkedList<BitemporalBooleanImpl>();

	@Override
	public WrappedBitemporalProperty<Address, BitemporalAddressImpl> address() {
		return new WrappedBitemporalProperty<Address, BitemporalAddressImpl>(bitemporalAddresses,
				new WrappedValueAccessor<Address, BitemporalAddressImpl>() {

					private static final long serialVersionUID = -3548772720386675459L;

					@Override
					public BitemporalAddressImpl wrapValue(Address value, Interval validityInterval) {
						return new BitemporalAddressImpl(value, validityInterval);
					}
				});
	}

	@Override
	public WrappedBitemporalProperty<Boolean, BitemporalBooleanImpl> alive() {
		return new WrappedBitemporalProperty<Boolean, BitemporalBooleanImpl>(bitemporalAlives,
				new WrappedValueAccessor<Boolean, BitemporalBooleanImpl>() {

					private static final long serialVersionUID = 5959318387062285749L;

					@Override
					public BitemporalBooleanImpl wrapValue(Boolean value, Interval validityInterval) {
						return new BitemporalBooleanImpl(value, validityInterval);
					}
				});
	}

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
		throw new NotImplementedException("Bitemporal: please use the method address()!");
	}

	@Override
	public void addAddresses(Address address) {
		this.address().set(address);
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
	public Long getId() {
		return super.getId();
	}

	@Override
	public void setId(Long id) {
		super.setId(id);
	}
}
