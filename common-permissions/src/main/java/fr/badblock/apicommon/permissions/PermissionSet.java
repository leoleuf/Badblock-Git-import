package fr.badblock.apicommon.permissions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.badblock.apicommon.permissions.Permission.PermissionResult;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represent a set of permission, power and value. A set is linked to a place list. Places can be minigames, faction, ...
 * @author LeLanN
 */
@AllArgsConstructor
@Data
public class PermissionSet
{
	
	private List<String> places;
	private List<Permission> permissions;
	private Map<String, Integer> powers;
	private Map<String, JsonElement> values;

	private boolean displayable;
	
	private String prefix;
	private String suffix;

	public PermissionSet(List<String> places, List<Permission> permissions, Map<String, Integer> powers, Map<String, JsonElement> values)
	{
		this.places = places;
		this.permissions = permissions;
		this.powers = powers;
		this.values = values;
		this.displayable = false;
		this.prefix = "";
		this.suffix = "";
	}
	
	public int getPower(String label)
	{
		if (powers == null)
		{
			powers = new HashMap<>();
		}

		return powers.containsKey(label) ? (int) powers.get(label) : 0;
	}

	public JsonElement getValue(String label)
	{
		if (values == null)
		{
			values = new HashMap<>();
		}

		if (!values.containsKey(label))
		{
			return new JsonObject();
		}

		return values.get(label);
	}

	public PermissionResult hasPermission(Permission permission)
	{
		PermissionResult result = null;

		if (permission == null)
		{
			return PermissionResult.UNKNOWN;
		}

		if (permissions == null)
		{
			return PermissionResult.UNKNOWN;
		}

		for (Permission perm : this.permissions)
		{
			result = perm.compare(permission);

			if (result != PermissionResult.UNKNOWN)
			{
				return result;
			}
		}

		return PermissionResult.UNKNOWN;
	}

	public boolean isCompatible(String place)
	{
		return places.contains(place) || places.contains("*");
	}

	public boolean isCompatible()
	{
		return isCompatible( PermissionsManager.getManager().getCurrentPlace() );
	}
	
	public boolean isCompatibleExcludingAll(String place)
	{
		return places.contains(place);
	}

	public boolean isCompatibleExcludingAll()
	{
		return isCompatibleExcludingAll(PermissionsManager.getManager().getCurrentPlace());
	}
}
