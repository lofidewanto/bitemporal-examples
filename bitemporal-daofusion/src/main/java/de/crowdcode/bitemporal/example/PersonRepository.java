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

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Named("personRepository")
public class PersonRepository {

	@PersistenceContext
	private EntityManager em;

	public Person save(Person person) {
		em.persist(person);
		return person;
	}

	public Integer getAmount() {
		Query query = em.createQuery("select count(c.id) from PersonImpl c");
		Number amount = (Number) query.getSingleResult();
		return amount.intValue();
	}

	@SuppressWarnings("unchecked")
	public Collection<Person> findAll() {
		Query query = em.createQuery("select c from PersonImpl c");
		return query.getResultList();
	}

	public Person findById(Long id) {
		Query query = em.createQuery("select c from PersonImpl c where c.id = :id");
		query.setParameter("id", id);
		return (Person) query.getSingleResult();
	}
}
