select * from PERSON;

select * from ADDRESS;

select * from BITEMP_ADDR;

select distinct * from PERSON, ADDRESS where PERSON.ID = ADDRESS.PERSON_ID;

select distinct * from PERSON, ADDRESS, BITEMP_ADDR where PERSON.ID = ADDRESS.PERSON_ID;