package com.jaroop.anorm.debug

import anorm._
import scala.annotation.{ StaticAnnotation, compileTimeOnly }
import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context

@compileTimeOnly("Enable macro paradise to expand macro annotations.")
class parser(name: String) extends StaticAnnotation {

    def macroTransform(annottees: Any*): Any = macro parserDebugMacro.impl

}

object parserDebugMacro {

    def impl(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
        import c.universe._

        val helper = new { val ctx: c.type = c } with Helper

        val annotationType = tq"com.jaroop.anorm.debug.parser"

        // Name of the debugger
        val name: String = c.prefix.tree match {
            case q"new $annotationType($nme)" => c.eval[String](c.Expr(nme))
        }

        val result = annottees.map(_.tree) match {
            case (valDef @ q"$mods val $tname: $tpt = $expr.map($func)") :: Nil => {
                val debugger = helper.replace(helper.split(expr, Nil))
                q"$mods val $tname: $tpt = com.jaroop.anorm.debug.RowParserDebugger.group($debugger.map($func), ${name.toString})"
            }
            case (defDef @ q"$mods def $tname: $tpt = $expr.map($func)") :: Nil => {
                val debugger = helper.replace(helper.split(expr, Nil))
                q"$mods def $tname: $tpt = com.jaroop.anorm.debug.RowParserDebugger.group($debugger.map($func), ${name.toString})"
            }
            case _ => c.abort(c.enclosingPosition, "???")
        }

        c.Expr[Any](result)
    }

}

private[debug] abstract class Helper {

    val ctx: Context

    import ctx.universe._

    // Replace each component parser with a debugger parser
    @scala.annotation.tailrec
    final def split(tree: ctx.Tree, acc: List[ctx.Tree]): List[ctx.Tree] = {
        tree match {
            case q"$chain.~($single)" => split(chain, single :: acc)
            case q"$single" => single :: acc
        }
    }

    final def replace(parsers: List[ctx.Tree]): ctx.Tree = {
        parsers.map(tree => (tree.toString, tree))
            .map { case (name, parser) => q"com.jaroop.anorm.debug.RowParserDebugger($parser, $name)" }
            .reduceLeft((a, b) => q"$a.~($b)")
    }

}