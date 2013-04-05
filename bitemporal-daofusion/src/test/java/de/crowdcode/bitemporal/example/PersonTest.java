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

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.anasoft.os.daofusion.bitemporal.TimeUtils;

/**
 * Unit test for simple app.
 * 
 * @author Lofi Dewanto
 * @since 1.0.0
 * @version 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:de/crowdcode/bitemporal/example/spring-example.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class PersonTest {

	@Inject
	@Named("PersonServiceImpl")
	private PersonServiceImpl personServiceImpl;

	@Inject
	@Named("AdresseServiceImpl")
	private AdresseServiceImpl adresseServiceImpl;

	@Test
	public void testCreateBitemporalAdressen() {
		PersonImpl person = new PersonImpl();
		person.setNachname("Jawa");
		person.setVorname("Lofi");

		personServiceImpl.createPerson(person);

		Adresse firstAdresse = new AdresseImpl();
		firstAdresse.setPerson(person);
		firstAdresse.setStrasse("Koeln 21");
		firstAdresse.setStadt("Koeln");
		firstAdresse.setPlz("50698");

		adresseServiceImpl.createAdresse((AdresseImpl) firstAdresse);

		TimeUtils.setReference(TimeUtils.day(1, 1, 2010));

		// First Adresse will be valid from now on (1-Jan-2010 .. end_of_time)
		person.getBitemporalAdressen().set(firstAdresse);

		Adresse secondAdresse = new AdresseImpl();
		secondAdresse.setPerson(person);
		secondAdresse.setStrasse("Berlin 22");
		secondAdresse.setStadt("Berlin");
		secondAdresse.setPlz("10313");

		adresseServiceImpl.createAdresse((AdresseImpl) secondAdresse);

		// Second Adresse supersedes the first one:
		// - First Adresse valid in [1-Jan-2010 .. 10-Feb-2010]
		// - Second Adresse valid in [10-Feb-2010 .. end_of_time]
		person.getBitemporalAdressen().set(secondAdresse, TimeUtils.from(TimeUtils.day(10, 2, 2010)));
	}
}
