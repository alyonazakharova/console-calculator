import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String input;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter an expression...\nType \"clear\" to start a new expression. Or type \"quit\" to quit this cool calculator");

            boolean ok = false;
            String expression;
            while (!ok) {
                expression = scanner.nextLine().replaceAll(" ", "");
                if ("quit".equalsIgnoreCase(expression)) {
                    return;
                }
                try {
                    ExpressionProcessor.parseExpression(expression);
                    ok = true;
                    System.out.println("OK");
                } catch (Exception e) {
                    System.out.println("Invalid input: " + e.getMessage());
                }
            }

            while (true) {
                input = scanner.nextLine().replaceAll(" ", "");
                if ("quit".equalsIgnoreCase(input)) {
                    return;
                } else if ("calculate".equalsIgnoreCase(input)) {
                    if (ExpressionProcessor.getVariables().containsValue(null)) {
                        System.out.println("ERROR. You have to set all variables values");
                    } else {
                        System.out.println("Result: " + ExpressionProcessor.calculate());
                    }
                } else if ("print".equalsIgnoreCase(input)) {
                    ExpressionProcessor.buildTree();
                } else if ("clear".equalsIgnoreCase(input)) {
                    break;
                } else {
                    Pattern pattern = Pattern.compile("(.+)=(.+)");
                    Matcher matcher = pattern.matcher(input);
                    if (matcher.find()) {
                        String variableName = matcher.group(1);
                        short variableValue;
                        try {
                            variableValue = Short.parseShort(matcher.group(2));
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Unable to parse " + variableName + " into number");
                            continue;
                        }
                        if (ExpressionProcessor.getVariables().containsKey(variableName)) {
                            ExpressionProcessor.getVariables().put(variableName, variableValue);
                        } else {
                            System.out.println("Invalid input. Expression does not contain this variable");
                        }
                        System.out.println("OK");
                    } else {
                        System.out.println("Invalid input");
                    }
                }
            }
        }
    }
}
