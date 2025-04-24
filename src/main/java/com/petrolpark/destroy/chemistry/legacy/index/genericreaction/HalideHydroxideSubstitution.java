package com.petrolpark.destroy.chemistry.legacy.index.genericreaction;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.chemistry.legacy.*;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.GenericReactant;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.SingleGroupGenericReaction;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyGroupTypes;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.legacy.index.group.AlcoholGroup;
import com.petrolpark.destroy.chemistry.legacy.index.group.HalideGroup;
import com.petrolpark.destroy.chemistry.legacy.index.group.NitroGroup;
import com.simibubi.create.AllTags;

import java.util.List;

public class HalideHydroxideSubstitution extends SingleGroupGenericReaction<HalideGroup> {

    public HalideHydroxideSubstitution() {
        super(Destroy.asResource("halide_hydroxide_substitution"), DestroyGroupTypes.HALIDE);
    };

    @Override
    public boolean isPossibleIn(ReadOnlyMixture mixture) {
        return mixture.getConcentrationOf(DestroyMolecules.HYDROXIDE) > 0f;
    };

    @Override
    public LegacyReaction generateReaction(GenericReactant<HalideGroup> reactant) {
        HalideGroup group = reactant.getGroup();
        LegacyMolecularStructure structure = reactant.molecule.shallowCopyStructure();
        List<LegacyAtom> chlorines = structure.moveTo(group.carbon).getBondedAtomsOfElement(LegacyElement.CHLORINE);
        List<LegacyAtom> iodines = structure.moveTo(group.carbon).getBondedAtomsOfElement(LegacyElement.IODINE);
        if (chlorines.size() + iodines.size() > 1) return null; // making sure this reaction and the geminal dihalide hydroxide reaction do not interfere
        structure.moveTo(group.carbon)
                .remove(group.halogen)
                .addGroup(LegacyMolecularStructure.alcohol());
        return reactionBuilder()
                .addReactant(reactant.getMolecule())
                .addReactant(DestroyMolecules.HYDROXIDE)
                .addProduct(getIon(group.halogen))
                .addProduct(moleculeBuilder().structure(structure).build())
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
