library('rJava')
.jinit(classpath="../target/rorsample-0.1-SNAPSHOT-jar-with-dependencies.jar")

createROR <- function(perfMat) {
  .jnew("fi/smaa/rorsample/RORSamplerRFacade", as.vector(perfMat), as.integer(nrow(perfMat)), as.integer(10000))
}

getValueFunctionVals <- function(ror, vfIndex, partialVfIndex) {
  partialVfIndex = partialVfIndex - 1
  vfIndex = vfIndex - 1
  .jcall(ror, "[D", method="getValueFunctionVals", as.integer(vfIndex), as.integer(partialVfIndex))
}

getValueFunctionEvals <- function(ror, vfIndex, partialVfIndex) {
  partialVfIndex = partialVfIndex - 1
  vfIndex = vfIndex - 1
  .jcall(ror, "[D", method="getValueFunctionEvals", as.integer(vfIndex), as.integer(partialVfIndex))
}

singleValueFunction <- function(ror, index) {
  nPartVf <- .jcall(ror, "I", method="getNrPartialValueFunctions")
  v <- c()
  e <- c()
  for (i in 1:nPartVf) {
    vals <- getValueFunctionVals(ror, index, i)
    evals <- getValueFunctionEvals(ror, index, i)

    v <- rbind(v, vals)
    e <- rbind(e, evals)    
  }
  list(vals=v, evals=e)
}

allValueFunctions <- function(ror, perfMat) {
  nAlt <- dim(perfMat)[1]
  nCrit <- dim(perfMat)[2]
  nVf <- .jcall(ror, "I", method="getNrValueFunctions")
  nPartVf <- .jcall(ror, "I", method="getNrPartialValueFunctions")
  ret <- list()

  lapply(seq(1, nVf), function(x) {singleValueFunction(ror, x)})
}

sampleROR <- function(ror) {
  .jcall(ror, method="sample")
}
                                                               
p <- matrix(runif(n=50), nrow=10)
ror <- createROR(p)
sampleROR(ror)
