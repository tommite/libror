## performances: m x j matrix of performances, m alternatives and j criteria
## profiles: t x j matrix of category profiles, t categories
## assignments: a x 2 matrix of reference alternative assignments,
##   where each row is a single assignment and the first column index of the
##   alternative, the second column index of the desired class
## necessary: whether to compute the necessary (T) or possible (F) relation
etricror <- function(performances, profiles, assignments, necessary=TRUE) {
  stopifnot(ncol(performances) == ncol(profiles))
  stopifnot(ncol(assignments) == 2)
  
  rel <- matrix(nrow=nrow(performances), ncol=nrow(profiles))
  
  for (i in 1:nrow(rel)) {
    for(j in 1:nrow(rel)) {
      rel[i,j] = checkETRICRelation(performances, profiles, assignments, i, j, necessary=necessary)
    }
  }
    
  if (!is.null(rownames(performances))) {
    rownames(rel) <- rownames(performances)
  }
  if (!is.null(rownames(profiles))) {
    colnames(rel) <- rownames(profiles)
  }
  return(rel)
}

## aIndex: index of the alternative
## cIndex: index of the category
checkETRICRelation <- function(performances, profiles, assignments, aIndex, cIndex, necessary) {
  stopifnot(is.logical(necessary))
  stopifnot(necessary) # only necessary implemented yet

  baseVars <- buildBaseModelVariableMatrix(performances, profiles, assignments)
  
}

buildBaseModelVariableMatrix <- function(performances, profiles, assignments) {
  nAlts <- nrow(performances)
  nCrit <- ncol(performances)
  nAssignments <- nrow(assignments)
  nCats <- nrow(profiles) + 2
  
  nCol <- (nCrit * nAlts * nCats) + (nCrit * 2) + (nAssignments  * 2) + 3

  b1 <- buildB1Constraint(nAlts, nCrit, nAssignments, nCats)
  b2 <- buildB2Constraint(nAlts, nCrit, nAssignments, nCats)
  b2 <- buildB3Constraint(nAlts, nCrit, nAssignments, nCats)
}

buildB3Constraint <- function(nAlts, nCrit, nAssignments, nCats) {
  lhs1 <- rep(0, getNrBaseVars(nAlts, nCrit, nAssignments, nCats))
  lhs1[getLambdaIndex(nAlts, nCrit, nCats)] = 1
  lhs2 <- rep(0, getNrBaseVars(nAlts, nCrit, nAssignments, nCats))
  lhs2[getLambdaIndex(nAlts, nCrit, nCats)] = 1

  rhs = c(0.5, 1.0)
  return(list(lhs=rbind(lhs1, lhs2), dir=c(">=", "<="), rhs=rhs))
}

buildB1Constraint <- function(nAlts, nCrit, nAssignments, nCats) {
  lhs <- rep(0, getNrBaseVars(nAlts, nCrit, nAssignments, nCats))
  for (j in 1 : nCrit) {
    lhs[getWjIndex(j)] = 1
  }
  return(list(lhs=lhs, dir="=", rhs=c(1)))
}

buildB2Constraint <- function(nAlts, nCrit, nAssignments, nCats) {
  res <- c()

  qMinus1 <- nCats - 3

  for (h in seq(1:qMinus1)) {
    lhs <- rep(0, getNrBaseVars(nAlts, nCrit, nAssignments, nCats))
    for (j in seq(1 : nCrit)) {
      varIndex <- getCjBhBh1Index(nCrit, nAlts, nCats, j, h)
      lhs[varIndex] = 1
    }
    ## + epsilon
    lhs[getEpsilonIndex(nAlts, nCrit, nCats)] = 1
    ## -lambda
    lhs[getLambdaIndex(nAlts, nCrit, nCats)] = -1
    res <- rbind(res, lhs)
  }
  return(list(lhs=res, dir=rep("<=", qMinus1), rhs=rep(0, qMinus1)))
}

getLambdaIndex <- function(nAlts, nCrit, nCats) {
  stopifnot(nCats > 3)
  return (getCjBhBh1Index(nCrit, nAlts, nCats, nCrit, nCats-3) + 1)
}

getEpsilonIndex <- function(nAlts, nCrit, nCats) {
  return (getLambdaIndex(nAlts, nCrit, nCats) + 1)
}

getCjBhBh1Index <- function (nCrit, nAlts, nCats, j, h) {
  stopifnot(nCats > 3)
  offset <- nCrit + getNrCjABA(nCrit, nAlts, nCats)
  return(offset + ((h-1) * nCrit) + j)
}

## order is j1a1b1, j1a1b2, j2a1b1, ...
getCjABIndex <- function(j, aInd, bInd, nAlts, nCats, nCrit) {
  stopifnot(nCats > 3)
  offset <- (nAlts * nCats) * (j-1)
  index <- nCats * (aInd - 1) + bInd
  return (nCrit + (offset + index))
}

getCjBAIndex <- function(j, aInd, bInd, nAlts, nCats, nCrit) {
  stopifnot(nCats > 3)
  
  offset <- (nAlts * nCats) * (j-1)
  index <- nCats * (aInd - 1) + bInd
  return (nCrit + (nAlts * nCats * j) + (offset + index))
}

getNrCjABA <- function(nCrit, nAlts, nCats) {
  return (nCrit * nAlts * nCats * 2)
}

getWjIndex <- function(j) {
  return (j)
}

getNrBaseVars <- function(nAlts, nCrit, nAssignments, nCats) {
  return(nCrit + nCrit*nCats*nAlts*2 + ((nCats - 3) * nCrit) + 1 + 1)
}
