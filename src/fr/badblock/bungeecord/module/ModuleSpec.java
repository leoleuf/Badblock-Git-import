package fr.badblock.bungeecord.module;

import java.io.File;
import lombok.Data;

@Data
public class ModuleSpec
{

    private final String name;
    private final File file;
    private final ModuleSource provider;
}
