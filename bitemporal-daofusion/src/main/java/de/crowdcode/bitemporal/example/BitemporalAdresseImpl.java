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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.joda.time.Interval;

import com.anasoft.os.daofusion.bitemporal.Bitemporal;
import com.anasoft.os.daofusion.bitemporal.BitemporalWrapper;

/**
 * BitemporalAdresse implementation.
 * 
 * @author Lofi Dewanto
 * @since 1.0.0
 * @version 1.0.0
 */
@Entity
@Table(name = "bitemp_adr")
public class BitemporalAdresseImpl extends BitemporalWrapper<Adresse> implements BitemporalAdresse {

	private static final long serialVersionUID = -3045504986806681059L;

	@ManyToOne(targetEntity = AdresseImpl.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Adresse value;

	protected BitemporalAdresseImpl() {
	}

	public BitemporalAdresseImpl(Adresse value, Interval validityInterval) {
		super(value, validityInterval);
	}

	@Override
	public Adresse getAdresse() {
		return value;
	}

	@Override
	public void setAdresse(Adresse adresse) {
		this.value = adresse;
	}

	@Override
	protected void setValue(Adresse value) {
		this.value = value;
	}

	@Override
	public Adresse getValue() {
		return value;
	}

	@Override
	public Bitemporal copyWith(Interval validityInterval) {
		return new BitemporalAdresseImpl(value, validityInterval);
	}
}
