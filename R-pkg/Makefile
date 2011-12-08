libror_%.tar.gz: libror/R/*.R libror/man/*.Rd libror/DESCRIPTION libror/NAMESPACE ../target/libror-%-jar-with-dependencies.jar
	R CMD build libror
	R CMD check libror
