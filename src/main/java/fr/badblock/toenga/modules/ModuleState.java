package fr.badblock.toenga.modules;

public enum ModuleState
{
	/**
	 * Called when an extinguished instance is recovered
	 */
	RECOVER,
	BEFORE_COPY,
	BEFORE_START,
	AFTER_START,
	BEFORE_STOP,
	AFTER_STOP,
	AFTER_DELETE;
}
