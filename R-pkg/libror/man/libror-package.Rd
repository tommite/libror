\name{libror-package}
\alias{libror-package}
\alias{libror}
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
\examples{
library(libror)

### UTAGMS ###
p <- matrix(runif(n=50), nrow=10) # 10 alts, 5 crit
ror <- utagms.create(p)
ror.addPreference(ror, 1, 2)

## To print constraints of individual model solved within UTAGMS
## utagms.printModel(ror, TRUE, 1, 2)

utagms.solve(ror)
utagms.getNecessaryRelation(ror)
utagms.getPossibleRelation(ror)

## Strictly monotonous (increasing) value functions
utagms.setStrictValueFunctions(ror, TRUE)
utagms.getPossibleRelation(ror)

### RORSMAA ###
sror <- rorsmaa.create(p)
ror.addPreference(sror, 1, 2)
rorsmaa.compute(sror)

## Rejected samples for generating 10000 vf's
rorsmaa.getMisses(sror)

## Main results
rorsmaa.getRAIs(sror)
rorsmaa.getPOIs(sror)

## First value function, first partial value function
rorsmaa.getValueFunctionVals(sror, 1, 1)
rorsmaa.getValueFunctionEvals(sror, 1, 1)

## First value function, all partial value functions
rorsmaa.singleValueFunction(sror, 1)

allVf <- rorsmaa.allValueFunctions(sror) # this is quite slow, uses R code

## Evaluate second alternative with the first value function
rorsmaa.evaluateAlternative(sror, 1, 2)

## is also an utagms-model, so the same functions can be used here
utagms.solve(sror)
utagms.getNecessaryRelation(sror)

}
