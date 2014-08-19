package org.command4spring.serializer;

import org.command4spring.command.Command;
import org.command4spring.exception.CommandSerializationException;
import org.command4spring.result.Result;

/**
 * Convert a command/result to its text representation and back to object
 * @author pborbas
 *
 */
public interface Serializer {

    String toText(Command<?> command) throws CommandSerializationException;

    String toText(Result result) throws CommandSerializationException;

    Command<?> toCommand(String textCommand) throws CommandSerializationException;

    Result toResult(String textResult) throws CommandSerializationException;
}
