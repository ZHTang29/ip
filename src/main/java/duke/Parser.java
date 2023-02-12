package duke;

import duke.Command.*;
import duke.Exception.DukeException;
import duke.Exception.IncorrectArgumentsException;
import duke.Exception.InvalidArgumentsException;
import duke.Exception.InvalidInputException;

/**
 * Main class that parses the users inputs.
 */
public class Parser {

    /**
     * Takes in the users input and splits it into single word inputs and multi-word inputs to separately handle.
     * @param command Users input.
     * @return If the user input is valid, return the corresponding (executable) command.
     * @throws DukeException If the user input is invalid, throw the corresponding exception.
     */
    public Command parseCommand(String command) throws DukeException {
        String[] tokens = command.split(" ", 2);
        if (tokens.length == 1) {
            return handleSingleInput(tokens[0]);
        } else {
            return handleMultiInput(tokens[0], tokens[1]);
        }
    }

    /**
     * For single word user input, check if user input is a valid single word command.
     * If valid, execute it or else throw a corresponding error.
     * @param command The users input.
     * @return if the user input is a valid command, return the corresponding (executable) command.
     * @throws InvalidInputException If the user input is not a supported command, throw this exception.
     * @throws IncorrectArgumentsException If the user input is a supported command, but with the wrong number of
     * arguments, throw this exception.
     */
    public Command handleSingleInput(String command) throws InvalidInputException,
            IncorrectArgumentsException {
        switch (command) {
            case "bye":
                return new ByeCommand();
            case "list":
                return new ListCommand();
            case "mark":
            case "unmark":
            case "delete":
            case "todo":
                throw new IncorrectArgumentsException(command, 1, 0);
            case "deadline":
                throw new IncorrectArgumentsException(command, 2, 0);
            case "event":
                throw new IncorrectArgumentsException(command, 3, 0);
            default:
                throw new InvalidInputException(command);
        }
    }

    /**
     * For multi-word user input, check if user input is a valid multi-word command.
     * If valid, execute it or else throw a corresponding error.
     * @param command The first word of the users input.
     * @param details The subsequent words of the user input.
     * @return if the user input is a valid command, return the corresponding (executable) command.
     * @throws InvalidInputException If the user input is not a supported command, throw this exception.
     * @throws IncorrectArgumentsException If the user input is a supported command, but with the wrong number of
     * arguments, throw this exception.
     * @throws InvalidArgumentsException If the user input is a supported command, but with the arguments given in
     * a wrong format, throw this exception.
     */
    public Command handleMultiInput(String command, String details) throws IncorrectArgumentsException,
            InvalidInputException, InvalidArgumentsException {
        switch (command) {
            case "bye":
            case "list":
                throw new IncorrectArgumentsException(command, 0, 1);
            case "mark":
                int numToMark = Integer.parseInt(details);
                return new MarkCommand(numToMark);
            case "unmark":
                int numToUnmark = Integer.parseInt(details);
                return new UnmarkCommand(numToUnmark);
            case "delete":
                int numToDelete = Integer.parseInt(details);
                return new DeleteCommand(numToDelete);
            case "todo":
                return new ToDoCommand(details);
            case "deadline":
                String[] deadlineTokens = details.split("/by");
                if (deadlineTokens.length != 2) {
                    throw new IncorrectArgumentsException("deadline", 2, deadlineTokens.length);
                }
                String DLtaskName = deadlineTokens[0];
                String DLdueTime = deadlineTokens[1];
                return new DeadlineCommand(DLtaskName, DLdueTime);
            case "event":
                String[] eventTokens = details.split("/from");
                if (eventTokens.length != 2) {
                    throw new IncorrectArgumentsException("event", 3, eventTokens.length + 1);
                }
                String[] eventTokens1 = eventTokens[1].split("/to");
                if (eventTokens1.length != 2) {
                    throw new IncorrectArgumentsException("event", 3, eventTokens.length + eventTokens1.length - 1);
                }
                String EtaskName = eventTokens[0];
                String EstartTime = eventTokens1[0];
                String EendTime = eventTokens1[1];
                return new EventCommand(EtaskName, EstartTime, EendTime);
            default:
                throw new InvalidInputException(command);
        }
    }
}
