package fr.badblock.apicommon.permissions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fr.badblock.apicommon.permissions.Permission.PermissionResult;

public class PermissionTest
{
	@Test
	public void testSamePermission()
	{
		Permission a = new Permission("bon.jour");
		Permission b = new Permission("bon.jour");

		assertEquals(a.compare(b), PermissionResult.YES);
	}
	
	@Test
	public void testInversePermission()
	{
		Permission a = new Permission("bon.jour");
		Permission b = new Permission("-bon.jour");

		assertEquals(a.compare(b), PermissionResult.NO);
	}

	@Test
	public void testInversePermission2()
	{
		Permission a = new Permission("-bon.jour");
		Permission b = new Permission("bon.jour");

		assertEquals(a.compare(b), PermissionResult.NO);
	}
	
	@Test
	public void testDifferentPermission()
	{
		Permission a = new Permission("bon.jour");
		Permission b = new Permission("bon.sang");

		assertEquals(a.compare(b), PermissionResult.UNKNOWN);
	}
	
	@Test
	public void testPermissionMatchAll()
	{
		Permission a = new Permission("*");
		Permission b = new Permission("bon.jour");

		assertEquals(a.compare(b), PermissionResult.YES);
	}

	@Test
	public void testAllDontMatchPermission()
	{
		Permission a = new Permission("bon.jour");
		Permission b = new Permission("*");

		assertEquals(a.compare(b), PermissionResult.UNKNOWN);
	}

	@Test
	public void testPermissionMatchSubAll()
	{
		Permission a = new Permission("bon.*");
		Permission b = new Permission("bon.jour");

		assertEquals(a.compare(b), PermissionResult.YES);
	}

	@Test
	public void testPermissionMatchSubAllInverse()
	{
		Permission a = new Permission("-bon.*");
		Permission b = new Permission("bon.jour");

		assertEquals(a.compare(b), PermissionResult.NO);
	}
}
