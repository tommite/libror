ror.addPreference <- function(ror, a, b) {
  a = a-1
  b = b-1
  .jcall(ror$model, "V", method="addPreference", as.integer(a), as.integer(b))
}
