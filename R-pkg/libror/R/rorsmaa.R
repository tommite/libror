rorsmaa <- function(performances, preferences, necessary=TRUE) {
  rel <- matrix(nrow=nrow(performances), ncol=ncol(performances))

  ror <- rorsmaa.create(performances)
  if (is.matrix(preferences)) {
    for (i in 1:nrow(preferences)) {
      ror.addPreference(ror, preferences[i,1], preferences[i,2])
    }
  }
  rorsmaa.compute(ror)

  poi <- rorsmaa.getPOIs(ror)
  rai <- rorsmaa.getRAIs(ror)
  misses <- rorsmaa.getMisses(ror)
  
  return(list(poi=poi, rai=rai, misses=misses))
}

rorsmaa.create <- function(perfMat) {
  model <- .jnew("fi/smaa/libror/r/RORSMAARFacade", as.vector(perfMat), as.integer(nrow(perfMat)))
  list(model=model, rownames=rownames(perfMat), colnames=colnames(perfMat))
}

rorsmaa.getValueFunctionVals <- function(ror, vfIndex, partialVfIndex) {
  partialVfIndex = partialVfIndex - 1
  vfIndex = vfIndex - 1
  .jcall(ror$model, "[D", method="getValueFunctionVals", as.integer(vfIndex), as.integer(partialVfIndex))
}

rorsmaa.getValueFunctionEvals <- function(ror, vfIndex, partialVfIndex) {
  partialVfIndex = partialVfIndex - 1
  vfIndex = vfIndex - 1
  .jcall(ror$model, "[D", method="getValueFunctionEvals", as.integer(vfIndex), as.integer(partialVfIndex))
}

rorsmaa.singleValueFunction <- function(ror, index) {
  nPartVf <- .jcall(ror$model, "I", method="getNrPartialValueFunctions")
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

rorsmaa.allValueFunctions <- function(ror) {
  nVf <- .jcall(ror$model, "I", method="getNrValueFunctions")
  nPartVf <- .jcall(ror$model, "I", method="getNrPartialValueFunctions")
  ret <- list()

  lapply(seq(1, nVf), function(x) {rorsmaa.singleValueFunction(ror, x)})
}

rorsmaa.compute <- function(ror) {
  .jcall(ror$model, method="compute")
}

rorsmaa.evaluateAlternative <- function(ror, vfIndex, altIndex) {
  altIndex = altIndex -1
  vfIndex = vfIndex-1
  .jcall(ror$model, "D", method="evaluateAlternative", as.integer(vfIndex), as.integer(altIndex))
}

rorsmaa.getMisses <- function(ror) {
  .jcall(ror$model, "I", method="getMisses")
}

rorsmaa.getRAIs <- function(ror) {
  rai <- .doubleArrayToMatrix(.jcall(ror$model, "[[D", method="getRAIs"))
  rownames(rai) <- ror$rownames
  if (!is.null(ror$rownames)) {
    colnames(rai) <- seq(1, length(ror$rownames))
  }
  return(rai)
}

rorsmaa.getPOIs <- function(ror) {
  poi <- .doubleArrayToMatrix(.jcall(ror$model, "[[D", method="getPOIs"))
  rownames(poi) <- ror$rownames
  colnames(poi) <- ror$rownames
  return(poi)
}



