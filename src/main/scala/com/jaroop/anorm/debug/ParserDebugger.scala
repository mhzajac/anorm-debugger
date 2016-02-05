package com.jaroop.anorm.debug

import anorm._

trait ParserDebugger {
    val dash = "-" * 100
    val star = "*" * 100
}

object RowParserDebugger extends ParserDebugger {

    def apply[A](parser: RowParser[A], name: String): RowParser[A] = new RowParser[A] {
        def apply(v1: Row): SqlResult[A] = {
            val result = parser(v1)
            println(s"$name : $result")
            result
        }
    }

    def group[A](parser: RowParser[A], name: String): RowParser[A] = new RowParser[A] {

        def apply(v1: Row): SqlResult[A] = {
            println(star)
            println(dash)
            println(s"RowParser: $name")
            println(dash)
            val result = parser(v1)
            println(star)
            result
        }

        private val listSize: List[A] => Int = _.size
        
        private val singleSize: A => Int = _ => 1

        private val optSize: Option[A] => Int = a => if(a.isDefined) 1 else 0

        override def * : ResultSetParser[List[A]] = ResultSetParserDebugger(super.*, s"${name}.*", listSize)

        override def + : ResultSetParser[List[A]] = ResultSetParserDebugger(super.+, s"${name}.+", listSize)

        override def single: ResultSetParser[A] = ResultSetParserDebugger(super.single, s"${name}.single", singleSize)

        override def singleOpt: ResultSetParser[Option[A]] = ResultSetParserDebugger(super.singleOpt, s"${name}.singleOpt", optSize)

    }

}

object ResultSetParserDebugger extends ParserDebugger {

    def apply[A](parser: ResultSetParser[A], name: String, count: A => Int): ResultSetParser[A] = new ResultSetParser[A] {

        def apply(v1: SqlParser.ResultSet): SqlResult[A] = {
            val result = parser(v1)
            val parsed = result match {
                case Success(a) => count(a)
                case Error(_) => 0
            }
            println(star)
            println(s"$name rows returned: ${v1.size}")
            println(s"$name rows parsed: ${parsed}")
            println(star)
            result
        }

    }

}
