CURPKG=ror_1.2.tar.gz
CURJAVAPKG=libror-1.0-jar-with-dependencies.jar

${CURPKG}: ../target/${CURJAVAPKG} ror/R/*.R ror/man/*.Rd ror/DESCRIPTION ror/NAMESPACE
	rm -rf ror.Rcheck
	rm -Rf ror/inst/java
	mkdir -p ror/inst/java
	cp $< ror/inst/java
	R CMD build ror
	R CMD check ror

all: ${CURPKG}

../target/${CURJAVAPKG}:
	mvn -f ../pom.xml package

clean:
	-rm ../target/${CURJAVAPKG}
	-rm ${CURPKG}
	-rm -Rf ror/inst
	-rm -rf ror.Rcheck

install: all
	R CMD INSTALL ${CURPKG}
