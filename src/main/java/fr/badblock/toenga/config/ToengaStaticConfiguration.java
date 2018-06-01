package fr.badblock.toenga.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class ToengaStaticConfiguration
{
	private Set<String> types = new HashSet<>(Arrays.asList("production"));
	private String hostnameOverride;
	private String java;
}
