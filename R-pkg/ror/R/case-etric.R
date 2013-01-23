library(ROI.plugin.glpk)
solvers <- ROI_installed_solvers()
if (!is.na(solvers['symphony'])) {
  .solver <<- 'symphony'
} else if (!is.na(solvers['glpk'])) {
  .solver <<- 'glpk'
} else {
  stop("No ROI Symphony or GLPK plugin installed")
}

source('ror_common.R')
source('etricror.R')

perfs <- read.table(file="alts.csv", sep=",", header=TRUE)
rownames(perfs) = perfs[,1]
perfs = perfs[,2:6]


profs <- read.table(file="profs.csv", sep=",", header=FALSE)
rownames(profs) = profs[,1]
profs = profs[,2:6]
colnames(profs) <- colnames(perfs)

assigs <- matrix(c(
                   1, 1,
#                   7, 2,
                   8, 2,
                   13, 2,
                   16, 3,
                   18, 3,
#                   27, 3,
                   31, 4,
                   33, 4,
                   40, 4)
                   ,ncol=2, byrow=TRUE)

thresholds <- matrix(c(
                      0, 0.01, 0, 0.02, FALSE,
                      0, 0, 1.9, 0, FALSE,
                      0, 0, 1.9, 0, FALSE,
                      0, 0, 1.9, 0, FALSE,
                      0, 0, 2, 0, FALSE),ncol=5, byrow=TRUE)

message("--- starting tests")
#nec <- etricror(perfs, profs, assigs, TRUE, phi=thresholds)
pos <- etricror(perfs, profs, assigs, FALSE, phi=thresholds)

#print(nec)
print(pos)
