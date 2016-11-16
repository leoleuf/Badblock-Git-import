package net.md_5.bungee.util;

import gnu.trove.set.hash.TCustomHashSet;
import java.util.Collection;

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
