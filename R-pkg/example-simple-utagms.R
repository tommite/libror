library(libror)

m <- matrix(c(1, 1, 1, 2, 1, 1.1, 2, 0.5, 3), nrow=3, byrow=TRUE)
pref <- matrix(c(3, 2), nrow=1)

utagms.buildRelation(m, pref, TRUE, TRUE)
