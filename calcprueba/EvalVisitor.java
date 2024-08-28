import java.util.HashMap;
import java.util.Map;

public class CalculadoraVisitor extends LabeledExprBaseVisitor<Integer> {
    /** "memory" for our calculator; variable/value pairs go here */
    Map<String, Integer> memory = new HashMap<String, Integer>();

    /** ID '=' expr NEWLINE */
    @Override
    public Integer visitAssign(LabeledExprParser.AssignContext ctx) {
        String id = ctx.ID().getText();  // id is left-hand side of '='
        int value = visit(ctx.expr());   // compute value of expression on right
        memory.put(id, value);           // store it in our memory
        return value;
    }

    /** expr NEWLINE */
    @Override
    public Integer visitPrintExpr(LabeledExprParser.PrintExprContext ctx) {
        Integer value = visit(ctx.expr()); // evaluate the expr child
        System.out.println(value);         // print the result
        return 0;                          // return dummy value
    }

    /** INT */
    @Override
    public Integer visitInt(LabeledExprParser.IntContext ctx) {
        return Integer.valueOf(ctx.INT().getText());
    }

    /** ID */
    @Override
    public Integer visitId(LabeledExprParser.IdContext ctx) {
        String id = ctx.ID().getText();
        if ( memory.containsKey(id) ) return memory.get(id);
        return 0;
    }

    /** expr op=('*'|'/') expr */
    @Override
    public Integer visitMulDiv(LabeledExprParser.MulDivContext ctx) {
        int left = visit(ctx.expr(0));  // get value of left subexpression
        int right = visit(ctx.expr(1)); // get value of right subexpression

        if (ctx.op.getType() == LabeledExprParser.MUL) {
            return left * right;
        } else if (ctx.op.getType() == LabeledExprParser.DIV) {
            if (right == 0) {
                throw new ArithmeticException("Error: Division by zero");
            }
            return left / right;
        }
        return 0;  // Esta línea es redundante pero necesaria
    }

    /** expr op=('+'|'-') expr */
    @Override
    public Integer visitAddSub(LabeledExprParser.AddSubContext ctx) {
        int left = visit(ctx.expr(0));  // get value of left subexpression
        int right = visit(ctx.expr(1)); // get value of right subexpression
        if ( ctx.op.getType() == LabeledExprParser.ADD ) return left + right;
        return left - right; // must be SUB
    }

    /** expr op='^' expr (Potenciación) */
    @Override
    public Integer visitPow(LabeledExprParser.PowContext ctx) {
        int base = visit(ctx.expr(0));  // get base value
        int exponent = visit(ctx.expr(1)); // get exponent value
        return (int) Math.pow(base, exponent);
    }

    /** 'sqrt' expr (Raíz cuadrada) */
    @Override
    public Integer visitSqrt(LabeledExprParser.SqrtContext ctx) {
        int value = visit(ctx.expr());  // get value inside sqrt
        if (value < 0) {
            throw new ArithmeticException("Error: Square root of negative number");
        }
        return (int) Math.sqrt(value);
    }

    /** '(' expr ')' */
    @Override
    public Integer visitParens(LabeledExprParser.ParensContext ctx) {
        return visit(ctx.expr()); // return child expr's value
    }
}
