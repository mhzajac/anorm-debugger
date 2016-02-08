package test

import anorm._, SqlParser._
import com.jaroop.anorm.debug, debug._
import java.io.{ OutputStream, PrintStream }
import org.scalatest._

class Interceptor(out: OutputStream) extends PrintStream(out, true) {

    var result: String = ""

    override def print(s: String): Unit = {
        result += s
        super.print(s)
    }

    def reset(): Unit = result = ""

}

class MacroSpec extends FlatSpec with Matchers {

    val interceptor = new Interceptor(Console.out)

    Console.setOut(interceptor)

    "The parser debugger macro" should "transform a simple parser into a debugger and log the results" in {
        interceptor.reset()

        @debug.parser("Foo.parser")
        val parser: RowParser[Bar] = {
            get[Long]("bars.id") ~
            get[Boolean]("bars.baz") ~
            get[Option[String]]("bars.buzz") map { case id~baz~buzz =>
                Bar(id, baz, buzz)
            }
        }

        val expected = RowParserDebugger.star +
            RowParserDebugger.dash +
            "RowParser: Foo.parser" +
            RowParserDebugger.dash +
            """get[Long]("bars.id") : Success(1)""" +
            """get[Boolean]("bars.baz") : Success(true)""" +
            """get[Option[String]]("bars.buzz") : Success(None)""" +
            RowParserDebugger.star

        parser(Results.metaData)

        interceptor.result should equal(expected)
    }

}
