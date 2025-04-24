package com.petrolpark.destroy.chemistry.legacy.index.genericreaction;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.chemistry.legacy.LegacyElement;
import com.petrolpark.destroy.chemistry.legacy.LegacyMolecularStructure;
import com.petrolpark.destroy.chemistry.legacy.LegacyReaction;
import com.petrolpark.destroy.chemistry.legacy.ReadOnlyMixture;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.GenericReactant;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.SingleGroupGenericReaction;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyGroupTypes;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.legacy.index.group.CarbonylGroup;
import com.petrolpark.destroy.chemistry.legacy.index.group.NitroGroup;
import com.simibubi.create.AllTags;

public class LeukartReaction extends SingleGroupGenericReaction<CarbonylGroup> {

    public LeukartReaction() {
        super(Destroy.asResource("leukart_reaction"), DestroyGroupTypes.CARBONYL);
    };

    @Override
    public boolean isPossibleIn(ReadOnlyMixture mixture) {
        return mixture.getConcentrationOf(DestroyMolecules.FORMIC_ACID) > 0f;
    };

    @Override
    public LegacyReaction generateReaction(GenericReactant<CarbonylGroup> reactant) {
        CarbonylGroup group = reactant.getGroup();
        LegacyMolecularStructure structure = reactant.molecule.shallowCopyStructure();
        structure.moveTo(group.carbon)
            .remove(group.oxygen)
            .addAtom(LegacyElement.HYDROGEN)
            .addGroup(
                    LegacyMolecularStructure.atom(LegacyElement.NITROGEN)
                            .addAtom(LegacyElement.HYDROGEN)
                            .addAtom(LegacyElement.HYDROGEN)
            );
        return reactionBuilder()
            .addReactant(reactant.getMolecule())
            .addReactant(DestroyMolecules.FORMIC_ACID)
            .addReactant(DestroyMolecules.AMMONIA)
            .addProduct(DestroyMolecules.WATER)
            .addProduct(DestroyMolecules.CARBON_DIOXIDE)
            .addProduct(moleculeBuilder().structure(structure).build())
            .build();
    };
    
};
