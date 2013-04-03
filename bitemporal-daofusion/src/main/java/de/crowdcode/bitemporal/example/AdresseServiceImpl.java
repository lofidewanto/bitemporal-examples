package de.crowdcode.bitemporal.example;

import javax.inject.Inject;
import javax.inject.Named;

@Named("AdresseServiceImpl")
public class AdresseServiceImpl implements AdresseService {

	@Inject
	@Named("AdresseRepository")
	private AdresseRepository adresseRepository;

	public Adresse createAdresse(AdresseImpl adresse) {
		Adresse adresseCreated = adresseRepository.save(adresse);
		return adresseCreated;
	}

	@Override
	public Adresse createAdresse() {
		// TODO Auto-generated method stub
		return null;
	}
}
