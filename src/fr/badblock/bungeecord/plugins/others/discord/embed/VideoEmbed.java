package fr.badblock.bungeecord.plugins.others.discord.embed;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoEmbed {
	String url;
	int height;
	int width;
}
