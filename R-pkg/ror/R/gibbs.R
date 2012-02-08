sample.vfs.gibbs <- function(performances, preferences, nr=10000, thinning=1, updInterval=1000) {
  stopifnot(nr > 0)
  stopifnot(thinning > 0)
  
  ror <- gibbs.create(performances, nr, thinning)
  if (is.matrix(preferences)) {
    for (i in 1:nrow(preferences)) {
      ror.addPreference(ror, preferences[i,1], preferences[i,2])
    }
  }
  gibbs.sample(ror, updInterval)

  return(gibbs.allValueFunctions(ror, ncol(performances)))    
}

gibbs.allValueFunctions <- function(ror, nVf) {
  lapply(seq(1, nVf), function(x) {gibbs.getValueFunctionsForCriterion(ror, x)})
}

gibbs.create <- function(perfMat, nrVF, thinning) {
  model <- .jnew("fi/smaa/libror/r/GibbsValueFunctionSamplerRFacade",
                 as.vector(perfMat), as.integer(nrow(perfMat)),
                 as.integer(nrVF), as.integer(thinning))
  list(model=model, rownames=rownames(perfMat), colnames=colnames(perfMat))
}


gibbs.sample <- function(ror, upd) {
  .jcall(ror$model, "V", method="sample", as.integer(upd))
}

gibbs.getValueFunctionsForCriterion <- function(ror, cIndex) {
  .jcall(ror$model,
         "[[D",
         method="getValueFunctionsForCriterion",
         as.integer(cIndex-1),
         simplify=TRUE)
}


