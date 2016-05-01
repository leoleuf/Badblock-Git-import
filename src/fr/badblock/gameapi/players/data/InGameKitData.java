package fr.badblock.gameapi.players.data;

import fr.badblock.gameapi.players.kits.PlayerKit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * InGameData pour g�rer le kit choisit par le joueur
 * @author LeLanN
 */
@NoArgsConstructor
public class InGameKitData implements InGameData {
	@Getter@Setter private PlayerKit choosedKit;
}
