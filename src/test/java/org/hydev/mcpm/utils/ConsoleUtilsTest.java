package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Tests for ConsoleUtils
 */
class ConsoleUtilsTest
{
    @Test
    @Tag("IntegrationTest")
    void move()
    {
        // NOTE: You must be on a Xterm-compatible TTY for this test to work, which is why it's
        // impossible to unit-test, and it can't even show up properly on gradle
        var cu = new ConsoleUtils();
        cu.clear();
        System.out.println("Hello World");

        // This will print "Meow"
        System.out.println("Hello Hell");
        cu.curUp(1);
        cu.eraseLine();
        System.out.println("Meow");

        // This will print "Hello Cell"
        System.out.println("Hello Hell");
        cu.curUp(1);
        System.out.println("Hello C");

        // This will print "Erased"
        System.out.print("This line is erased");
        cu.eraseLine();
        System.out.println("Erased");
    }

    public static void main(String[] args)
    {
        new ConsoleUtilsTest().move();
    }
}
