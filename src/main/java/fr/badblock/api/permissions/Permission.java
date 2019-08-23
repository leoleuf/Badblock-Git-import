package fr.badblock.api.permissions;

public class Permission
{
    private transient PermissionResult result;
    private transient boolean isAll = false;

    public PermissionResult getResult() {
        return result;
    }

    public boolean isAll() {
        return isAll;
    }

    public String getPermission() {
        return permission;
    }

    private String permission;

    public Permission(String permission)
    {
        load(permission);
    }

    private void load(String permission)
    {
        result = PermissionResult.YES;

        if(permission == null)
            permission = "";

        char first = permission.charAt( 0 );
        char last  = permission.charAt( permission.length() - 1 );

        if(first == '-')
        {
            result = PermissionResult.NO;
            permission = permission.substring(1);
        }

        if(last == '*')
        {
            isAll = true;
            permission = permission.substring(0, permission.length() - 1);
        }

        this.permission = permission.toLowerCase();
    }

    public PermissionResult compare(Permission permission)
    {
        String perm1 = getPermission(), perm2 = permission.getPermission();

        if (getResult() == null && getPermission() != null)
        {
            load(getPermission());
            perm1 = getPermission();
        }

        if (perm1 == null || permission == null || perm2 == null)
        {
            return PermissionResult.UNKNOWN;
        }

        if (perm1.equals(perm2) || permission.getPermission().equals("*") || ( isAll && perm2.startsWith(perm1) ))
        {
            return getResult() == permission.getResult() ? PermissionResult.YES : PermissionResult.NO;
        }

        return PermissionResult.UNKNOWN;
    }

    @Override
    public String toString()
    {
        return (result == PermissionResult.NO ? "-" : "")					// -
                + permission												// permissioon
                + (isAll ? (permission.isEmpty() ? "*" : ".*") : "");		// *
    }

    public enum PermissionResult
    {
        YES,
        NO,
        UNKNOWN;
    }
}

