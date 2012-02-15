\name{maximalvectors}
\alias{maximalvectors}
\title{Maximal Vector Computation}
\description{
  Maximal Vector Computation using the BEST algorithm.
  The current version assumes ascending preferences,
  i.e. higher criterion evaluation means higher preferability (=better).
}
\usage{maximalvectors(performances)}
\arguments{
\item{performances}{m x n performance matrix with m alternatives
and n criteria}
}
\value{Matrix of performances of the non-dominated alternatives}
\keyword{robust}
\seealso{ror-package,maximalvectors.indices}
\examples{
# Set Java VM memory use to 2g not to run out of heap space
options( java.parameters = "-Xmx2g" )
library(ror)

performances <- matrix(runif(n=50), nrow=10) # 10 alts, 5 crit

nonDominated <- maximalvectors(performances)
}
