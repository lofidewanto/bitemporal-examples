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

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

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
@TransactionConfiguration(defaultRollback = true)
public class PersonTest {

	@Test
	public void testCreateBitemporalAdressen() {
		assertTrue(true);

		PersonImpl person = new PersonImpl();
		TimeUtils.setReference(TimeUtils.day(1, 1, 2010));

		Adresse firstAdresse = new AdresseImpl();
		Adresse secondAdresse = new AdresseImpl();

		// First Order will be valid from now on (1-Jan-2010 .. end_of_time)
		person.getBitemporalAdressen().set(firstAdresse);

		// Second Order supersedes the first one:
		// - First order valid in [1-Jan-2010 .. 10-Feb-2010]
		// - Second order valid in [10-Feb-2010 .. end_of_time]
		person.getBitemporalAdressen().set(secondAdresse, TimeUtils.from(TimeUtils.day(10, 2, 2010)));
	}
}
