library(libror)

p <- matrix(runif(n=50), nrow=10) # 10 alts, 5 crit
ror <- rorsmaa.create(p)

ror.addPreference(ror, 1, 2)
ror.addPreference(ror, 4, 5)
ror.addPreference(ror, 7, 8)
ror.addPreference(ror, 1, 3)

rorsmaa.compute(ror)

## Rejected samples for generating 10000 vf's
rorsmaa.getMisses(ror)

## Main results
rorsmaa.getRAIs(ror)
rorsmaa.getPOIs(ror)

## First value function, first partial value function
rorsmaa.getValueFunctionVals(ror, 1, 1)
rorsmaa.getValueFunctionEvals(ror, 1, 1)

## First value function, all partial value functions
rorsmaa.singleValueFunction(ror, 1)

allVf <- rorsmaa.allValueFunctions(ror) # this is quite slow, uses R code

## Evaluate second alternative with the first value function
rorsmaa.evaluateAlternative(ror, 1, 2)
