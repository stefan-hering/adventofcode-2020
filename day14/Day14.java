import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Day14 {
    private static void assign(BigInteger[] memory, String bitmask, Day14Parser.AssignmentContext assignment) {
        BigInteger result = new BigInteger(assignment.value().NUMBER().getText());
        for (int i = 0; i < bitmask.length(); i++) {
            char currentChar = bitmask.charAt(bitmask.length() - 1 - i);
            if (currentChar == 'X') {
                continue;
            }
            if (currentChar == '1') {
                result = result.setBit(i);
            } else {
                result = result.clearBit(i);
            }
        }
        memory[Integer.parseInt(assignment.index().NUMBER().getText())] = result;
    }

    private static void assign(Map<BigInteger, BigInteger> memory, String bitmask, Day14Parser.AssignmentContext assignment) {
        BigInteger value = new BigInteger(assignment.value().NUMBER().getText());
        BigInteger index = new BigInteger(assignment.index().NUMBER().getText());

        for (int i = 0; i < bitmask.length(); i++) {
            char currentChar = bitmask.charAt(bitmask.length() - 1 - i);
            if (currentChar == '1') {
                index = index.setBit(i);
            }
        }

        BigInteger modifiedIndex = index;

        permutate(bitmask)
                .map(mask -> {
                    BigInteger result = modifiedIndex;

                    for (int i = 0; i < mask.length(); i++) {
                        char currentChar = mask.charAt(mask.length() - 1 - i);
                        if (currentChar == 'A') {
                            result = result.setBit(i);
                        }
                        if (currentChar == 'B') {
                            result = result.clearBit(i);
                        }
                    }

                    return result;
                })
                .forEach(address -> memory.put(address, value));
    }

    private static Stream<String> permutate(String bitmask) {
        if (bitmask.contains("X")) {
            return List.of(
                    bitmask.replaceFirst("X", "A"),
                    bitmask.replaceFirst("X", "B"))
                    .stream()
                    .flatMap(it -> permutate(it));
        } else {
            return Stream.of(bitmask);
        }
    }

    private static void executeProgram(
            Day14Parser.ProgramContext program,
            BiConsumer<String, Day14Parser.AssignmentContext> assignFunction) {
        String bitmask = program.mask().BITMASK().getText();
        Consumer<Day14Parser.AssignmentContext> executeAssignment = (context) -> assignFunction.accept(bitmask, context);
        program.assignment().forEach(executeAssignment);
    }

    private static void countResult(Iterable<BigInteger> memory) {
        BigInteger result = BigInteger.ZERO;
        for (BigInteger val : memory) {
            if (val != null) {
                result = result.add(val);
            }
        }
        System.out.println(result);
    }

    private static void solveDay1(Day14Parser.ProgramsContext context) {
        BigInteger[] memory = new BigInteger[65536];

        BiConsumer<String, Day14Parser.AssignmentContext> executeAssignment =
                (bitmask, assignment) -> assign(memory, bitmask, assignment);
        Consumer<Day14Parser.ProgramContext> doExecuteProgram =
                (Day14Parser.ProgramContext program) -> executeProgram(program, executeAssignment);

        context.program().forEach(doExecuteProgram);

        countResult(Arrays.asList(memory));
    }

    private static void solveDay2(Day14Parser.ProgramsContext context) {
        Map<BigInteger, BigInteger> memory = new HashMap<>();

        BiConsumer<String, Day14Parser.AssignmentContext> executeAssignment =
                (bitmask, assignment) -> assign(memory, bitmask, assignment);
        Consumer<Day14Parser.ProgramContext> doExecuteProgram =
                (Day14Parser.ProgramContext program) -> executeProgram(program, executeAssignment);

        context.program().forEach(doExecuteProgram);

        countResult(memory.values());
    }

    private static Day14Parser.ProgramsContext getProgramsContext() throws IOException {
        var inputStream = CharStreams.fromStream(Day14.class.getClassLoader().getResourceAsStream("input"));
        var lexer = new Day14Lexer(inputStream);
        var stream = new CommonTokenStream(lexer);
        var parser = new Day14Parser(stream);
        return parser.programs();
    }

    /**
     * To run antrl4 -Dlanguage=Java Day14.g4
     * and put the antlr4 jar on the classpath
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        var context = getProgramsContext();
        solveDay1(context);
        solveDay2(context);
    }
}
