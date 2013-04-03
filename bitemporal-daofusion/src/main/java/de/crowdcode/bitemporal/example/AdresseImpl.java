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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.anasoft.os.daofusion.test.example.entity.OidBasedMutablePersistentEntity;

/**
 * Adresse implementation.
 * 
 * @author Lofi Dewanto
 * @since 1.0.0
 * @version 1.0.0
 */
@Entity
@Table(name = "adresse")
public class AdresseImpl extends OidBasedMutablePersistentEntity implements Adresse {

	private static final long serialVersionUID = -9005004076768341870L;

	@Column(name = "strasse")
	private String strasse;

	@Column(name = "plz")
	private String plz;

	@Column(name = "stadt")
	private String stadt;

	@ManyToOne(targetEntity = PersonImpl.class)
	@JoinColumn(nullable = false, updatable = false)
	private Person person;

	@Override
	public String getStrasse() {
		return strasse;
	}

	@Override
	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	@Override
	public Person getPerson() {
		return person;
	}

	@Override
	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
	public String getStadt() {
		return stadt;
	}

	@Override
	public void setStadt(String stadt) {
		this.stadt = stadt;
	}

	@Override
	public String getPlz() {
		return plz;
	}

	@Override
	public void setPlz(String plz) {
		this.plz = plz;
	}
}
