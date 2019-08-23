package fr.badblock.api.permissions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.badblock.api.permissions.Permission.PermissionResult;

/**
 * Represent a set of permission, power and value. A set is linked to a place list. Places can be minigames, faction, ...
 * @author LeLanN
 */
public class PermissionSet
{

    private List<String> places;
    private List<Permission> permissions;
    private Map<String, Integer> powers;
    private Map<String, JsonElement> values;

    public List<String> getPlaces() {
        return places;
    }

    public void setPlaces(List<String> places) {
        this.places = places;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public Map<String, Integer> getPowers() {
        return powers;
    }

    public void setPowers(Map<String, Integer> powers) {
        this.powers = powers;
    }

    public Map<String, JsonElement> getValues() {
        return values;
    }

    public void setValues(Map<String, JsonElement> values) {
        this.values = values;
    }

    public boolean isDisplayable() {
        return displayable;
    }

    public void setDisplayable(boolean displayable) {
        this.displayable = displayable;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

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

            if (result != Permission.PermissionResult.UNKNOWN)
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

