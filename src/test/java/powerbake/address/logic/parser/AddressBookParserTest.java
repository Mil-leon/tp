package powerbake.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static powerbake.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static powerbake.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static powerbake.address.testutil.Assert.assertThrows;
import static powerbake.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import powerbake.address.commons.core.index.Index;
import powerbake.address.logic.commands.AddClientCommand;
import powerbake.address.logic.commands.AddCommand;
import powerbake.address.logic.commands.ClearCommand;
import powerbake.address.logic.commands.DeleteCommand;
import powerbake.address.logic.commands.EditCommand;
import powerbake.address.logic.commands.EditCommand.EditPersonDescriptor;
import powerbake.address.logic.commands.ExitCommand;
import powerbake.address.logic.commands.FindCommand;
import powerbake.address.logic.commands.HelpCommand;
import powerbake.address.logic.commands.ViewCommand;
import powerbake.address.logic.parser.exceptions.ParseException;
import powerbake.address.model.person.Person;
import powerbake.address.testutil.EditPersonDescriptorBuilder;
import powerbake.address.testutil.PersonBuilder;
import powerbake.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddClientCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddClientCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " client " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand("client", INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone("91234567").build();
        EditCommand clientCommand = new EditCommand("client",
                Index.fromOneBased(1), descriptor, true);
        assertEquals(clientCommand, parser.parseCommand("edit client 1 -p 91234567"));
        assertThrows(ParseException.class, () -> parser.parseCommand("edit client 1 name -p 91234567"));
        assertThrows(ParseException.class, () -> parser.parseCommand("edit client -p 91234567"));
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        String commandWord = "client";
        List<String> args = Arrays.asList(commandWord, "foo", "bar", "baz");
        String[] keywords = args.subList(1, args.size()).toArray(new String[0]);
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + args.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(commandWord, keywords), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_viewClient() throws Exception {
        // Test for the "client" argument
        ViewCommand command = (ViewCommand) parser.parseCommand(ViewCommand.COMMAND_WORD + " client");
        assertEquals(new ViewCommand("client"), command);
    }

    @Test
    public void parseCommand_viewPastry() throws Exception {
        // Test for the "pastry" argument
        ViewCommand command = (ViewCommand) parser.parseCommand(ViewCommand.COMMAND_WORD + " pastry");
        assertEquals(new ViewCommand("pastry"), command);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
