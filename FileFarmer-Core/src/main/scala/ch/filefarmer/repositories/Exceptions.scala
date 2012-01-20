package ch.filefarmer.repositories

case class DuplicateException(val msg:String) extends Exception(msg)
case class ArgumentInvalidException(val msg:String) extends Exception(msg)