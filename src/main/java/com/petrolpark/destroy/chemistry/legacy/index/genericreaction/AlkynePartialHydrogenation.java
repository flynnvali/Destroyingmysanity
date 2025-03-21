package com.petrolpark.destroy.chemistry.legacy.index.genericreaction;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.chemistry.legacy.LegacyBond.BondType;
import com.petrolpark.destroy.chemistry.legacy.*;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.GenericReactant;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.SingleGroupGenericReaction;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyGroupTypes;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.legacy.index.group.SaturatedCarbonGroup;

public class AlkynePartialHydrogenation extends SingleGroupGenericReaction<SaturatedCarbonGroup> {

    public AlkynePartialHydrogenation() {
        super(Destroy.asResource( "alkyne_dissolving_metal_reduction"),
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
                .addReactant(DestroyMolecules.SODIUM_METAL, 2)
                .addReactant(DestroyMolecules.AMMONIA, 2) // irl this uses liquid ammonia, couldn't figure out how to implement that
                .addProduct(product, 1)
                .addProduct(DestroyMolecules.AZANIDE, 2)
                .addProduct(DestroyMolecules.SODIUM_ION, 2)
                .build();
    };

};
