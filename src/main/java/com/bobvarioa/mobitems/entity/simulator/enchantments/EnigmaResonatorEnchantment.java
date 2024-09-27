package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.MobStat;
import com.bobvarioa.mobitems.entity.simulator.Operation;
import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class EnigmaResonatorEnchantment extends SimulatedBehavior {
	public EnigmaResonatorEnchantment() {
		super(ModBehaviors.ENIGMA_RESONATOR.getId());
	}

	@Override
	public void init(SimulatedMob self) {
		double soul = self.getTotalSoul();
		double critChance = (10 - 1) * (1 - Math.exp(-0.01 * (soul)));
		
		self.stats.add(MobStat.CRIT_CHANCE, critChance, Operation.ADD_BASE);
	}
}
