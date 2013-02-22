\name{utagms.strong.necessary}
\alias{utagms.strong.necessary}
\title{UTA^{GMS} MCDA solver strong necessary relation}
\description{
  Implements UTA^{GMS} robust ordinal regression: computes the
  strong necessary relation. Assumes ascending
  preferences, i.e. higher criterion evaluation means higher
  preferability (=better).
}
\usage{utagms.strong.necessary(performances, strongPrefs=NULL,
weakPrefs=NULL, indifPrefs=NULL, strictVF=FALSE)}
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

## Strong necessary relation
strongnec <- utagms.strong.necessary(performances, strongPrefs)

## Sanity check, the relation have a3 >^N a2
stopifnot(strongnec[3,2] == TRUE)

\dontrun{
## Plot the relation
plot(strongnec)
}

}
