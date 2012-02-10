rorsmaa <- function(performances, preferences, necessary=TRUE) {
  ror <- rorsmaa.create(performances)
  if (is.matrix(preferences)) {
    for (i in 1:nrow(preferences)) {
      ror.addPreference(ror, preferences[i,1], preferences[i,2])
    }
  }
  rets <- rorsmaa.compute(ror)
  if (nchar(rets) > 0) {
    stop(rets)
  }

  poi <- rorsmaa.getPOIs(ror)
  rai <- rorsmaa.getRAIs(ror)
  misses <- rorsmaa.getMisses(ror)
  
  return(list(poi=poi, rai=rai, misses=misses))
}

rorsmaa.create <- function(perfMat) {
  model <- .jnew("fi/smaa/libror/r/RORSMAARFacade", as.vector(perfMat), as.integer(nrow(perfMat)))
  list(model=model, rownames=rownames(perfMat), colnames=colnames(perfMat))
}

rorsmaa.compute <- function(ror) {
  rets <- .jcall(ror$model, "S", method="compute")
  return(.jstrVal(rets))
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
  rai <- .jcall(ror$model, "[[D", method="getRAIs", simplify=TRUE)
  rownames(rai) <- ror$rownames
  if (!is.null(ror$rownames)) {
    colnames(rai) <- seq(1, length(ror$rownames))
  }
  return(rai)
}

rorsmaa.getPOIs <- function(ror) {
  poi <- .jcall(ror$model, "[[D", method="getPOIs", simplify=TRUE)
  rownames(poi) <- ror$rownames
  colnames(poi) <- ror$rownames
  return(poi)
}



