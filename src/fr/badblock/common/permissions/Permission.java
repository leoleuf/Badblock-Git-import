package fr.badblock.common.permissions;

import lombok.Getter;

/**
 * Repr�sente une permission
 * Classe compatible Spigot/BungeeCord/Ladder
 * @author LeLanN
 */
@Getter public class Permission {
	private boolean antiPermission;
	private String permission;
	private boolean all;
	
	public Permission(String permission){
 		if(permission == null){
 			antiPermission = false;
 			all			   = false;
 			permission	   = "";
 		}
		if(permission.startsWith("-")){
			antiPermission = true;
			permission = permission.substring(1);
		} else antiPermission = false;
		
		if(permission.endsWith("*")){
			all = true;
			permission = permission.substring(0, permission.length() - 1);
		} else all = false;
		
		if(permission.endsWith("."))
			permission = permission.substring(0, permission.length() - 1);

		this.permission = permission.toLowerCase();
	}
	
	/**
	 * V�rifie si la permission correspond, et, dans ce cas, si  le joueur � la permission :
	 * Si la permission est sous la forme -permission, la r�ponse sera 'NO'
	 * Si la permission est introuvable, la r�ponse sera 'UNKNOW'
	 * Sinon, la r�ponse sera 'YES'
	 * 
	 * La permission correspond, si la permission est la m�me, ou si la permission donn�e
	 * en param�tre est une sous-partie de la permission donn�e, et qu'elle est sous la forme
	 * permission.*
	 * 
	 * Par exemple, sera reconnu comme m�me permission :
	 * - example.* (Permission) et example.command (param�tre)
	 * - * (Permission) et n'importe quelle permission
	 * 
	 * Ne sera pas reconnu comme m�me permission :
	 * - example2.* (Permission) et example.command (param�tre)
	 * - example.test (Permission) et example.command (param�tre)
	 * 
	 * @param perm La permission a tester
	 * @return La r�ponse
	 */
	public Reponse has(Permission perm){
		if(perm.permission.equalsIgnoreCase(permission)
			|| (perm.permission.startsWith(permission) && all)){
			return antiPermission == perm.antiPermission ? Reponse.YES : Reponse.NO;
		}
		
		return Reponse.UNKNOW;
	}
	
	@Override
	public String toString(){
		return (antiPermission ? "-" : "") + permission + (all ? (permission.isEmpty() ? "*" : ".*") : "");
	}
	
	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Permission)) return false;
	    Permission permission = (Permission) other;
	    if (!permission.getPermission().equalsIgnoreCase(this.getPermission())) return false;
	    if (permission.isAll() != this.isAll()) return false;
	    if (permission.isAntiPermission() != this.isAntiPermission()) return false;
	    return true;
	}
	
	public String getPermission() {
		return this.permission;
	}
	
	/**
	 * Repr�sente la r�ponse de Permission lorsque l'on check si une autre permission correspond
	 * Classe compatible Spigot/BungeeCord/Ladder
	 * @author LeLanN
	 */
	public enum Reponse {
		/**
		 * Les deux permissions sont correspondent [voir Permission.has()] et le joueur a bien la permission
		 */
		YES,
		/**
		 * Les deux permissions ne correspondent pas [voir Permission.has()], mais une anti-permission [voir Permission.has()] fait que le joueur n'a pas la permission
		 */
		NO,
		/**
		 * Les deux permissions ne correspondent pas [voir Permission.has()]
		 */
		UNKNOW;
	}
}
