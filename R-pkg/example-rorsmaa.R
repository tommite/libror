library(libror)

nrSamples <- 10000
p <- matrix(runif(n=50), nrow=10) # 10 alts, 5 crit
ror <- rorsmaa.create(p, nrSamples)
ror.addPreference(ror, 1, 2)
ror.addPreference(ror, 4, 5)
ror.addPreference(ror, 7, 8)
ror.addPreference(ror, 1, 3)
rorsmaa.sample(ror)
message(paste(rorsmaa.getMisses(ror), "rejected samples when generating", nrSamples, "value functions"))
