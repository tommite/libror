CURPKG=libror_0.2.tar.gz

libror_%.tar.gz: ../target/libror-%-jar-with-dependencies.jar libror/R/*.R libror/man/*.Rd libror/DESCRIPTION libror/NAMESPACE
	rm -Rf libror/inst/java
	mkdir -p libror/inst/java
	cp $< libror/inst/java
	R CMD build libror
	R CMD check libror

all: ${CURPKG}

../target/libror-%-jar-with-dependencies.jar:
	mvn -f ../pom.xml package

clean:
	-rm ${CURPKG}
	-rm -Rf libror/inst

install: all
	R CMD INSTALL libror_0.2.tar.gz
