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

class MacroSpec extends FlatSpec with Matchers with BeforeAndAfterEach {

    val interceptor = new Interceptor(Console.out)

    Console.setOut(interceptor)

    override def beforeEach(): Unit = {
        interceptor.reset()
    }

    "The parser debugger macro" should "transform a simple parser into a debugger and log the results" in {
        @debug.parser("Bar.parser")
        val parser: RowParser[Bar] = {
            get[Long]("bars.id") ~
            get[Boolean]("bars.baz") ~
            get[Option[String]]("bars.buzz") map { case id~baz~buzz =>
                Bar(id, baz, buzz)
            }
        }

        val expected = RowParserDebugger.star +
            RowParserDebugger.dash +
            "RowParser: Bar.parser" +
            RowParserDebugger.dash +
            """get[Long]("bars.id") : Success(1)""" +
            """get[Boolean]("bars.baz") : Success(true)""" +
            """get[Option[String]]("bars.buzz") : Success(None)""" +
            RowParserDebugger.star

        parser(Results.singleBarRow)

        interceptor.result should equal(expected)
    }

    it should "log a List ResultSetParser" in {
        @debug.parser("Bar.parser")
        val parser: RowParser[Bar] = {
            get[Long]("bars.id") ~
            get[Boolean]("bars.baz") ~
            get[Option[String]]("bars.buzz") map { case id~baz~buzz =>
                Bar(id, baz, buzz)
            }
        }

        val expected = RowParserDebugger.star +
        RowParserDebugger.dash +
        "RowParser: Bar.parser" +
        RowParserDebugger.dash +
        """get[Long]("bars.id") : Success(1)""" +
        """get[Boolean]("bars.baz") : Success(true)""" +
        """get[Option[String]]("bars.buzz") : Success(None)""" +
        RowParserDebugger.star +
        RowParserDebugger.star +
        RowParserDebugger.dash +
        "RowParser: Bar.parser" +
        RowParserDebugger.dash +
        """get[Long]("bars.id") : Success(2)""" +
        """get[Boolean]("bars.baz") : Success(true)""" +
        """get[Option[String]]("bars.buzz") : Success(Some(a))""" +
        RowParserDebugger.star +
        RowParserDebugger.star +
        RowParserDebugger.dash +
        "RowParser: Bar.parser" +
        RowParserDebugger.dash +
        """get[Long]("bars.id") : Success(3)""" +
        """get[Boolean]("bars.baz") : Success(false)""" +
        """get[Option[String]]("bars.buzz") : Success(Some(b))""" +
        RowParserDebugger.star +
        RowParserDebugger.star +
        """Bar.parser.* rows returned: 3""" +
        """Bar.parser.* rows parsed: 3""" +
        RowParserDebugger.star

        parser.*(Results.listBarMeta)

        interceptor.result should equal(expected)
    }

}
