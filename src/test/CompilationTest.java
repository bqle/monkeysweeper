package test;

import monkeysweeper.Game;
import org.junit.jupiter.api.*;

import java.lang.reflect.*;

/**
 * This code makes sure that your minesweeper.Game class has a
 * {@code public static void main(String[] args)} method.
 * Compilation will fail if your minesweeper.Game requires additional libraries - feel free
 * to follow up on Piazza or via email if this is the case.
 */
public class CompilationTest {

    @Test

    public void testMain() {
        String error = ("Error: Your submission must include a class called " +
                "\"minesweeper.Game\" in package \"org.cis120\" with a main method:\n" +
                "   public static void main(String[] args)");

        Class<Game> gameClass = Game.class;
        if (gameClass == null) {
            System.out.println(error + "\nBut there was no minesweeper.Game class.");
            Assertions.fail(error + "\nBut there was no minesweeper.Game class.");
        }

        try {
            Class[] mainArgs = new Class[1];
            mainArgs[0] = (String[].class);

            Method gameMain = gameClass.getMethod("main", mainArgs);

            if (gameMain == null) {
                System.out.println(error + "\nBut no main method was found.");
                Assertions.fail(error + "\nBut no main method was found.");
            }

            if (!gameMain.getReturnType().toString().equals("void")) {
                System.out.println(
                        error
                                + "\nThe minesweeper.Game class's main method should have return type void."
                );
                Assertions.fail("The minesweeper.Game class's main method should have return type void.");
            }

            int modifiers = gameMain.getModifiers();

            if (!Modifier.isStatic(modifiers)) {
                System.out.println(error + "The main method should be static.");
                Assertions.fail("The minesweeper.Game class's main method should be static.");
            }
        } catch (NoSuchMethodException e) {
            System.out.println(error + "\nBut there was no main method or it had the wrong type.");
            Assertions.fail(error + "\nBut there was no main method or it had the wrong type.");
        } catch (Exception e) {
            System.out.println(
                    "Exception encountered while checking your minesweeper.Game class, please email the TAs."
            );
            e.printStackTrace();
            Assertions.fail("Exception encountered while checking your minesweeper.Game class, please email the TAs.");
        }

        System.out.println(
                "\n\nYour code compiles and your org.cis120.minesweeper.Game.main method has the right type."
        );
        System.out.println("Next step is the demo with your TA. \n\n");
    }
}
