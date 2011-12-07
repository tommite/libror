PACKAGE=libror_0.1.tar.gz

$(PACKAGE): libror/R/* libror/man/* libror/DESCRIPTION
	R CMD build libror
	R CMD check libror

.PHONY: install

install: $(PACKAGE)
	R CMD INSTALL libror
