library(ror)

performances <- matrix(runif(n=50), nrow=10) # 10 alts, 5 crit
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

## Example with 3 alternatives and 3 criteria
perf2 <- matrix(c(1.0, 1.0, 1.0, 2.0, 1.0, 1.1, 2.0, 0.5, 3.0), ncol=3, byrow=TRUE)
## a3 > a2
pref2 <- matrix(c(3, 2), ncol=2, byrow=TRUE)

nec2 <- utagms(perf2, pref2, necessary=TRUE, strictVF=FALSE)
## sanity check, the matrix should be
## T F F
## T T F
## T T T
stopifnot(nec2 == matrix(c(T, F, F, T, T, F, T, T, T), ncol=3, byrow=TRUE))
