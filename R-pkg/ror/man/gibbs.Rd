\name{sample.vfs.gibbs}
\alias{sample.vfs.gibbs}
\title{Robust Ordinal Regression Value Function sampler}
\description{
  Rejection Gibbs sampling of general monotone value functions.
  The current version assumes ascending preferences,
  i.e. higher criterion evaluation means higher preferability (=better).
}
\usage{sample.vfs.gibbs(performances, preferences, nr=10000, thinning=1, updInterval=1000)}
\arguments{
\item{performances}{m x n performance matrix with m alternatives
and n criteria}
\item{preferences}{k x 2 matrix of preferences statements (row indices
of alternatives in performance matrix). Each row
r is a preference statements meaning that preferences[k,1] is preferred
to preferences[k,2]}
\item{nr}{The number of value functions to sample}
\item{thinning}{The thinning factor to use}
\item{updInterval}{Update interval for printing out current
  iteration. 0 = no information will be printed out}
}
\value{Named tuple, where 'vfs' is a list where element [[i]] are the
  value functions for the i'th criterion, one function per row and
  'misses' contains the amount of misses during value function sampling.}
\keyword{robust}
\seealso{rorsmaa,utagms,ror-package,sample.vfs.rejection}
\examples{
# Set Java VM memory use to 2g not to run out of heap space
options( java.parameters = "-Xmx2g" )
library(ror)

performances <- matrix(runif(n=50), nrow=10) # 10 alts, 5 crit
preferences <- matrix(c(1, 2, 4, 5, 7, 8, 1, 3), ncol=2, byrow=TRUE)

vfs <- sample.vfs.gibbs(performances, preferences, nr=10, thinning=2)
}
