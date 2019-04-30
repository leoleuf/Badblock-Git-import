package fr.badblock.gameapi.commands;

@FunctionalInterface
public interface ISourceCastFunction<T>
{
	public T cast(Object o);
}
