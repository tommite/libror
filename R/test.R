library('rJava')
.jinit(classpath="../target/rorsample-0.1-SNAPSHOT-jar-with-dependencies.jar")

createROR <- function(perfMat) {
  .jnew("fi/smaa/rorsample/RORSamplerRFacade", as.vector(perfMat), as.integer(nrow(perfMat)), as.integer(10))
}
                                                               
p <- matrix(c(1, 2, 3, 4), nrow=2)
ror <- createROR(p)
.jcall(ror, method="sample")

