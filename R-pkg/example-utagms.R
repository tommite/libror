library(libror)

p <- matrix(runif(n=50), nrow=10) # 10 alts, 5 crit
ror <- utagms.createROR(p)
ror.addPreference(ror, 1, 2)
ror.addPreference(ror, 4, 5)
ror.addPreference(ror, 7, 8)
ror.addPreference(ror, 1, 3)

utagms.printModel(ror, 0, 1)

utagms.solve(ror)
