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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * Person implementation.
 * 
 * @author Lofi Dewanto
 * @since 1.0.0
 * @version 1.0.0
 */
@Entity
@Table(name = "person")
public class PersonImpl implements Person {

	@Column(name = "firstname")
	private String firstname;

	@Column(name = "lastname")
	private String lastname;

	@OneToMany(fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.ALL })
	@JoinColumn(name = "person")
	@Override
	public String getFirstname() {
		return firstname;
	}

	@Override
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@Override
	public Address getAddress() {
		return null;
	}

	@Override
	public void setAddress(Address address) {
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
		return null;
	}

	@Override
	public Address alive() {
		return null;
	}
}
