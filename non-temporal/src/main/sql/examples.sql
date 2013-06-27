select * from PERSON;

select * from ADDRESS;

select distinct * from PERSON, ADDRESS where PERSON.ID = ADDRESS.PERSON_ID;

