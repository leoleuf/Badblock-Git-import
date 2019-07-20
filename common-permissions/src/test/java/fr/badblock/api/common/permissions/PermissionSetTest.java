package fr.badblock.api.common.permissions;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import fr.badblock.api.common.permissions.PermissionSet;

public class PermissionSetTest
{
	@Test
	public void testCompatible()
	{
		PermissionSet set = new PermissionSet(Arrays.asList("bungee"), Arrays.asList(), new HashMap<>(), new HashMap<>());
		assertTrue(set.isCompatible("bungee"));
	}

	@Test
	public void testNotCompatible()
	{
		PermissionSet set = new PermissionSet(Arrays.asList("bungee"), Arrays.asList(), new HashMap<>(), new HashMap<>());
		assertFalse(set.isCompatible("coincoin"));
	}

	@Test
	public void testCompatibleAll()
	{
		PermissionSet set = new PermissionSet(Arrays.asList("*", "bungee"), Arrays.asList(), new HashMap<>(), new HashMap<>());
		assertTrue(set.isCompatible("bungee"));
		assertTrue(set.isCompatible("coincoin"));
	}

	@Test
	public void testCompatibleExcludingAll()
	{
		PermissionSet set = new PermissionSet(Arrays.asList("*", "bungee"), Arrays.asList(), new HashMap<>(), new HashMap<>());
		assertTrue(set.isCompatibleExcludingAll("bungee"));
		assertFalse(set.isCompatibleExcludingAll("coincoin"));
	}
}
