package de.uniwue.jpp.compiler.eval;

import java.util.ArrayList;
import java.util.List;

import de.uniwue.jpp.compiler.lowering.Instruction;
import de.uniwue.jpp.compiler.lowering.Operand;
import de.uniwue.jpp.compiler.lowering.Operation;
import de.uniwue.jpp.compiler.syntax.ExpressionKind;

public class Evaluator {
    List<Instruction> instructions; int stackSize;int[] memory;
    public Evaluator(List<Instruction> instructions, int stackSize) {
        this.instructions=instructions;
        this.memory=new int[stackSize]; //Hierbei soll direkt schon der Speicher mit einer Größe von stackSize Integern erstellt werden.***********
    }

    public Evaluator(List<Instruction> instructions, int[] memory) {
        this.instructions=instructions;
        this.memory=memory;
    }

    public int[] getMemory() {
        return memory;
    }

    public int getValue(Operand operand) {
        if (operand.isLocal()){
            return memory[operand.getValue()];
        }
        else return operand.getValue();
    }



    public void evaluateInstruction(Instruction instruction) {
        if (instruction.getOperation()== Operation.Op){
            memory[instruction.getResult()] = getValue(instruction.getValue());
        } else if (instruction.getOperation()== Operation.Add) {
            memory[instruction.getResult()] = getValue(instruction.getLeft()) + getValue(instruction.getRight());
        }else if (instruction.getOperation()== Operation.Sub)
            memory[instruction.getResult()] = getValue(instruction.getLeft()) - getValue(instruction.getRight());
        else if (instruction.getOperation()== Operation.Mul)
            memory[instruction.getResult()] = getValue(instruction.getLeft()) * getValue(instruction.getRight());
        else if (instruction.getOperation()== Operation.Div) {
            memory[instruction.getResult()] = getValue(instruction.getLeft()) / getValue(instruction.getRight());
        }else if (instruction.getOperation()== Operation.Mod)
            memory[instruction.getResult()] = getValue(instruction.getLeft()) % getValue(instruction.getRight());
    }

    public void evaluate() {
        instructions.forEach(this::evaluateInstruction);
    }
}
