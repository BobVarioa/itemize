package com.bobvarioa.mobitems.entity.simulator;

import com.bobvarioa.mobitems.helpers.EntityUtils;
import com.bobvarioa.mobitems.helpers.ItemUtils;
import com.bobvarioa.mobitems.items.MobItem;
import com.bobvarioa.mobitems.items.components.MobEnchantmentList;
import com.bobvarioa.mobitems.items.components.SimulatedEffectMap;
import com.bobvarioa.mobitems.register.ModDataComponents;
import com.bobvarioa.mobitems.register.ModItems;
import com.bobvarioa.mobitems.register.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SimulatedMob {
	public boolean dirty = false;
	public CompoundTag tag;
	public RandomSource random;
	public ServerLevel level;
	public Vec3 position;
	
	public StatMap stats = new StatMap();
	
	private int stunTurns = 0;
	private List<SimulatedEffect> effects;
	private List<SimulatedBehavior> behaviors;
	private boolean discarded;

	public SimulatedMob(ServerLevel level, Vec3 position, ItemStack stack) {
		this.tag = MobItem.getEntityData(stack);
		this.random = level.getRandom();
		this.level = level;
		this.position = position;
		this.behaviors = stack.getOrDefault(ModDataComponents.MOB_ENCHANTMENTS, MobEnchantmentList.empty()).toList();
		this.effects = stack.getOrDefault(ModDataComponents.SIMULATED_EFFECTS, SimulatedEffectMap.empty()).toList();
		this.init();
	}
	

	public void tick() {
		giveSoul(getSoul());
		for (var e : effects) {
			if (e.duration > 0) {
				e.tick(this);
				e.duration--;
				this.markDirty();
			} else {
				effects.remove(e);
			}
		}
		for (var b : behaviors) {
			b.tick(this);
		}
	}


	public void attack(SimulatedMob other) {
		if (stunTurns > 0) {
			stunTurns--;
			return;
		}

		for (var b : behaviors) {
			if (b.beforeAttack(this, other)) {
				return;
			}
		}

		double damage = this.getDamage();
		if (stats.get(MobStat.ATTACK_SPEED) != 0.0f) {
			damage *= 4.0d / stats.get(MobStat.ATTACK_SPEED);
		}
		boolean doesCrit = random.nextIntBetweenInclusive(0, 10) <= stats.get(MobStat.CRIT_CHANCE);
		if (doesCrit) {
			damage *= stats.get(MobStat.CRIT_MULTIPLIER);
		}

		if (random.nextIntBetweenInclusive(0, 100) <= other.getHitChance(this)) {
			for (var b : behaviors) {
				damage = b.attack(this, other, damage);
			}
			other.hit(this, damage);
			if (stats.get(MobStat.KNOCKBACK) >= 0.0f) {
				other.doKnockback(stats.get(MobStat.KNOCKBACK));
			}
			for (var b : behaviors) {
				b.afterAttack(this, other);
			}
		}
	}

	public void hit(@Nullable SimulatedMob attacker, double damage) {
		for (var b : behaviors) {
			damage = b.beforeHit(this, attacker, damage);
		}
//		System.out.printf("damage %.2f%n", damage);
		setHealth(getHealth() - getEffectiveDamage(damage));
		for (var b : behaviors) {
			b.hit(this, attacker);
		}
	}

	public void doKnockback(double knockback) {
		double stun = knockback - (knockback * stats.get(MobStat.KNOCKBACK_RESISTANCE));

		if (random.nextBoolean()) {
			applyStun((int) Math.floor(stun));
		} else {
			applyStun((int) Math.ceil(stun));
		}
	}

	public boolean hasEffect(ResourceLocation id) {
		for (var e : effects) {
			if (e.id().equals(id)) {
				return true;
			}
		}
		return false;
	}
	
	public void applyEffect(SimulatedEffect effect) {
		this.markDirty();
		for (var e : effects) {
			if (e.id().equals(effect.id())) {
				e.duration = effect.duration;
				return;
			}
		}
		effects.add(effect);
	}

	public void applyStun(int stunTurns) {
		this.stunTurns += stunTurns;
	}

	public double getHealth() {
		return tag.getFloat("Health");
	}

	public double getRawMaxHealth() {
		return tag.getFloat("MaxHealth");
	}

	public double getMaxHealth() {
		return tag.getFloat("MaxHealth") + (float) Math.floor(getTotalSoul());
	}

	public void setHealth(double health) {
		tag.putFloat("Health", (float)(Math.clamp(health, 0, getMaxHealth())));
		markDirty();
	}

	public float getSoul() {
		return tag.getFloat(EntityUtils.SOUL_KEY);
	}

	public float getTotalSoul() {
		return getSoul() + tag.getFloat(EntityUtils.USED_SOUL_KEY);
	}

	public void setSoul(float soul) {
		tag.putFloat(EntityUtils.SOUL_KEY, soul);
		markDirty();
	}


	public float getUsedSoul() {
		return tag.getFloat(EntityUtils.USED_SOUL_KEY);
	}

	public void setUsedSoul(float soul) {
		tag.putFloat(EntityUtils.USED_SOUL_KEY, soul);
		markDirty();
	}

	public void giveSoul(float soul) {
		if (soul <= 0) return;
		setHealth(getHealth() + (float) Math.floor(soul));
		setUsedSoul(getUsedSoul() + soul);
		setSoul(0.0f);
	}

	
	private ItemStack parseStack(HolderLookup.Provider lookupProvider, CompoundTag tag) {
		if (!tag.isEmpty()) {
			return ItemStack.parseOptional(lookupProvider, tag);
		}
		return ItemStack.EMPTY;
	}

	private ItemAttributeModifiers itemsMods(ItemStack stack) {
		if (stack != null && !stack.isEmpty()) {
			if (stack.getItem() instanceof ArmorItem item) {
				return item.getDefaultAttributeModifiers();
			}
			return stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
		}
		return null;
	}
	
	private void addDefaultStat(AttributeSupplier supplier, Holder<Attribute> attr, MobStat stat, double defaultValue) {
		if (!supplier.hasAttribute(attr)) {
			this.stats.add(stat, defaultValue, Operation.ADD_BASE);
		} else {
			this.stats.add(stat, supplier.getBaseValue(attr), Operation.ADD_BASE);
		}
	}
	
	private void addDefaultAttrs(AttributeSupplier supplier) {
		this.stats.add(MobStat.CRIT_MULTIPLIER, 1.5, Operation.ADD_BASE);
		addDefaultStat(supplier, Attributes.MAX_HEALTH, MobStat.MAX_HEALTH, 0.0f);
		addDefaultStat(supplier, Attributes.ARMOR, MobStat.ARMOR, 0.0f);
		addDefaultStat(supplier, Attributes.ARMOR_TOUGHNESS, MobStat.ARMOR_TOUGHNESS, 0.0f);
		addDefaultStat(supplier, Attributes.ATTACK_DAMAGE, MobStat.DAMAGE, 1.0f);
		addDefaultStat(supplier, Attributes.ATTACK_KNOCKBACK, MobStat.KNOCKBACK, 0.0f);
		addDefaultStat(supplier, Attributes.KNOCKBACK_RESISTANCE, MobStat.KNOCKBACK_RESISTANCE, 0.0f);
		addDefaultStat(supplier, Attributes.ATTACK_SPEED, MobStat.ATTACK_SPEED, 4.0f);
		addDefaultStat(supplier, Attributes.LUCK, MobStat.CRIT_CHANCE, 2.0f);
		addDefaultStat(supplier, Attributes.MOVEMENT_SPEED, MobStat.MOVEMENT_SPEED, 0.25f);
	}

	private void addAttr(Holder<Attribute> attributeHolder, AttributeModifier modifier) {
		switch (modifier.operation()) {
            case ADD_VALUE -> this.stats.add(MobStat.getFor(attributeHolder), modifier.amount(), Operation.ADD_BASE);
            case ADD_MULTIPLIED_BASE -> this.stats.add(MobStat.getFor(attributeHolder), modifier.amount(), Operation.ADD_MULTIPLIER);
            case ADD_MULTIPLIED_TOTAL -> this.stats.add(MobStat.getFor(attributeHolder), modifier.amount(), Operation.TOTAL_MULTIPLIER);
        }
	}


	private void addAttrsItem(EquipmentSlot slot, ItemAttributeModifiers modifiers) {
		if (modifiers != null && !modifiers.modifiers().isEmpty()) {
			modifiers.forEach(slot, this::addAttr);
		}
	}
	
	public void init() {
		EntityType<? extends LivingEntity> type = getEntityType();
		addDefaultAttrs(DefaultAttributes.getSupplier(type));

		RegistryAccess lookupProvider = level.registryAccess();
		if (tag.contains("HandItems", CompoundTag.TAG_LIST)) {
			var handItems = tag.getList("HandItems", CompoundTag.TAG_COMPOUND);
			if (handItems.size() == 2) {
				// todo: bow? / other ranged
				addEquippable(lookupProvider, EquipmentSlot.MAINHAND, handItems);
				addEquippable(lookupProvider, EquipmentSlot.OFFHAND, handItems);
			}
		}
		if (tag.contains("ArmorItems", CompoundTag.TAG_LIST)) {
			var armorItems = tag.getList("ArmorItems", CompoundTag.TAG_COMPOUND);
			if (armorItems.size() == 4) {
				addEquippable(lookupProvider, EquipmentSlot.HEAD, armorItems);
				addEquippable(lookupProvider, EquipmentSlot.CHEST, armorItems);
				addEquippable(lookupProvider, EquipmentSlot.LEGS, armorItems);
				addEquippable(lookupProvider, EquipmentSlot.FEET, armorItems);
			}
		}

		if (ModRegistries.ENTITY_BEHAVIORS.containsKey(type)) {
			var behavior = ModRegistries.ENTITY_BEHAVIORS.get(type);
			this.behaviors.add(behavior.get());
		}

		// enchantments
		for (var e : behaviors) {
			e.init(this);
		}
		
		stats.freeze();
	}
	
	private void addItemBehavior(EquipmentSlot slot, ItemStack stack) {
		Item item = stack.getItem();
		if (ModRegistries.ITEM_BEHAVIORS.containsKey(item)) {
			SimulatedBehavior behavior = ModRegistries.ITEM_BEHAVIORS.get(item).get();
			if (behavior instanceof SimulatedItemBehavior itemBehavior) {
				itemBehavior.slot = slot;
			}
			this.behaviors.add(behavior);
		}
	}
	
	private void addEquippable(HolderLookup.Provider lookupProvider, EquipmentSlot slot, ListTag items) {
		ItemStack stack = parseStack(lookupProvider, items.getCompound(slot.getIndex()));
		addAttrsItem(slot, itemsMods(stack));
		addItemBehavior(slot, stack);
	}

	@NotNull
	private EntityType<? extends LivingEntity> getEntityType() {
		return (EntityType<? extends LivingEntity>) EntityType.byString(tag.getString("id")).orElse(EntityType.PIG);
	}

	public double getDamage() {
		return stats.get(MobStat.DAMAGE) + Math.floor(getTotalSoul() / 25);
	}

	public double getEffectiveDamage(double damage) {
		final float MAX_ARMOR_PERCENTAGE = 23; // poorly named but in vanilla this value is 20, basically the max is 20/25 vs 23/25
		double v1 = (4 * damage) / (Math.min(stats.get(MobStat.ARMOR_TOUGHNESS), 20) + 8);
		double armor = stats.get(MobStat.ARMOR);
		double v2 = Math.max(armor / 5, armor - v1);
		double v3 = 1 - (Math.min(MAX_ARMOR_PERCENTAGE, v2) / 25);
		return damage * v3;
	}

	public double getHitChance(SimulatedMob other) {
		double x = stats.get(MobStat.MOVEMENT_SPEED) - other.stats.get(MobStat.MOVEMENT_SPEED);
		double exp = (float) ((90 - 1) * (1 - Math.exp(-(1.1) * x)));
		return 100 - exp;
	}

	public void markDirty() {
		dirty = true;
	}

	public ItemStack getStack() {
		ItemStack stack = new ItemStack(ModItems.MOB_ITEM.get());
		for (var e : effects) {
			e.saveIntoEntity(tag);
		}
		stack.set(DataComponents.ENTITY_DATA, CustomData.of(tag));
		stack.set(ModDataComponents.MOB_ENCHANTMENTS, MobEnchantmentList.from(behaviors));
		stack.set(ModDataComponents.SIMULATED_EFFECTS, SimulatedEffectMap.from(effects));
		return stack;
	}
	
	private void dropItems (List<ItemStack> stacks, @Nullable SimulatedMob other) {
		ResourceKey<LootTable> resourcekey = getEntityType().getDefaultLootTable();
		LootTable loottable = level.getServer().reloadableRegistries().getLootTable(resourcekey);
		LootParams.Builder lootparams$builder = (new LootParams.Builder(level))
			.withParameter(LootContextParams.ORIGIN, this.position)
			.withParameter(LootContextParams.DAMAGE_SOURCE, other == null ? level.damageSources().generic() : level.damageSources().playerAttack(null))
			.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, null)
			.withLuck((float)stats.get(MobStat.CRIT_CHANCE)); // todo: looting

		LootParams lootparams = lootparams$builder.create(LootContextParamSets.EMPTY);
		loottable.getRandomItems(lootparams, 0L, stacks::add);
		
		int chances = (int)Math.floor(getTotalSoul() / getRawMaxHealth());
		while (chances >= 1) {
			if (random.nextIntBetweenInclusive(1, 10) <= 6) {
				loottable.getRandomItems(lootparams, 0L, stacks::add);
			}
			chances--;
		}
	}
	
	public enum DeathResult {
		/**
		 * This mob neither lives nor dies, another method can determine its fate
		 */
		PASS,
		/**
		 * This mob is not removed
		 */
		LIVE,
		/**
		 * This mob is removed, drops and/or soul are given
		 */
		DIE,
		/**
		 * This mob is removed, no drops or soul are given
		 */
		DISCARD
	}

	public DeathResult shouldDie() {
		if (discarded) return DeathResult.DISCARD;
		for (var e : behaviors) {
			DeathResult result = e.shouldDie(this);
			if (result != DeathResult.PASS) {
				return result;
			}
		}
		
		if (getHealth() <= 0) {
			return DeathResult.DIE;
		}
		return DeathResult.LIVE;
	}
	
	public void discard() {
		this.discarded = true;
	}
	

	public List<ItemStack> die(SimulatedMob other) {
		// other gains a random amount of soul if this had a higher or equal max hp (basically if the other was "weaker" than this)
		if (getMaxHealth() >= other.getMaxHealth()) {
			other.giveSoul(random.nextIntBetweenInclusive(0, (int) Math.floor(other.getRawMaxHealth() / 2)));
		}
		// plus any unused soul it might've had
		other.giveSoul(getSoul());
		
		for (var b : other.behaviors) {
			b.kill(other, this);
		}
		
		List<ItemStack> loot = new ArrayList<>();
		dropItems(loot, other);

		var lookupProvider = level.registryAccess();
		
		// this is basically the same as vanilla, but it doesn't randomly damage the items
		if (tag.contains("HandItems", CompoundTag.TAG_LIST)) {
			var handItems = tag.getList("HandItems", CompoundTag.TAG_COMPOUND);
			var handDropChances = tag.getList("HandDropChances", CompoundTag.TAG_FLOAT);
			if (handItems.size() == 2) {
				float mainChance = handDropChances.getFloat(0);
				if (mainChance > 1.0f || this.random.nextFloat() < mainChance) {
					loot.add(ItemUtils.parseItemStack(lookupProvider, handItems.getCompound(0)));
				}

				float offChance = handDropChances.getFloat(1);
				if (offChance > 1.0f || this.random.nextFloat() < offChance) {
					loot.add(ItemUtils.parseItemStack(lookupProvider, handItems.getCompound(1)));
				}
			}
		}
		if (tag.contains("ArmorItems", CompoundTag.TAG_LIST)) {
			var armorItems = tag.getList("ArmorItems", CompoundTag.TAG_COMPOUND);
			var handDropChances = tag.getList("ArmorDropChances", CompoundTag.TAG_FLOAT);
			if (armorItems.size() == 4) {
				for (int i = 0; i < 4; i++) {
					float chance = handDropChances.getFloat(i);
					if (chance > 1.0f || this.random.nextFloat() < chance) {
						loot.add(ItemUtils.parseItemStack(lookupProvider, armorItems.getCompound(i)));
					}
				}
			}
		}
		
		return loot;
	}

}
