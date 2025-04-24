package com.petrolpark.destroy.chemistry.legacy.index.genericreaction;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.chemistry.legacy.*;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.GenericReactant;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.SingleGroupGenericReaction;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyGroupTypes;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.legacy.index.group.GeminalDihalideGroup;

public class GeminalDihalideHydroxideSubstitution extends SingleGroupGenericReaction<GeminalDihalideGroup> {

    public GeminalDihalideHydroxideSubstitution() {
        super(Destroy.asResource("geminal_dihalide_hydroxide_substitution"), DestroyGroupTypes.GEMINAL_DIHALIDE);
    };

    @Override
    public boolean isPossibleIn(ReadOnlyMixture mixture) {
        return mixture.getConcentrationOf(DestroyMolecules.HYDROXIDE) > 0f;
    };

    @Override
    public LegacyReaction generateReaction(GenericReactant<GeminalDihalideGroup> reactant) {
        GeminalDihalideGroup group = reactant.getGroup();
        LegacyMolecularStructure structure = reactant.molecule.shallowCopyStructure();
        if (reactant.getMolecule() == DestroyMolecules.DICHLOROMETHANE) return null;
        structure.moveTo(group.carbon)
            .remove(group.firstHalogen)
            .remove(group.secondHalogen)
            .addCarbonyl();
        return reactionBuilder()
            .addReactant(reactant.getMolecule())
            .addReactant(DestroyMolecules.HYDROXIDE)
            .addProduct(moleculeBuilder().structure(structure).build())
            .addProduct(getAcid(group.firstHalogen), 2) // with a little bit of effort this could spit out both acids in the case of a dihalide with two different halides! unfortunately i am lazy
            .build();
    };

    public LegacySpecies getAcid(LegacyAtom atom) {
        switch (atom.getElement()) {
            case FLUORINE:
                return DestroyMolecules.HYDROFLUORIC_ACID;
            case CHLORINE:
                return DestroyMolecules.HYDROCHLORIC_ACID;
            case IODINE:
                return DestroyMolecules.HYDROGEN_IODIDE;
            default:
                throw new GenericReactionGenerationException(atom.getElement().toString()+" is not a halogen.");
        }
    };
    
};
