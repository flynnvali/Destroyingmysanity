package com.petrolpark.destroy.chemistry.legacy.index.genericreaction;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.chemistry.legacy.*;
import com.petrolpark.destroy.chemistry.legacy.LegacyBond.BondType;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.GenericReactant;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.SingleGroupGenericReaction;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyGroupTypes;
import com.petrolpark.destroy.chemistry.legacy.index.group.AlkyneGroup;
import com.petrolpark.destroy.chemistry.legacy.index.group.SaturatedCarbonGroup;

public class AlkyneHydrolysis extends SingleGroupGenericReaction<SaturatedCarbonGroup> {

    public AlkyneHydrolysis() {
        super(Destroy.asResource( "alkyne_hydrolysis"),
                DestroyGroupTypes.ALKYNE);
    };

    @Override
    public boolean isPossibleIn(ReadOnlyMixture mixture) {
        return mixture.getConcentrationOf(DestroyMolecules.WATER) > 0f;
    };

    @Override
    public LegacyReaction generateReaction(GenericReactant<SaturatedCarbonGroup> reactant) {
        SaturatedCarbonGroup group = reactant.getGroup();
        LegacySpecies substrate = reactant.getMolecule();
        LegacySpecies product = moleculeBuilder().structure(substrate
                .shallowCopyStructure()
                .moveTo(group.highDegreeCarbon)
                .replaceBondTo(group.lowDegreeCarbon, BondType.SINGLE)
                .addCarbonyl()
                .moveTo(group.lowDegreeCarbon)
                .addGroup(LegacyMolecularStructure.atom(LegacyElement.HYDROGEN))
                .addGroup(LegacyMolecularStructure.atom(LegacyElement.HYDROGEN))
        ).build();

        return reactionBuilder()
                .addReactant(reactant.getMolecule(), 1, 1)
                .addReactant(DestroyMolecules.WATER)
                .addCatalyst(DestroyMolecules.PROTON, 2)
                .addProduct(product, 1)
                .activationEnergy(10f)
                .displayAsReversible()
                .build();
    };

};
