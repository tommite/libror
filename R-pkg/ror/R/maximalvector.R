maximalvectors <- function(performances) {
  mvc <- mvc.create(performances)
  mvc.computeBEST(mvc)
}

mvc.create <- function(perfMat) {
  mvc <- .jnew("fi/smaa/libror/r/MaximalVectorComputationRFacade",
                 as.vector(perfMat), as.integer(nrow(perfMat)))
  return(mvc)
}

mvc.computeBEST <- function(mvc) {
  .jcall(mvc,
         "[[D",
         method="computeBEST",
         simplify=TRUE)
}
