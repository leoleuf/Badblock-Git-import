package fr.badblock.bungeecord.plugins.others.utils;

import lombok.Data;

@Data
public class IPHubObject
{

	private String	ip;
	private String	countryCode;
	private String	countryName;
	private int		asn;
	private String	isp;
	private int		block;
	
}
