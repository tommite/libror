\name{ror-package}
\alias{ror-package}
\alias{ror}
\docType{package}
\title{Robust Ordinal Regression MCDA sampler/solver}
\description{
  This package implements UTAGMS and RORSMAA MCDA methods
  for ranking multiple alternatives in terms of multiple criteria.
  The current version assumes ascending preferences, i.e. higher
  criterion evaluation means higher preferability (=better).
}
\author{
  Tommi Tervonen

  Maintainer: Tommi Tervonen <tommi@smaa.fi>
}
\references{
  Greco, S., Mousseau, V., Slowinski R., 2008. Ordinal regression
  revisited: multiple criteria ranking using a set of additive value
  functions. Eur J Oper Res 191 (2), 415.

  Kadzinski, M., Tervonen, T., 2012. Stochastic Ordinal Regression for
  Multiple Criteria Decision Support. Submitted manuscript.
}
\keyword{robust}
\seealso{rorsmaa,utagms,sample.vfs.gibbs,sample.vfs.rejection}
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

## Necessary relation
utagms(performances, preferences, necessary=TRUE, strictVF=TRUE)
## Possible relation
utagms(performances, preferences, necessary=FALSE, strictVF=TRUE)

## RORSMAA giving the POIs and RAIs
ror <- rorsmaa(performances, preferences)
print(ror$poi)
print(ror$rai)
cat(ror$misses, "misses while generating 10k value functions")

## Sample some value functions
vfs <- sample.vfs.gibbs(performances, preferences, nr=10, thinning=2)
}
