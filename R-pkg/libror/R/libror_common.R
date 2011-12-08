ror.addPreference <- function(ror, a, b) {
  .jcall(ror, "V", method="addPreference", as.integer(a), as.integer(b))
}
