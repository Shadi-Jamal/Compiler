package de.uniwue.jpp.compiler.lowering;

import de.uniwue.jpp.compiler.error.CannotFindNameError;
import de.uniwue.jpp.compiler.syntax.AST;
import de.uniwue.jpp.compiler.syntax.Expression;

import java.util.ArrayList;
import java.util.List;

import de.uniwue.jpp.compiler.error.Error;
import de.uniwue.jpp.compiler.syntax.ExpressionKind;
import de.uniwue.jpp.compiler.syntax.TokenKind;


//Lowering
//
//One semantic technique that is obvious in hindsight (but took Andrei Alexandrescu to point out to me) is called "lowering."
// It consists of, internally, rewriting more complex semantic constructs in terms of simpler ones.
// For example, while loops and foreach loops can be rewritten in terms of for loops.
// Then, the rest of the code only has to deal with for loops.
// This turned out to uncover a couple of latent bugs in how while loops were implemented in D, and so was a nice win.
// It's also used to rewrite scope guard statements in terms of try-finally statements, etc.
// Every case where this can be found in the semantic processing will be win for the implementation.
//If it turns out that there are some special-case rules in the language that prevent this "lowering" rewriting,
// it might be a good idea to go back and revisit the language design.
//Any time you can find commonality in the handling of semantic constructs, it's an opportunity to reduce implementation effort and bugs.

public class Lowerer {

    AST ast;
    StackSize stackSize;
    List<LocalInfo> stackLocalInfoGetNameStack;
    List<Instruction> instructionList;
    List<Error> errorList;
    public Lowerer(AST ast) {
        this.ast=ast;
        this.instructionList=new ArrayList<>();
        this.stackSize=StackSize.create();
        this.stackLocalInfoGetNameStack =new ArrayList<>();
        this.errorList=new ArrayList<>();

    }

    public Lowerer(AST ast, StackSize stackSize) {
        this.ast=ast;
        this.stackSize=stackSize;
        this.stackLocalInfoGetNameStack =new ArrayList<>();
        this.errorList=new ArrayList<>();
        this.instructionList=new ArrayList<>();
    }

    public StackSize getStackSize() {
        return stackSize;
    }

    public List<LocalInfo> getNameStack() {
        return stackLocalInfoGetNameStack;
    }

    public List<Instruction> getInstructions() {
        return instructionList;
    }

    public List<Error> getErrors() {
        return errorList;
    }

    public void pushNamedLocal(int name, int offset) {
        getNameStack().add(new LocalInfo(name,offset));
    }

    public void popNamedLocal() {
        if (!stackLocalInfoGetNameStack.isEmpty()) {
            stackLocalInfoGetNameStack.removeLast();
        }
    }
    public int makeLocal() {
        return stackSize.allocate(1);
    }

    public Operand lowerNumber(Expression expression) {
        return new Operand(false,expression.getValue());
    }

    public Operand lowerIdentifier(Expression expression) {
        List<LocalInfo> nameStack = getNameStack();
        int value = expression.getValue();
        for (int i = nameStack.size()-1; i >=0 ; i--) {
            if (nameStack.get(i).getName()==value){
                return new Operand(true,nameStack.get(i).getOffset());
            }
        }
        errorList.add(new CannotFindNameError(value, expression.getLocation(ast)));
        return new Operand(true,-1);
    }

    public Operand lowerLet(Expression expression) {
        int allokiereEineLokaleVariableFürDasErgebnis = makeLocal();
        int currentSize = stackSize.getCurrentStackSize();
        Operand operand = lowerExpression(expression.getValue());
        instructionList.add(new Instruction(Operation.Op, allokiereEineLokaleVariableFürDasErgebnis,operand));
        stackSize.truncate(currentSize);
        pushNamedLocal(expression.getLeft(), allokiereEineLokaleVariableFürDasErgebnis);
        Operand operand1 = lowerExpression(expression.getRight() + 1);
        popNamedLocal();
        return operand1;
    }

    public Operand lowerBinary(Expression expression, Operation operation) {
        int allokiereEineLokaleVariableFürDasErgebnis = makeLocal();
        int currentSize = stackSize.getCurrentStackSize();
        Operand operandleft = lowerExpression(expression.getLeft());
        Operand operandright = lowerExpression(expression.getRight());
        instructionList.add(new Instruction(operation,allokiereEineLokaleVariableFürDasErgebnis,operandleft,operandright));
        stackSize.truncate(currentSize);
        return new Operand(true, allokiereEineLokaleVariableFürDasErgebnis);

    }

    public Operand lowerExpression(int expressionIndex) {
        if (ast.getExpression(expressionIndex).getKind() == ExpressionKind.Number)
            return lowerNumber(ast.getExpression(expressionIndex));
        else if (ast.getExpression(expressionIndex).getKind()==ExpressionKind.Identifier)
            return lowerIdentifier(ast.getExpression(expressionIndex));
        else if (ast.getExpression(expressionIndex).getKind()==ExpressionKind.Let) {
            return lowerLet(ast.getExpression(expressionIndex));
        }else if (ast.getExpression(expressionIndex).getKind() == ExpressionKind.Add){
            return lowerBinary(ast.getExpression(expressionIndex),Operation.Add);
        }else if (ast.getExpression(expressionIndex).getKind() == ExpressionKind.Sub)
            return lowerBinary(ast.getExpression(expressionIndex),Operation.Sub);
        else if (ast.getExpression(expressionIndex).getKind() == ExpressionKind.Mul) {
            return lowerBinary(ast.getExpression(expressionIndex),Operation.Mul);
        }else if (ast.getExpression(expressionIndex).getKind() == ExpressionKind.Div){
            return lowerBinary(ast.getExpression(expressionIndex),Operation.Div);
        } else  {
            return lowerBinary(ast.getExpression(expressionIndex),Operation.Mod);
        }
    }
}
