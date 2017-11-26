package fr.badblock.bungeecord.plugins.others.discord.embed;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FooterEmbed {
	String text;
	String icon_url;
	String proxy_icon_url;
}
