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
      rel[i,j] = checkETRICRelation(performances, profiles, assignments, i, j, necessary=necessary))
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

  mat <- buildB1Constraint(nAlts, nCrit, nAssignments, nCats)
}

buildB1Constraint <- function(nAlts, nCrit, nAssignments, nCats) {
  for (j = 1 : nCrit) {
    
  }
}

## order is j1a1b1, j1a1b2, j2a1b1, ...
getCjABIndex <- function(j, aInd, bInd, nAlts, nCats) {
  offset <- (nAlts * nCats) * (j-1)
  index <- nCats * (aInd - 1) + bInd
  return (offset + index)
}

getCjBAIndex <- function(j, aInd, bInd, nAlts, nCats) {
  offset <- (nAlts * nCats) * (j-1)
  index <- nCats * (aInd - 1) + bInd
  return ((nAlts * nCats * j) + (offset + index))
}

getNrCjABA <- function(nCrit, nAlts, nCats) {
  return (nCrit * nAlts * nCats * 2)
}

getCjBt10Index <- function(nCrit, nAlts, nCats, j, t) {
  return getNrCjABA(nCrit, nAlts, nCats) + 
}
