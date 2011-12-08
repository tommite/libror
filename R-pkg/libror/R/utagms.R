library(rJava)
include('libror_common.R')

utagms.createROR <- function(perfMat) {
  .jnew("fi/smaa/libror/r/UTAGMSSolverRFacade", as.vector(perfMat), as.integer(nrow(perfMat)))
}

utagms.solve <- function(ror) {
  .jcall(ror, method="solve")  
}

utagms.printModel <- function(necessary, aind, bind) {
  .jcall(ror, method="printModel", as.logical(necessary), as.integer(aind),
         as.integer(bind))
}

utagms.getNecessaryRelation <- function(ror) {
  .jcall(ror, "[D", method="getNecessaryRelation")
}

utagms.getPossibleRelation <- function(ror) {
  .jcall(ror, "[D", method="getPossibleRelation")
}

