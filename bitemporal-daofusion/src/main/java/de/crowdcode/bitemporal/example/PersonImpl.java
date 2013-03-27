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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.joda.time.Interval;

import com.anasoft.os.daofusion.bitemporal.WrappedBitemporalProperty;
import com.anasoft.os.daofusion.bitemporal.WrappedValueAccessor;
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

	@Column
	private String vorname;

	@Column
	private String nachname;

	@OneToMany(fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@JoinColumn(name = "person")
	private final Collection<BitemporalAdresseImpl> bitemporalAdressen = new LinkedList<BitemporalAdresseImpl>();

	// Use this method for accessing bitemporal trace of Adressen values
	public WrappedBitemporalProperty<Adresse, BitemporalAdresseImpl> getBitemporalAdressen() {
		return new WrappedBitemporalProperty<Adresse, BitemporalAdresseImpl>(bitemporalAdressen,
				new WrappedValueAccessor<Adresse, BitemporalAdresseImpl>() {

					private static final long serialVersionUID = -3548772720386675459L;

					@Override
					public BitemporalAdresseImpl wrapValue(Adresse value, Interval validityInterval) {
						return new BitemporalAdresseImpl(value, validityInterval);
					}
				});
	}

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
