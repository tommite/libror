\name{rorsmaa}
\alias{rorsmaa}
\title{Robust Ordinal Regression SMAA sampler}
\description{
  Implements stochastic simulation of the indices used in a SMAA-type
  decision analysis with UTA^{GMS} models. The current version
  assumes ascending preferences, i.e. higher criterion evaluation
  means higher preferability (=better).
}
\usage{rorsmaa(performances, preferences, necessary=TRUE)}
\arguments{
\item{performances}{m x n performance matrix with m alternatives
and n criteria}
\item{preferences}{k x 2 matrix of preferences statements (row indices
of alternatives in performance matrix). Each row
r is a preference statements meaning that preferences[k,1] is preferred
to preferences[k,2]}
\item{necessary}{Whether to compute the necessary relation (TRUE)
or the possible one (FALSE)}
}
\keyword{robust}
\seealso{utagms,ror-package}
\examples{
library(ror)

## Function needed to generate pareto-optimal alternatives
randomPointFromHypersphere <- function(ncrit) {
  rns <- c()
  while(TRUE) {
    rns <- rnorm(ncrit)
    if (all(rns > 0)) {
      break
    }
  }
  mul <- 1 / sqrt(sum(rns * rns))
  return(rns * mul)
}

performances <- t(replicate(10, randomPointFromHypersphere(5)))  # 10 alts, 5 crit
preferences <- matrix(c(1, 2, 4, 5, 7, 8, 1, 3), ncol=2, byrow=TRUE)

## RORSMAA gives the POIs and RAIs
ror <- rorsmaa(performances, preferences)
print(ror$poi)
print(ror$rai)
cat(ror$misses, "misses while generating 10k value functions\n")
}
