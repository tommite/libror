library(libror)

p <- matrix(runif(n=50), nrow=10) # 10 alts, 5 crit
ror <- utagms.create(p)
ror.addPreference(ror, 1, 2)
ror.addPreference(ror, 4, 5)
ror.addPreference(ror, 7, 8)
ror.addPreference(ror, 1, 3)

utagms.printModel(ror, TRUE, 1, 2)

if (!utagms.solve(ror)) { # if returns false, the pref. info is infeasible
  error("Preference information making model infeasible")
}

utagms.getNecessaryRelation(ror)
utagms.getPossibleRelation(ror)

# Try strictly monotonously increasing value functions
utagms.setStrictValueFunctions(ror, TRUE)
utagms.getPossibleRelation(ror)
