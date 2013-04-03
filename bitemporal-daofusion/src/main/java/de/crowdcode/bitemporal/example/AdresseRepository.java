package de.crowdcode.bitemporal.example;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
@Named("AdresseRepository")
public class AdresseRepository {

	@PersistenceContext
	private EntityManager em;

	public Adresse save(Adresse adresse) {
		em.persist(adresse);
		return adresse;
	}
}
