select * from PERSON;

select * from ADRESSE;

select * from BITEMP_ADR;

select distinct * from PERSON, ADRESSE where PERSON.ID = ADRESSE.PERSON_ID;

select distinct * from PERSON, ADRESSE, BITEMP_ADR where PERSON.ID = ADRESSE.PERSON_ID;