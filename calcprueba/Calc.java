import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Calc {
    public static void main(String[] args) throws Exception {
        // Crear un CharStream para leer desde la entrada estándar (System.in)
        CharStream input = CharStreams.fromStream(System.in);
        
        // Crear un lexer con el CharStream de entrada
        LabeledExprLexer lexer = new LabeledExprLexer(input);
        
        // Crear un buffer de tokens para el lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        
        // Crear un parser con el buffer de tokens
        LabeledExprParser parser = new LabeledExprParser(tokens);
        
        // Crear un árbol de análisis sintáctico a partir de la regla 'stat'
        ParseTree tree = parser.stat();
        
        // Crear un visitor para evaluar las expresiones en el árbol
        EvalVisitor eval = new EvalVisitor();
        
        // Evaluar el árbol de análisis sintáctico
        eval.visit(tree);
    }
}
