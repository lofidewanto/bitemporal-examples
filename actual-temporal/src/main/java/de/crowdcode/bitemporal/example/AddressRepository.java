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
import java.util.Date;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
@Named("addressRepository")
public class AddressRepository {

	private final static Logger logger = LoggerFactory
			.getLogger(AddressRepository.class);

	@PersistenceContext
	private EntityManager em;

	public Address save(Address address) {
		em.persist(address);
		return address;
	}

	public Address update(Address address) {
		em.merge(address);
		return address;
	}

	public Integer getAmount() {
		Query query = em.createQuery("select count(c.id) from AddressImpl c");
		Number amount = (Number) query.getSingleResult();
		return amount.intValue();
	}

	@SuppressWarnings("unchecked")
	public Collection<Address> findAll() {
		Query query = em.createQuery("select c from AddressImpl c");
		return query.getResultList();
	}

	public Address findById(Long id) {
		Query query = em
				.createQuery("select c from AddressImpl c where c.id = :id");
		query.setParameter("id", id);
		return (Address) query.getSingleResult();
	}

	public Address findByValidity(Date validDate) {
		try {
			Query query = em.createQuery("select c from AddressImpl c where "
					+ "c.validFrom <= :validDate and "
					+ "c.validTo >= :validDate");
			query.setParameter("validDate", validDate);
			Address result = (Address) query.getSingleResult();
			return result;
		} catch (NoResultException e) {
			logger.error("findByPersonIdAndValidity: "
					+ e.getMessage().toString());
			return null;
		}
	}

	public Address findByPersonIdAndValidity(Long personId, Date validDate) {
		try {
			Query query = em.createQuery("select c from AddressImpl c where "
					+ "c.validFrom <= :validDate and "
					+ "c.validTo >= :validDate and "
					+ "c.person.id = :personId");
			query.setParameter("validDate", validDate);
			query.setParameter("personId", personId);
			Address result = (Address) query.getSingleResult();
			return result;
		} catch (NoResultException e) {
			logger.error("findByPersonIdAndValidity: " + e.getMessage());
			return null;
		}
	}
}
