package ru.ulpfr.pension_brms.listeners;

import ru.ulpfr.pension_brms.managers.DroolsManager.RULES_TYPES;

public interface GlobalListener {
	void windowReady();
	void windowNotReady();
	void allowDroolsInit(RULES_TYPES mode);
}
