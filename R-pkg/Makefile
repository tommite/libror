CURPKG=ror_0.4.tar.gz
CURJAVAPKG=libror-0.3-jar-with-dependencies.jar

${CURPKG}: ../target/${CURJAVAPKG} ror/R/*.R ror/man/*.Rd ror/DESCRIPTION ror/NAMESPACE
	rm -Rf ror/inst/java
	mkdir -p ror/inst/java
	cp $< ror/inst/java
	R CMD build ror
	R CMD check ror

all: ${CURPKG}

../target/${CURJAVAPKG}:
	mvn -f ../pom.xml package

clean:
	-rm ${CURPKG}
	-rm -Rf ror/inst

install: all
	R CMD INSTALL ${CURPKG}
