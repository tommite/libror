CURPKG=libror_0.3.tar.gz
CURJAVAPKG=libror-0.2-jar-with-dependencies.jar

${CURPKG}: ../target/${CURJAVAPKG} libror/R/*.R libror/man/*.Rd libror/DESCRIPTION libror/NAMESPACE
	rm -Rf libror/inst/java
	mkdir -p libror/inst/java
	cp $< libror/inst/java
	R CMD build libror
	R CMD check libror

all: ${CURPKG}

../target/${CURJAVAPKG}:
	mvn -f ../pom.xml package

clean:
	-rm ${CURPKG}
	-rm -Rf libror/inst

install: all
	R CMD INSTALL ${CURPKG}
