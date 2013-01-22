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
  nCats <- nrow(profiles)
  
  b1 <- buildB1Constraint(nAlts, nCrit, nAssignments, nCats)
  b2 <- buildB2Constraint(nAlts, nCrit, nAssignments, nCats)
  b3 <- buildB3Constraint(nAlts, nCrit, nAssignments, nCats)
  b4 <- buildB4Constraint(nAlts, nCrit, nAssignments, nCats)
  b5 <- buildB5Constraint(nAlts, nCrit, nAssignments, nCats)
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

  tMinus1 <- nCats - 1
  
  for (h in 1:tMinus1) {
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
  return(list(lhs=res, dir=rep("<=", nCats), rhs=rep(0, tMinus1)))
}

buildB3Constraint <- function(nAlts, nCrit, nAssignments, nCats) {
  lhs1 <- rep(0, getNrBaseVars(nAlts, nCrit, nAssignments, nCats))
  lhs1[getLambdaIndex(nAlts, nCrit, nCats)] = 1
  lhs2 <- rep(0, getNrBaseVars(nAlts, nCrit, nAssignments, nCats))
  lhs2[getLambdaIndex(nAlts, nCrit, nCats)] = 1

  rhs = c(0.5, 1.0)
  return(list(lhs=rbind(lhs1, lhs2), dir=c(">=", "<="), rhs=rhs))
}

buildB4Constraint <- function(nAlts, nCrit, nAssignments, nCats) {
  lhsRes <- c()
  rhsRes <- c()

  for (j in 1 : nCrit) {
    lhs1 <- rep(0, getNrBaseVars(nAlts, nCrit, nAssignments, nCats))
    lhs2 <- rep(0, getNrBaseVars(nAlts, nCrit, nAssignments, nCats))

    lhs1[getEpsilonIndex(nAlts, nCrit, nCats)] = 1
    lhs1[getWjIndex(j)] = -1
    
    lhs2[getWjIndex(j)] = 1

    lhsRes <- rbind(lhsRes, lhs1, lhs2)
    rhsRes <- rbind(rhsRes, 0, 0.5)
  }
  return(list(lhs=lhsRes, dir=rep("<=", nCrit*2), rhs=rhsRes))
}

## phi is a vector of function objects (1 for each criterion)
buildB5Constraint <- function(performances, profiles, nAssignments, phi) {
  
  nAlts <- nrow(performances)
  nCats <- nrow(profiles) + 2
  nCrit <- ncol(performances)
  
  stopifnot(length(phi) == nCrit)
  
  lhsRes <- c()

  nrRows <- 0
  for (j in 1 : nCrit) {
    for (aInd in 1 : nAlts) {
      for (bInd in 1 : nCats) {
        lhs1 <- rep(0, getNrBaseVars(nAlts, nCrit, nAssignments, nCats))
        lhs2 <- rep(0, getNrBaseVars(nAlts, nCrit, nAssignments, nCats))
        indAB <- getCjABIndex(j, aInd, bInd, nAlts, nCats, nCrit)
        indBA <- getCjBAIndex(j, aInd, bInd, nAlts, nCats, nCrit)
        
        lhs1[indAB] = 1
        lhs1[getWjIndex(j)] = -1 * phi[j](performances[aInd,j], profiles[bInd,j])
        
        lhs2[indBA] = 1
        lhs2[getWjIndex(j)] = -1 * phi[j](profiles[bInd,j], performances[aInd,j])
        lhsRes <- rbind(lhsRes, lhs1, lhs2)

        nrRows = nrRows + 2
      }
    }
    for (h in 1:nCats) {
      lhs <- rep(0, getNrBaseVars(nAlts, nCrit, nAssignments, nCats))
      lhs[getWjIndex(j)] = -1 * phi[j](profiles[h,j], profiles[h+1,j])
      lhs[getCjBhBh1Index(nCrit, nAlts, nCats, j, h)]
      
      lhsRes <- rBind(lhsRes, lhs)
    }    
  }
  return(list(lhs=lhsRes, dir=rep("=", nCrit*2), rhs=rep(0, nrRows)))
}

getLambdaIndex <- function(nAlts, nCrit, nCats) {
  return (nCrit + nCrit * nAlts * nCats * 2 + nCrit * (nCats-1) + 1)
}

getEpsilonIndex <- function(nAlts, nCrit, nCats) {
  return (getLambdaIndex(nAlts, nCrit, nCats) + 1)
}

getCjBhBh1Index <- function (nCrit, nAlts, nCats, j, h) {
  offset <- nCrit +  (nCrit * nAlts * nCats * 2)
  tMinus1 <- nCats - 1
  return(offset + (j-1) * tMinus1 + h)
}

## order is j1a1b1, j1a1b2, j2a1b1, ...
getCjABIndex <- function(j, aInd, bInd, nAlts, nCats, nCrit) {
  stopifnot(bInd <= nCats && bInd > 0)
  stopifnot(aInd <= nAlts && aInd > 0)
  stopifnot(j <= nCrit && j > 0)
  
  offset <- nCrit
  index <- (j - 1) * nCats * nAlts + (aInd - 1) * nCats + bInd
  return (offset + index)
}

getCjBAIndex <- function(j, aInd, bInd, nAlts, nCats, nCrit) {
  stopifnot(bInd <= nCats && bInd > 0)
  stopifnot(aInd <= nAlts && aInd > 0)
  stopifnot(j <= nCrit && j > 0)

  offset <- nCrit + (nCrit * nAlts * nCats)
  return (offset + (j - 1) * nCats * nAlts + (aInd - 1) * nCats + bInd)
}

getWjIndex <- function(j) {
  return (j)
}

getNrBaseVars <- function(nAlts, nCrit, nAssignments, nCats) {
  return(getEpsilonIndex(nAlts=nAlts, nCrit=nCrit, nCats=nCats))
}
