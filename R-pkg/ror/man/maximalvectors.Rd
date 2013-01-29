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

## Test with pareto-optimal alternatives
performances <- matrix(c(0.1823507, 0.5232321, 0.7595968, 0.2964752,
0.1676054, 0.5408093, 0.1604821,0.4699517, 0.4170541, 0.5357071,
0.1292226, 0.2366909, 0.7583132, 0.3765545, 0.4587448), ncol=5, byrow=TRUE)
nonDominated <- maximalvectors(performances)
stopifnot(nrow(nonDominated) == 3)
}
