Bitemporal Examples for Java
============================

A nice introduction article to Temporality and its patterns:
* Martin Fowler Temporal Patterns: http://martinfowler.com/eaaDev/timeNarrative.html

Temporal and Bitemporal Examples in Java with OSS Frameworks:

* Erwin Vervaet (ervacon): https://svn.ervacon.com/public/projects/bitemporal/trunk
* DAOFusion: http://opensource.anasoft.com/daofusion-site/index.html
* Hibernate Envers: http://docs.jboss.org/hibernate/orm/4.2/devguide/en-US/html_single/#d5e3941
* Spring Data JPA Auditing: http://static.springsource.org/spring-data/data-jpa/docs/current/reference/html/jpa.repositories.html#jpa.auditing
* JTemporal: http://jtemporal.sourceforge.net
 
You can take a look at some examples in this folder:

* **non-temporal**: this is an example without temporality, very simple example
* **actual-temporal**: this is an actual temporal example
* **record-temporal-envers**: this is a record temporal example using Hibernate Envers
* **bitemporal-daofusion**: this is a bitemporal example using the framework from Erwin Vervaet and DAOFusion

All the examples use following implementation basic frameworks:
* [KissMDA](https://github.com/crowdcode-de/KissMDA): to be able to generate the Java interfaces automatically from the UML model. Using UML makes it easy to explain the examples! You can see the exported model as JPG file in *src/main/docs* directory.
* [Spring Framework](http://www.springsource.org/spring-framework): for dependency injection and integration platform
* [Hibernate](http://www.hibernate.org): as the implementation of JPA
* [Maven](http://maven.apache.org): as build system

Have fun!  
[Lofi](http://lofidewanto.blogspot.com)
