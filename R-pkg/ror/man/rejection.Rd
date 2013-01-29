\name{sample.vfs.rejection}
\alias{sample.vfs.rejection}
\title{Robust Ordinal Regression Value Function sampler}
\description{
  Pure rejection sampling of general monotone value functions.
  The current version assumes ascending preferences,
  i.e. higher criterion evaluation means higher preferability (=better).
}
\usage{sample.vfs.rejection(performances, preferences, nr=10000, updInterval=1000)}
\arguments{
\item{performances}{m x n performance matrix with m alternatives
and n criteria}
\item{preferences}{k x 2 matrix of preferences statements (row indices
of alternatives in performance matrix). Each row
r is a preference statements meaning that preferences[k,1] is preferred
to preferences[k,2]}
\item{nr}{The number of value functions to sample}
\item{updInterval}{Update interval for printing out current
  iteration. 0 = no information will be printed out}
}
\value{Named tuple, where 'vfs' is a list where element [[i]] are the
  value functions for the i'th criterion, one function per row and
  'misses' contains the amount of misses during value function sampling.}
\keyword{robust}
\seealso{rorsmaa,utagms,ror-package,sample.vfs.gibbs}
\examples{
# Set Java VM memory use to 2g not to run out of heap space
options( java.parameters = "-Xmx2g" )
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

vfs <- sample.vfs.rejection(performances, preferences, nr=10)
}
