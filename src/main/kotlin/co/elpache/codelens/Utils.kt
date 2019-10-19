package co.elpache.codelens


fun Boolean.then(cb: ()->Unit): Boolean {
  if(this) cb()
  return this
}
