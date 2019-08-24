package fr.badblock.api.permissions;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.badblock.api.permissions.Permission.PermissionResult;

/**
 * Permissible
 * @author LeLanN
 */
public class Permissible
{

    @SuppressWarnings("serial")
    Type inheritanceList = new TypeToken<List<String>>() {}.getType();
    @SuppressWarnings("serial")
    Type permissionList = new TypeToken<List<PermissionSet>>() {}.getType();

    public Type getInheritanceList() {
        return inheritanceList;
    }

    public Type getPermissionList() {
        return permissionList;
    }

    private String				name;
    private List<String>		inheritances;
    private List<PermissionSet>	permissions;

    public Permissible(String name, List<String> inheritances, List<PermissionSet> permissions)
    {
        this.name = name;
        this.inheritances = inheritances;
        this.permissions = permissions;
    }

    public Permissible()
    {
        this.name = "default";
        this.inheritances = new ArrayList<>();
        this.permissions = new ArrayList<>();
    }

    public Permissible(JsonObject jsonObject)
    {
        Gson gson = new Gson();

        name = jsonObject.get("name").getAsString();
        inheritances = gson.fromJson(jsonObject.get("inheritances"), inheritanceList);
        permissions = gson.fromJson(jsonObject.get("permissions"), permissionList);
    }

    public List<PermissionSet> getPermissions()
    {
        return this.permissions;
    }

    public String getName()
    {
        return name;
    }

    private PermissionSet getNameSet()
    {
        for (PermissionSet set : permissions)
        {
            if (set.isCompatibleExcludingAll())
                return set;
        }

        for (PermissionSet set : permissions)
        {
            if (set.isCompatible())
                return set;
        }

        return null;
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
     * @return Return {@link PermissionResult#NO} if the result is explicitly false. Return {@link PermissionResult#YES} if the result is explicitly true. Otherwise, return {@link PermissionResult}
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
}

