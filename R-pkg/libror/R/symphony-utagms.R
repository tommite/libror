library(Rsymphony)

utagms.buildRelation <- function(perf, preferences, necessary=TRUE, strictVF=FALSE) {
  rel <- matrix(nrow=nrow(perf), ncol=ncol(perf))

  for (i in 1:nrow(rel)) {
    for(j in 1:ncol(rel)) {
      rel[i,j] = checkRelation(perf, preferences, i, j, necessary, strictVF)
    }
  }
  if (!is.null(rownames(perf))) {
    rownames(rel) <- rownames(perf)
    colnames(rel) <- rownames(perf)    
  }
  return(rel)
}

checkRelation <- function(perf, preferences, a, b, necessary=TRUE, strictVF=FALSE) {
  ## check vars
  stopifnot(is.logical(necessary))
  stopifnot(is.logical(strictVF))
  if (a == b) {
    return(TRUE)
  }
  altVars <- buildAltVariableMatrix(perf)  
  baseModel <- buildBaseLPModel(perf, preferences, strictVF)

  addConst <- c()
  if (necessary == TRUE) {
    addConst <- buildStrongPreferenceConstraint(b, a, altVars)
  } else { ## possible
    allConst <- buildWeakPreferenceConstraint(a, b, altVars)
  }
  allConst <- combineConstraints(baseModel, addConst)
  obj <- buildObjectiveFunction(perf)
  ret <- Rsymphony_solve_LP(obj, allConst$lhs, allConst$dir, allConst$rhs, max=TRUE)

  if (necessary == TRUE) {
    return(ret$status != 0 || ret$objval <= 0)
  } else { # possible
    return(ret$status == 0 && ret$objval > 0)
  }
}

buildObjectiveFunction <- function(perf) {
  levels <- getLevels(perf)
  nrVars <- getNrVars(levels)

  lhs <- rep(0, nrVars)
  lhs[length(lhs)] = 1
  return(lhs)
}


## perf: performance matrix
## preferences: an n x 2 matrix, where each row (a, b) means
## that a is strictly preferred to b.
## strictVF = TRUE -> value functions strictly increasing (instead of monotonous increasing)
buildBaseLPModel <- function(perf, preferences, strictVF=FALSE) {
  altVars <- buildAltVariableMatrix(perf)

  c1 <- buildMonotonousConstraints(perf, strictVF)
  c2 <- buildFirstLevelZeroConstraints(perf)
  c3 <- buildBestLevelsAddToUnityConstraint(perf)
  c4 <- buildAllVariablesLessThan1Constraint(perf)
  c5 <- buildEpsilonStrictlyPositiveConstraint(perf)

  prefConst <- c()
  
  for (i in 1:nrow(preferences)) {
    prefConst <- append(prefConst, buildStrongPreferenceConstraint(preferences[i,1], preferences[i,2], altVars))
  }
  return(combineConstraints(c1, c2, c3, c4, c5, prefConst))
}

buildStrongPreferenceConstraint <- function(a, b, altVars) {
  levels <- getLevels(perf)
  nrVars <- getNrVars(levels)

  lhs <- altVars[a,]
  lhs[length(lhs)] = -1
  lhs <- lhs - altVars[b,]

  return(list(lhs=lhs, dir=">=", rhs=0))
}

buildWeakPreferenceConstraint <- function(a, b, altVars) {
  levels <- getLevels(perf)
  nrVars <- getNrVars(levels)

  lhs <- altVars[a,]
  lhs <- lhs - altVars[b,]

  return(list(lhs=lhs, dir=">=", rhs=0))
}

combineConstraints <- function(...) {
  allConst = list(...)

  lhs <- c()
  dir <- c()
  rhs <- c()

  for (const in allConst) {
    lhs <- rbind(lhs, const$lhs)
    dir <- c(dir, const$dir)
    rhs <- c(rhs, const$rhs)
  }

  return(list(lhs=lhs, dir=dir, rhs=rhs))
}

buildEpsilonStrictlyPositiveConstraint <- function(perf) {
  levels <- getLevels(perf)
  nrVars <- getNrVars(levels)

  lhs <- rep(0, nrVars)
  lhs[length(lhs)] = 1
  return(list(lhs=lhs, dir=">=", rhs=1E-7))
}

buildAllVariablesLessThan1Constraint <- function(perf) {
  levels <- getLevels(perf)
  nrVars <- getNrVars(levels)

  lhs <- diag(nrVars)

  return(list(lhs=lhs, dir=rep("<=", nrVars), rhs=rep(1, nrVars)))
}

buildBestLevelsAddToUnityConstraint <- function(perf) {
  levels <- getLevels(perf)
  offsets <- getOffsets(levels)
  nrVars <- getNrVars(levels)

  lhs <- rep(0, nrVars)
  ind <- c((offsets-1)[-1], nrVars-1)
  lhs[ind] = 1
  return(list(lhs=lhs, dir="==", rhs=1))
}

buildFirstLevelZeroConstraints <- function(perf) {
  levels <- getLevels(perf)
  offsets <- getOffsets(levels)
  nrVars <- getNrVars(levels)
  
  res <- matrix(0, nrow=nrow(perf),ncol=nrVars)

  for (i in seq(1:length(offsets))) {
    res[i,offsets[i]] = 1
  }

  return(list(lhs=res,dir=rep("==", length(offsets)),rhs=rep(0,length(offsets))))
}

buildMonotonousConstraints <- function(perf, strictly=FALSE) {
  levels <- getLevels(perf)
  offsets <- getOffsets(levels)
  nrVars <- getNrVars(levels)

  res <- c()
  
  for (i in seq(1:length(levels))) {
    for (j in seq(1:(length(levels[[i]])-1))) {
      index <- offsets[i] + j - 1
      lhs <- array(0, dim=nrVars)
      lhs[index] <- 1
      lhs[index+1] <- -1
      if (strictly == TRUE) {
        lhs[length(lhs)] = 1
      }
      res <- rbind(res, lhs)
    }
  }

  return(list(lhs=res, dir=rep("<=", nrow(res)), rhs=rep(0, nrow(res))))
}

getLevels <- function(perf) {
  return(apply(perf, 2, function(x) {sort(unique(x))} ))
}

getNrVars <- function(levels) {
  return(sum(as.numeric(lapply(levels, length))) + 1)
}

buildAltVariableMatrix <- function(perf) {
  levels <- getLevels(perf)

  offsets <- getOffsets(levels)
  nrAlts <- nrow(perf)
  nrCrit <- ncol(perf)

  nrVars <- getNrVars(levels)

  resMat = matrix(nrow=nrAlts,ncol=nrVars)
  
  for (i in seq(1:nrAlts)) {
    vec <- array(0, dim=nrVars)
    indices <- sapply(seq(1:nrCrit), function(x) {which(levels[[x]] == perf[i,x])} )
    vec[indices + offsets - 1] = 1
    resMat[i,] = vec
  }
  return(resMat)
}

getOffsets <- function(levels) {
  x <- cumsum(lapply(levels, length))
  return(c(1, x[1:length(x)-1] + 1))
}
