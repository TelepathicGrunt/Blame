package com.telepathicgrunt.blame.main;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import com.telepathicgrunt.blame.Blame;
import com.telepathicgrunt.blame.utils.GeneralUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.text.MutableText;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Stack;

/* @author - TelepathicGrunt
 *
 * Detect if a command registered is broken due to calling .executes() outside an .then() call.
 *
 * LGPLv3
 */
public class BrokenCommandBlame {

    public static <S> void detectBrokenCommand(CommandDispatcher<CommandSource> builtCommand) {

        try {
            // Cannot use mixins as CommandDispatcher and CommandNode are com.mojang.brigadier classes which mixins cannot touch.
            Field root = CommandDispatcher.class.getDeclaredField("root");
            root.setAccessible(true);
            Field commandNodeChildren = CommandNode.class.getDeclaredField("children");
            commandNodeChildren.setAccessible(true);
            RootCommandNode<S> rootCommandNode = (RootCommandNode<S>) root.get(builtCommand);

            // keep track of the nodes we passed so we can get each of their names when we find a dead end node
            Stack<CommandNode<S>> nodeStack = new Stack<>();
            nodeStack.add(rootCommandNode);
            for (CommandNode<S> child : rootCommandNode.getChildren()) {
                nodeStack.add(child); // add to end of stack
                checkIfInvalidNode(child, commandNodeChildren, nodeStack);
                nodeStack.pop(); // pop the stack
            }

            // reset accessibility
            root.setAccessible(false);
            commandNodeChildren.setAccessible(false);
        }
        catch (NoSuchFieldException | IllegalAccessException x) {
            x.printStackTrace();
        }
    }

    private static <S> void checkIfInvalidNode(CommandNode<S> currentNode, Field commandNodeChildren, Stack<CommandNode<S>> currentNodePathStack) throws IllegalAccessException {
        if (currentNode.getCommand() != null) {
            for (CommandNode<S> child : currentNode.getChildren()) {
                currentNodePathStack.add(child); // add to end of stack
                checkIfInvalidNode(child, commandNodeChildren, currentNodePathStack);
                currentNodePathStack.pop(); // pop the stack
            }
        }
        else if (currentNode.getRedirect() != null) {
            CommandNode<S> redirectChild = currentNode.getRedirect();
            currentNodePathStack.add(redirectChild); // add to end of stack
            checkIfInvalidNode(redirectChild, commandNodeChildren, currentNodePathStack);
            currentNodePathStack.pop(); // pop the stack
        }
        else if (!currentNode.isFork() && currentNode.getChildren().size() == 0) {
            // We are looking for commands where they have no children, fork, redirect, and no command as those are dead ends.
            // All commands nodes should have either children or a command.

            CommandNode<S> childNode = currentNode;
            StringBuilder currentCommandPath = new StringBuilder();
            for (int i = currentNodePathStack.size() - 2; i >= 0; i--) {
                CommandNode<S> parentNode = currentNodePathStack.elementAt(i);
                Map<String, CommandNode<S>> mapOfChildren = (Map<String, CommandNode<S>>) commandNodeChildren.get(parentNode);
                currentCommandPath.insert(0, " ");
                currentCommandPath.insert(0, GeneralUtils.getKeysForMapValue(mapOfChildren, childNode).findFirst().orElse(null));
                childNode = parentNode;
            }

            Blame.LOGGER.log(Level.ERROR,
                    "\n****************** Blame Report " + Blame.VERSION + " ******************" +
                            "\n\n Detected a command that is broken. The command may have called .executes() outside a .then() call by mistake " +
                            "\n The broken command is : " + currentCommandPath);
        }
    }

    public static void printStacktrace(String commandString, Logger logger, Exception exception, MutableText mutableText) {
        Blame.LOGGER.log(Level.ERROR,
                "\n****************** Blame Report " + Blame.VERSION + " ******************" +
                        "\n\n A command broke. Here's the stacktrace of the failed command execution:\n");

        logger.error("Command exception: {}", commandString, exception);
        StackTraceElement[] astacktraceelement = exception.getStackTrace();

        for (int i = 0; i < Math.min(astacktraceelement.length, 5); ++i) {
            mutableText.append("\n\n").append(astacktraceelement[i].getMethodName()).append("\n ").append(astacktraceelement[i].getFileName()).append(":").append(String.valueOf(astacktraceelement[i].getLineNumber()));
        }
    }
}
