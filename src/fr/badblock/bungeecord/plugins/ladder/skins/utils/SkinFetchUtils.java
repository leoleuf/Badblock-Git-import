/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 */

package fr.badblock.bungeecord.plugins.ladder.skins.utils;

import java.util.UUID;

import fr.badblock.bungeecord.api.ChatColor;
import fr.badblock.bungeecord.plugins.ladder.json.parser.ParseException;
import fr.badblock.bungeecord.plugins.ladder.skins.format.Profile;
import fr.badblock.bungeecord.plugins.ladder.skins.format.SkinProfile;
import fr.badblock.bungeecord.plugins.ladder.skins.storage.LocaleStorage;

public class SkinFetchUtils {

	public static SkinProfile fetchSkinsProfile(String name, UUID uuid) throws SkinFetchFailedException {
		
		try {
			if (uuid != null) {
				try {
					SkinProfile skinprofile = MojangAPI.getSkinProfile(uuid.toString());
					if (skinprofile.getName().equalsIgnoreCase(name)) {
						return skinprofile;
					}
				} catch (SkinFetchFailedException ex) {
				}
			}
			Profile profile = MojangAPI.getProfile(name);
			return MojangAPI.getSkinProfile(profile.getId());
		} catch (ParseException e) {
			throw new SkinFetchFailedException(SkinFetchFailedException.Reason.SKIN_RECODE_FAILED);
		} catch (SkinFetchFailedException sffe) {
			throw sffe;
		} catch (Throwable t) {
			throw new SkinFetchFailedException(t);
		}
	}

	public static class SkinFetchFailedException extends Exception {

		private static final long serialVersionUID = -7597517818949217019L;

		private Reason reason;

		public SkinFetchFailedException(Reason reason) {
			super(reason.getExceptionCause()); 
			this.reason = reason;
		}

		public SkinFetchFailedException(Throwable exception) {
			super(Reason.GENERIC_ERROR.getExceptionCause()+": "+exception.getClass().getName()+": "+exception.getMessage(), exception); 
			this.reason = Reason.GENERIC_ERROR;
		}

		public Reason getReason() {
			return reason;
		}

		public static enum Reason {
			NO_PREMIUM_PLAYER(c(LocaleStorage.getInstance().SKIN_FETCH_FAILED_NO_PREMIUM_PLAYER)),
			NO_SKIN_DATA(c(LocaleStorage.getInstance().SKIN_FETCH_FAILED_NO_SKIN_DATA)),
			SKIN_RECODE_FAILED(c(LocaleStorage.getInstance().SKIN_FETCH_FAILED_PARSE_FAILED)),
			RATE_LIMITED(c(LocaleStorage.getInstance().SKIN_FETCH_FAILED_RATE_LIMITED)),
			GENERIC_ERROR(c(LocaleStorage.getInstance().SKIN_FETCH_FAILED_ERROR)),
			MCAPI_FAILED(c(LocaleStorage.getInstance().SKIN_FETCH_FAILED_ERROR)),
			MCAPI_OVERLOAD(c(LocaleStorage.getInstance().SKIN_FETCH_FAILED_MCAPI_OVERLOAD));

			private String exceptionCause;

			private Reason(String exceptionCause) {
				this.exceptionCause = exceptionCause;
			}

			public String getExceptionCause() {
				return exceptionCause;
			}
		}
		
	}

	   public static String c(String msg){
		   return ChatColor.translateAlternateColorCodes('&', msg);
	   }
}
