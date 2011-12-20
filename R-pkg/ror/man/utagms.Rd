\name{utagms}
\alias{utagms}
\title{UTA^{GMS} MCDA solver}
\description{
  Implements UTA^{GMS} robust ordinal regression: computes either
  the necessary- or the possible relation. Assumes ascending
  preferences, i.e. higher criterion evaluation means higher
  preferability (=better).
}
\usage{utagms(performances, preferences, necessary=TRUE,
strictVF=FALSE)}
\arguments{
\item{performances}{m x n performance matrix with m alternatives
and n criteria.}
\item{preferences}{k x 2 matrix of preferences statements (row indices
of alternatives in performance matrix). Each row
r is a preference statements meaning that preferences[k,1] is preferred
to preferences[k,2].}
\item{necessary}{Whether to compute the necessary relation (TRUE)
or the possible one (FALSE).}
\item{strictVF}{Whether to use sctrictly increasing (TRUE) or
monotonously increasing (FALSE) value functions.}
}
\keyword{robust}
\seealso{rorsmaa,ror-package}
\examples{
library(ror)

performances <- matrix(runif(n=50), nrow=10) # 10 alts, 5 crit
preferences <- matrix(c(1, 2, 4, 5, 7, 8, 1, 3), ncol=2, byrow=TRUE)

## Necessary relation
utagms(performances, preferences, necessary=TRUE, strictVF=TRUE)
## Possible relation
utagms(performances, preferences, necessary=FALSE, strictVF=TRUE)
}
