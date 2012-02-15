\name{maximalvectors.indices}
\alias{maximalvectors.indices}
\title{Maximal Vector index computation}
\description{
  Maximal Vector Computation using the BEST algorithm.
  The current version assumes ascending preferences,
  i.e. higher criterion evaluation means higher preferability (=better).
}
\usage{maximalvectors.indices(performances)}
\arguments{
\item{performances}{m x n performance matrix with m alternatives
and n criteria}
}
\value{Row indices of the non-dominated alternatives}
\keyword{robust}
\seealso{ror-package,maximalvectors}
\examples{
# Set Java VM memory use to 2g not to run out of heap space
options( java.parameters = "-Xmx2g" )
library(ror)

performances <- matrix(runif(n=50), nrow=10) # 10 alts, 5 crit

nonDominatedIdx <- maximalvectors.indices(performances)
}
