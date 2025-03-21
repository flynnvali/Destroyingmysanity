package com.petrolpark.destroy.chemistry.legacy.index.genericreaction;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.chemistry.legacy.*;
import com.petrolpark.destroy.chemistry.legacy.LegacyBond.BondType;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.GenericReactant;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.SingleGroupGenericReaction;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyGroupTypes;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.legacy.index.group.AcetylideGroup;
import com.petrolpark.destroy.chemistry.legacy.index.group.SaturatedCarbonGroup;

import java.util.List;

public class AlkyneDeprotonation extends SingleGroupGenericReaction<SaturatedCarbonGroup> {

    public AlkyneDeprotonation() {
        super(Destroy.asResource("alkyne_deprotonation"), DestroyGroupTypes.ALKYNE);
    };

    @Override
    public boolean isPossibleIn(ReadOnlyMixture mixture) {
        return mixture.getConcentrationOf(DestroyMolecules.AZANIDE) > 0f;
    };

    @Override
    public LegacyReaction generateReaction(GenericReactant<SaturatedCarbonGroup> reactant) {
        SaturatedCarbonGroup alkyne = reactant.getGroup();
        LegacyMolecularStructure structure = reactant.getMolecule().shallowCopyStructure();
        List<LegacyAtom> hydrogens = structure.moveTo(alkyne.lowDegreeCarbon).getBondedAtomsOfElement(LegacyElement.HYDROGEN);
       // if (hydrogens.size() == 0 ) return  null;
        if (hydrogens.isEmpty()) return null; // This should never be the case
        structure.remove(hydrogens.get(0))
        .moveTo(alkyne.highDegreeCarbon)
        .remove(alkyne.lowDegreeCarbon)
        .addAtom(new LegacyAtom(LegacyElement.CARBON, -1d))
        .replaceBondTo(new LegacyAtom(LegacyElement.CARBON, -1d), BondType.TRIPLE);

        return reactionBuilder()
            .addReactant(reactant.getMolecule(), 1)
            .addReactant(DestroyMolecules.AZANIDE, 1)
            .addProduct(moleculeBuilder().structure(structure).build(), 1)
            .addProduct(DestroyMolecules.AMMONIA,1)
            .build();
    };

    
};
