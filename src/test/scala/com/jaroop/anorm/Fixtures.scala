package test

case class Foo(id: Long, name: String, bar: Bar)

case class Bar(id: Long, baz: Boolean, buzz: Option[String])
