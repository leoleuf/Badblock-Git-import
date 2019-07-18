package fr.badblock.apicommon.permissions;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.badblock.apicommon.permissions.Permission.PermissionResult;
import lombok.ToString;

/**
 * Permissible
 * @author LeLanN
 */
@ToString
public class Permissible
{

	@SuppressWarnings("serial")
	Type inheritanceList = new TypeToken<List<String>>() {}.getType();
	@SuppressWarnings("serial")
	Type permissionList = new TypeToken<List<PermissionSet>>() {}.getType();

	private String				name;
	private int					power;
	private List<String>		inheritances;
	private List<PermissionSet>	permissions;

	public Permissible(String name, List<String> inheritances, List<PermissionSet> permissions, boolean displayable, int power)
	{
		this.name = name;
		this.inheritances = inheritances;
		this.permissions = permissions;
		this.power = power;
	}

	public Permissible()
	{
		this.name = "default";
		this.inheritances = new ArrayList<>();
		this.permissions = new ArrayList<>();
		this.power = 0;
	}

	public Permissible(JsonObject jsonObject)
	{
		Gson gson = new Gson();

		name = jsonObject.get("name").getAsString();
		inheritances = gson.fromJson(jsonObject.get("inheritances"), inheritanceList);
		permissions = gson.fromJson(jsonObject.get("permissions"), permissionList);
		power = jsonObject.get("power").getAsInt();
	}
	
	public List<PermissionSet> getPermissions()
	{
		return this.permissions;
	}

	public String getName()
	{
		return name;
	}
		
	/**
	 * Get the group power
	 * @return An integer
	 */
	public int getPower()
	{
		return power;
	}
	
	private PermissionSet getNameSet()
	{
		for (PermissionSet set : permissions)
		{
			if (set.isCompatibleExcludingAll() && set.isDisplayable())
				return set;
		}
		
		for (PermissionSet set : permissions)
		{
			if (set.isCompatible() && set.isDisplayable())
				return set;
		}
		
		return null;
	}

	public boolean isDisplayable()
	{
		return getNameSet() != null;
	}
	
	public String getPrefix()
	{
		PermissionSet set = getNameSet();
		return set == null ? null : set.getPrefix();
	}

	public String getSuffix()
	{
		PermissionSet set = getNameSet();
		return set == null ? null : set.getSuffix();
	}

	/**
	 * Return the inheritances of the permissible
	 * @return A collection of permissible
	 */
	public Collection<Permissible> getInheritances()
	{
		List<Permissible> inheritances = new ArrayList<>();

		for (String inheritance : this.inheritances)
		{
			Permissible permissible = PermissionsManager.getManager().getGroup(inheritance);

			if (permissible != null)
			{
				inheritances.add(permissible);
			}
		}

		return inheritances;
	}

	/**
	 * Check if the permissible has the permission <i>permission</io>
	 * @param permission The permission label
	 * @return Return: <b>true</b> if the permission has the permission, <b>false</b> otherwise
	 */
	public boolean hasPermission(String permission)
	{
		return testPermission(new Permission(permission)) == PermissionResult.YES;
	}

	/**
	 * Test the permission on the permissible.
	 * @param perm The permission
	 * @return Return {@link PermissionResult#NO} if the result is explicitly false. Return {@link PermissionResult#YES} if the result is explicitly true. Otherwise, return {@link PermissionResult#UNKNOW}
	 */
	public PermissionResult testPermission(Permission perm)
	{
		PermissionResult finalResult = PermissionResult.UNKNOWN, currentResult = null;

		for (PermissionSet set : permissions)
		{
			if (!set.isCompatible())
			{
				continue;
			}

			currentResult = set.hasPermission(perm);
			
			if (currentResult.equals(PermissionResult.NO))
			{
				return PermissionResult.NO;
			}
			if (currentResult.equals(PermissionResult.YES))
			{
				finalResult = PermissionResult.YES;
			}
		}

		if (finalResult.equals(PermissionResult.YES))
		{
			return finalResult;
		}

		for (Permissible permissible : getInheritances())
		{
			
			currentResult = permissible.testPermission(perm);

			if (currentResult == PermissionResult.NO)
			{
				return PermissionResult.NO;
			}
			if (currentResult == PermissionResult.YES)
			{
				finalResult = PermissionResult.YES;
			}
		}

		return finalResult;
	}

	/**
	 * Return the power for <i>label</i> for this permissible.
	 * @param label The power label
	 * @return Return an unsigned integer representing the power.
	 */
	public int getPower(String label)
	{
		PermissionSet set = getSetWithMaximalPower(label);
		return set == null ? 0 : set.getPower(label);
	}

	/**
	 * Return the permission set having the biggest power value for <i>label</i>
	 * @param label The power label
	 * @return Return the permission set. Can be null.
	 */
	public PermissionSet getSetWithMaximalPower(String label)
	{
		int max = -1, currentValue = 0;
		PermissionSet result = null, currentSet = null;

		for (PermissionSet set : permissions)
		{
			if (!set.isCompatible())
				continue;

			currentValue = set.getPower(label);

			if(currentValue > max)
			{
				max = currentValue;
				result = set;
			}
		}

		for (Permissible permissible : getInheritances())
		{
			currentSet = permissible.getSetWithMaximalPower(label);

			if (currentSet == null)
			{
				continue;
			}

			currentValue = currentSet.getPower(label);

			if (currentValue > max)
			{
				max = currentValue;
				result = currentSet;
			}
		}

		return result;
	}

	/**
	 * Return the value <i>valueLabel</i> of the permissible. The value is taken from the permission set having the biggest power value for <i>powerLabel</i>
	 * @param powerLabel The power label
	 * @param valueLabel The value label
	 * @return Return the label. Can be null.
	 */
	public JsonElement getSimpleValue(String powerLabel, String valueLabel)
	{
		PermissionSet set = getSetWithMaximalPower(powerLabel);
		return set == null ? null : set.getValue(valueLabel);
	}
}
