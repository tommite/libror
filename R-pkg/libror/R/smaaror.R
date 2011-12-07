library('rJava')
.jinit(classpath="../../../target/libror-0.1-SNAPSHOT-jar-with-dependencies.jar")

smaaror.createROR <- function(perfMat, nrSamples) {
  .jnew("fi/smaa/rorsample/RORSamplerRFacade", as.vector(perfMat), as.integer(nrow(perfMat)), as.integer(nrSamples))
}

smaaror.getValueFunctionVals <- function(ror, vfIndex, partialVfIndex) {
  partialVfIndex = partialVfIndex - 1
  vfIndex = vfIndex - 1
  .jcall(ror, "[D", method="getValueFunctionVals", as.integer(vfIndex), as.integer(partialVfIndex))
}

smaaror.getValueFunctionEvals <- function(ror, vfIndex, partialVfIndex) {
  partialVfIndex = partialVfIndex - 1
  vfIndex = vfIndex - 1
  .jcall(ror, "[D", method="getValueFunctionEvals", as.integer(vfIndex), as.integer(partialVfIndex))
}

smaaror.singleValueFunction <- function(ror, index) {
  nPartVf <- .jcall(ror, "I", method="getNrPartialValueFunctions")
  v <- c()
  e <- c()
  for (i in 1:nPartVf) {
    vals <- smaaror.getValueFunctionVals(ror, index, i)
    evals <- smaaror.getValueFunctionEvals(ror, index, i)

    v <- rbind(v, vals)
    e <- rbind(e, evals)    
  }
  list(vals=v, evals=e)
}

smaaror.allValueFunctions <- function(ror, perfMat) {
  nAlt <- dim(perfMat)[1]
  nCrit <- dim(perfMat)[2]
  nVf <- .jcall(ror, "I", method="getNrValueFunctions")
  nPartVf <- .jcall(ror, "I", method="getNrPartialValueFunctions")
  ret <- list()

  lapply(seq(1, nVf), function(x) {smaaror.singleValueFunction(ror, x)})
}

smaaror.sampleROR <- function(ror) {
  .jcall(ror, method="sample")
}

evaluateAlternative <- function(ror, vfIndex, altIndex) {
  altIndex = altIndex -1
  .jcall(ror, "D", method="evaluateAlternative", as.integer(vfIndex), as.integer(altIndex))
}

smaaror.getMisses <- function(ror) {
  .jcall(ror, "I", method="getMisses")
}

smaaror.addPreference <- function(ror, a, b) {
  .jcall(ror, "V", method="addPreference", as.integer(a), as.integer(b))
}



