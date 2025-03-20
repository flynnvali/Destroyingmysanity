package com.petrolpark.destroy.chemistry.legacy.index.genericreaction;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.chemistry.legacy.*;
import com.petrolpark.destroy.chemistry.legacy.LegacyBond.BondType;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.DoubleGroupGenericReaction;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.GenericReactant;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyGroupTypes;
import com.petrolpark.destroy.chemistry.legacy.index.group.HalideGroup;
import com.petrolpark.destroy.chemistry.legacy.index.group.AcetylideGroup;

public class HalideAcetylideSubstitution extends DoubleGroupGenericReaction<HalideGroup, AcetylideGroup> {

    public HalideAcetylideSubstitution() {
        super(Destroy.asResource( "halide_acetylide_substitution"),
                DestroyGroupTypes.HALIDE, DestroyGroupTypes.ACETYLIDE);
    };

    @Override
    public boolean isPossibleIn(ReadOnlyMixture mixture) {
        return true;
    }


    @Override
    public LegacyReaction generateReaction(GenericReactant<HalideGroup> firstReactant, GenericReactant<AcetylideGroup> secondReactant) {
        LegacyMolecularStructure halideStructureCopy = firstReactant.getMolecule().shallowCopyStructure();
        HalideGroup halideGroup = firstReactant.getGroup();
        LegacyMolecularStructure acetylideStructureCopy = secondReactant.getMolecule().shallowCopyStructure();
        AcetylideGroup acetylideGroup = secondReactant.getGroup();

        acetylideStructureCopy.moveTo(acetylideGroup.carbonWithCharge);
        acetylideStructureCopy.moveTo(acetylideGroup.carbonWithCharge);

        halideStructureCopy.moveTo(HalideGroup.getHalogen);
        halideStructureCopy.remove(HalideGroup.getCarbon);

        LegacySpecies ccBond = moleculeBuilder().structure(LegacyMolecularStructure.joinFormulae(halideStructureCopy, acetylideStructureCopy, BondType.SINGLE)).build();



        return reactionBuilder()
                .addReactant(firstReactant.getMolecule())
                .addReactant(secondReactant.getMolecule())
                .addProduct(ccBond)
                .addProduct(getIon(halideGroup.halogen))
                //TODO kinetics
                .build();
    };

    public LegacySpecies getIon(LegacyAtom atom) {
        switch (atom.getElement()) {
            case FLUORINE:
                return DestroyMolecules.FLUORIDE;
            case CHLORINE:
                return DestroyMolecules.CHLORIDE;
            case IODINE:
                return DestroyMolecules.IODIDE;
            default:
                throw new GenericReactionGenerationException(atom.getElement().toString()+" is not a halogen.");
        }
    };



};
