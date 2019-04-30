package fr.badblock.gameapi.commands;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import fr.badblock.gameapi.commands.example.CommandAdd;
import fr.badblock.gameapi.commands.example.CommandAddMultiple;
import fr.badblock.gameapi.commands.example.CommandPex;
import fr.badblock.gameapi.commands.example.ExampleReceiver;

public class CommandNodeTest 
{
	private CommandDispatcher<ExampleReceiver> dispatcher;
	private ExampleReceiver receiver;
	
	@Before
	public void beforeTests()
	{
		dispatcher = new CommandDispatcher<>();
		receiver = new ExampleReceiver();
	}
	
	@Test
	public void testSimpleCommand() throws CommandSyntaxException
	{
		dispatcher.register(new CommandAdd().createCommand());

		String[] usage = dispatcher.getAllUsage(dispatcher.getRoot(), receiver, false);

		assertTrue(Arrays.deepEquals(usage, new String[] {
			"add <a> <b>"
		}));

		dispatcher.execute("add 10 20", receiver);
		assertEquals(30, receiver.val);
	}
	
	@Test
	public void testCommandOptional() throws CommandSyntaxException
	{
		dispatcher.register(new CommandAddMultiple().createCommand());

		dispatcher.execute("add_m 10 20", receiver);
		assertEquals(30, receiver.val);
		
		dispatcher.execute("add_m 10 20 30", receiver);
		assertEquals(60, receiver.val);
		
		dispatcher.execute("add_m 10 20 30 40", receiver);
		assertEquals(100, receiver.val);
	}

	@Test
	public void testTree() throws CommandSyntaxException
	{
		dispatcher.register(new CommandPex().createCommand());

		dispatcher.execute("pex user LeLanN permission add *", receiver);
		assertEquals("add * to LeLanN", receiver.sVal);

		dispatcher.execute("pex user LeLanN permission remove *", receiver);
		assertEquals("remove * to LeLanN", receiver.sVal);

		dispatcher.execute("pex user LeLanN groups", receiver);
		assertEquals("LeLanN groups: a, b", receiver.sVal);

		dispatcher.execute("pex groups", receiver);
		assertEquals("groups: a, b, c", receiver.sVal);
	}
}
