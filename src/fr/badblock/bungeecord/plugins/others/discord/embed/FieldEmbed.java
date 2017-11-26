package fr.badblock.bungeecord.plugins.others.discord.embed;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FieldEmbed {
	String name;
	String value;
	boolean inline;
}
