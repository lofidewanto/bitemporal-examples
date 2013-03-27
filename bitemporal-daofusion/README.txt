Install DAOFusion Maven artifacts:

Download:
http://opensource.anasoft.com/daofusion-site/download.html

Install:
mvn install:install-file -Dfile=daofusion-core-1.2.0.jar -DgroupId=com.anasoft.os -DartifactId=daofusion-core -Dversion=1.2.0 -Dpackaging=jar
mvn install:install-file -Dfile=daofusion-core-1.2.0-sources.jar -DgroupId=com.anasoft.os -DartifactId=daofusion-core -Dversion=1.2.0 -Dpackaging=jar -Dclassifier=sources

mvn install:install-file -Dfile=daofusion-test-1.2.0.jar -DgroupId=com.anasoft.os -DartifactId=daofusion-test -Dversion=1.2.0 -Dpackaging=jar
mvn install:install-file -Dfile=daofusion-test-1.2.0-sources.jar -DgroupId=com.anasoft.os -DartifactId=daofusion-test -Dversion=1.2.0 -Dpackaging=jar -Dclassifier=sources
