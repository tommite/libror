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

## Example with 3 alternatives and 3 criteria
performances <- matrix(c(1.0, 1.0, 1.0, 2.0, 1.0, 1.1, 2.0, 0.5, 3.0), ncol=3, byrow=TRUE)
## a3 > a2
preferences <- matrix(c(3, 2), ncol=2, byrow=TRUE)

## Necessary relation
necrel <- utagms(performances, preferences, necessary=TRUE, strictVF=TRUE)
## Possible relation
posrel <- utagms(performances, preferences, necessary=FALSE, strictVF=FALSE)

## Sanity check, the necessary relation should be
## T F F
## T T F
## T T T
stopifnot(necrel == matrix(c(TRUE, FALSE, FALSE, TRUE, TRUE, FALSE,
TRUE, TRUE, TRUE), ncol=3, byrow=TRUE))

## Sanity check, the possible relation should be
## T T F
## T T F
## T T T
stopifnot(posrel == matrix(c(TRUE, TRUE, FALSE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE), ncol=3, byrow=TRUE))
}
