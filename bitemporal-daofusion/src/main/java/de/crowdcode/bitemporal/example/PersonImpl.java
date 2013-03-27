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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.anasoft.os.daofusion.test.example.entity.OidBasedMutablePersistentEntity;

/**
 * Person implementation.
 * 
 * @author Lofi Dewanto
 * @since 1.0.0
 * @version 1.0.0
 */
@Entity
@Table(name = "person")
public class PersonImpl extends OidBasedMutablePersistentEntity implements Person {

	private static final long serialVersionUID = -7110031754812700550L;

	@Column(nullable = false, updatable = false)
	private String vorname;

	@Column(nullable = false, updatable = false)
	private String nachname;

	@Override
	public String getVorname() {
		return vorname;
	}

	@Override
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	@Override
	public Collection<Adresse> getAdressen() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAdressen(Adresse adress) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getNachname() {
		return nachname;
	}

	@Override
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
}
