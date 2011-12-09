library(rJava)
include('libror_common.R')

rorsmaa.create <- function(perfMat, nrSamples) {
  .jnew("fi/smaa/libror/r/RORSMAARFacade", as.vector(perfMat), as.integer(nrow(perfMat)), as.integer(nrSamples))
}

rorsmaa.getValueFunctionVals <- function(ror, vfIndex, partialVfIndex) {
  partialVfIndex = partialVfIndex - 1
  vfIndex = vfIndex - 1
  .jcall(ror, "[D", method="getValueFunctionVals", as.integer(vfIndex), as.integer(partialVfIndex))
}

rorsmaa.getValueFunctionEvals <- function(ror, vfIndex, partialVfIndex) {
  partialVfIndex = partialVfIndex - 1
  vfIndex = vfIndex - 1
  .jcall(ror, "[D", method="getValueFunctionEvals", as.integer(vfIndex), as.integer(partialVfIndex))
}

rorsmaa.singleValueFunction <- function(ror, index) {
  nPartVf <- .jcall(ror, "I", method="getNrPartialValueFunctions")
  v <- c()
  e <- c()
  for (i in 1:nPartVf) {
    vals <- rorsmaa.getValueFunctionVals(ror, index, i)
    evals <- rorsmaa.getValueFunctionEvals(ror, index, i)

    v <- rbind(v, vals)
    e <- rbind(e, evals)    
  }
  list(vals=v, evals=e)
}

rorsmaa.allValueFunctions <- function(ror, perfMat) {
  nAlt <- dim(perfMat)[1]
  nCrit <- dim(perfMat)[2]
  nVf <- .jcall(ror, "I", method="getNrValueFunctions")
  nPartVf <- .jcall(ror, "I", method="getNrPartialValueFunctions")
  ret <- list()

  lapply(seq(1, nVf), function(x) {rorsmaa.singleValueFunction(ror, x)})
}

rorsmaa.sample <- function(ror) {
  .jcall(ror, method="sample")
}

evaluateAlternative <- function(ror, vfIndex, altIndex) {
  altIndex = altIndex -1
  .jcall(ror, "D", method="evaluateAlternative", as.integer(vfIndex), as.integer(altIndex))
}

rorsmaa.getMisses <- function(ror) {
  .jcall(ror, "I", method="getMisses")
}




