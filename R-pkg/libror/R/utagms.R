library(rJava)
include('libror_common.R')

utagms.create <- function(perfMat) {
  .jnew("fi/smaa/libror/r/UTAGMSSolverRFacade", as.vector(perfMat), as.integer(nrow(perfMat)))
}

utagms.solve <- function(ror) {
  .jcall(ror, method="solve")  
}

utagms.printModel <- function(ror, necessary, aind, bind) {
  aind = aind-1
  bind = bind-1
  .jcall(ror, "V", method="printModel", as.logical(necessary), as.integer(aind),
         as.integer(bind))
}

utagms.getNecessaryRelation <- function(ror) {
  .doubleArrayToMatrix(.jcall(ror, "[[D", method="getNecessaryRelation"))
}

utagms.getPossibleRelation <- function(ror) {
  .doubleArrayToMatrix(.jcall(ror, "[[D", method="getPossibleRelation"))
}

