library(libror)

nrSamples <- 10000
p <- matrix(runif(n=50), nrow=10) # 10 alts, 5 crit
ror <- smaaror.createROR(p, nrSamples)
smaaror.addPreference(ror, 1, 2)
smaaror.addPreference(ror, 4, 5)
smaaror.addPreference(ror, 7, 8)
smaaror.addPreference(ror, 1, 3)
smaaror.sampleROR(ror)
message(paste(smaaror.getMisses(ror), "rejected samples when generating", nrSamples, "value functions"))
