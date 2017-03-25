package fr.badblock.bungeecord.util;

import java.util.Collection;

import gnu.trove.set.hash.TCustomHashSet;

public class CaseInsensitiveSet extends TCustomHashSet<String>
{

    @SuppressWarnings("unchecked")
	public CaseInsensitiveSet()
    {
        super( CaseInsensitiveHashingStrategy.INSTANCE );
    }

    @SuppressWarnings("unchecked")
	public CaseInsensitiveSet(Collection<? extends String> collection)
    {
        super( CaseInsensitiveHashingStrategy.INSTANCE, collection );
    }
}
