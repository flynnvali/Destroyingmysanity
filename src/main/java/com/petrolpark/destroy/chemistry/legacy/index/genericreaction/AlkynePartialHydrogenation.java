package com.petrolpark.destroy.chemistry.legacy.index.genericreaction;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.chemistry.legacy.LegacyBond.BondType;
import com.petrolpark.destroy.chemistry.legacy.*;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.GenericReactant;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.SingleGroupGenericReaction;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyGroupTypes;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.legacy.index.group.SaturatedCarbonGroup;

public class AlkynePartialHydrogenation extends SingleGroupGenericReaction<SaturatedCarbonGroup> {

    public AlkynePartialHydrogenation() {
        super(Destroy.asResource( "alkyne_poisoned_hydrogenation"),
                DestroyGroupTypes.ALKYNE);
    };

    @Override
    public boolean isPossibleIn(ReadOnlyMixture mixture) {
        return mixture.getConcentrationOf(DestroyMolecules.SODIUM_METAL) > 0f && mixture.getConcentrationOf(DestroyMolecules.AMMONIA) > 0f;
    };

    @Override
    public LegacyReaction generateReaction(GenericReactant<SaturatedCarbonGroup> reactant) {
        SaturatedCarbonGroup group = reactant.getGroup();
        LegacySpecies substrate = reactant.getMolecule();
        LegacySpecies product = moleculeBuilder().structure(substrate
                .shallowCopyStructure()
                .moveTo(group.highDegreeCarbon)
                .replaceBondTo(group.lowDegreeCarbon, BondType.DOUBLE)
                .addGroup(LegacyMolecularStructure.atom(LegacyElement.HYDROGEN))
                .moveTo(group.lowDegreeCarbon)
                .addGroup(LegacyMolecularStructure.atom(LegacyElement.HYDROGEN))

        ).build();

        return reactionBuilder()
                .addReactant(reactant.getMolecule(), 1)
                .addReactant(DestroyMolecules.HYDROGEN, 1)
                .addSimpleItemCatalyst(DestroyItems.NICKEL_BORIDE::get, 1f)
                .addProduct(product, 1)
                .build();
    };

};
