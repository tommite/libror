ror.addPreference <- function(ror, a, b) {
  a = a-1
  b = b-1
  .jcall(ror$model, "V", method="addPreference", as.integer(a), as.integer(b))
}

.doubleArrayToMatrix <- function(doubleArray) {
  mat <- c();
  for (row in doubleArray) {
    mat <- rbind(mat, .jevalArray(row))
  }
  return(mat)
}
