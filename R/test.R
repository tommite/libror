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

evaluateAlternative <- function(ror, vfIndex, altIndex) {
  altIndex = altIndex -1
  .jcall(ror, "D", method="evaluateAlternative", as.integer(vfIndex), as.integer(altIndex))
}

addPreference <- function(ror, a, b) {
  .jcall(ror, "V", method="addPreference", as.integer(a), as.integer(b))
}  
                                                               
p <- matrix(runif(n=50), nrow=10) # 10 alts, 5 crit
ror <- createROR(p)
addPreference(ror, 1, 2)
addPreference(ror, 4, 5)
addPreference(ror, 7, 8)
addPreference(ror, 1, 3)

sampleROR(ror)


