\name{utagms}
\alias{utagms}
\title{UTA^{GMS} MCDA solver}
\description{
  Implements UTA^{GMS} robust ordinal regression: computes either
  the necessary- or the possible relation. Assumes ascending
  preferences, i.e. higher criterion evaluation means higher
  preferability (=better).
}
\usage{utagms(performances, strongPrefs=NULL, weakPrefs=NULL, indifPrefs=NULL, necessary=TRUE,
strictVF=FALSE)}
\arguments{
\item{performances}{m x n performance matrix with m alternatives
and n criteria.}
\item{strongPrefs}{k x 2 matrix of strong preferences statements (row indices
of alternatives in the performance matrix). Each row
r is a preference statements meaning that preferences[k,1] is strongly preferred
to preferences[k,2].}
\item{weakPrefs}{k x 2 matrix of weak preferences statements (row
  indices of alternatives in the performance matrix). Each row
r is a preference statements meaning that preferences[k,1] is weakly preferred
to preferences[k,2].}
\item{indifPrefs}{k x 2 matrix of indifference preferences statements (row
  indices of alternatives in the performance matrix). Each row
r is a preference statements meaning that preferences[k,1] is equally
preferred to preferences[k,2].}
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
## a3 > a2 (strongly preferred)
strongPrefs <- matrix(c(3, 2), ncol=2, byrow=TRUE)

## Necessary relation
necrel <- utagms(performances, strongPrefs, necessary=TRUE, strictVF=TRUE)
## Possible relation with strictly increasing value functions
posrel <- utagms(performances, strongPrefs, necessary=FALSE, strictVF=FALSE)

## Sanity check, the necessary relation should be
## T F F
## T T F
## T T T
stopifnot(necrel == matrix(c(TRUE, FALSE, FALSE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE), ncol=3, byrow=TRUE))

## Sanity check, the possible relation should be
## T T F
## T T F
## T T T
stopifnot(posrel == matrix(c(TRUE, TRUE, FALSE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE), ncol=3, byrow=TRUE))

## Test with a2 ~ a3
necrel <- utagms(performances, strongPrefs=NULL, indifPrefs=strongPrefs, necessary=TRUE, strictVF=TRUE)
## Sanity check, the necessary relation should represent (a2 ~ a3)
stopifnot(necrel[2,3] == necrel[3,2] && necrel[3,2] == TRUE)

\dontrun{

## Plot the relation
plot(necrel)

## a3 > a2 and a2 > a3
strongPrefs <- matrix(c(3, 2, 2, 3), ncol=2, byrow=TRUE)

## Error as the model is infeasible
necrel <- utagms(performances, strongPrefs, necessary=TRUE, strictVF=TRUE)
}
}
