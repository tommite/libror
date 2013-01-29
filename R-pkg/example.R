library(ror)

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

## Sample a few value functions with thinning 20, Gibbs sampler
vfs <- sample.vfs.gibbs(performances, preferences, 10, 20);

## Necessary relation
nec <- utagms(performances, preferences, necessary=TRUE, strictVF=TRUE)
print(nec)
## Possible relation
pos <- utagms(performances, preferences, necessary=FALSE, strictVF=TRUE)
print(pos)

## RORSMAA giving the POIs and RAIs
ror <- rorsmaa(performances, preferences)
print(ror$poi)
print(ror$rai)
cat(ror$misses, "misses while generating 10k value functions\n")

## Example with 3 alternatives and 3 criteria
perf2 <- matrix(c(1.0, 1.0, 1.0, 2.0, 1.0, 1.1, 2.0, 0.5, 3.0), ncol=3, byrow=TRUE)
## a3 > a2
pref2 <- matrix(c(3, 2), ncol=2, byrow=TRUE)

nec2 <- utagms(perf2, pref2, necessary=TRUE, strictVF=FALSE)
## sanity check, the matrix should be
## T F F
## T T F
## T T T
stopifnot(nec2 == matrix(c(TRUE, FALSE, FALSE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE), ncol=3, byrow=TRUE))

## Compute maximal vectors
nonDominated <- maximalvectors(performances)
