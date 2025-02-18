package com.petrolpark.destroy.chemistry.legacy.index.genericreaction;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.chemistry.legacy.*;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.GenericReactant;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.SingleGroupGenericReaction;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyGroupTypes;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.legacy.index.group.CarbonylGroup;
import com.simibubi.create.AllTags;

public class CarbonylHydrogenation extends SingleGroupGenericReaction<CarbonylGroup> {

    public CarbonylHydrogenation() {
        super(Destroy.asResource("carbonyl_hydrogenation"), DestroyGroupTypes.CARBONYL);
    };

    @Override
    public boolean isPossibleIn(ReadOnlyMixture mixture) {
        return mixture.getConcentrationOf(DestroyMolecules.HYDROGEN) > 0f;
    };

    @Override
    public LegacyReaction generateReaction(GenericReactant<CarbonylGroup> reactant) {
        LegacySpecies reactantMolecule = reactant.getMolecule();
        CarbonylGroup carbonyl = reactant.getGroup();
        LegacySpecies productMolecule = moleculeBuilder().structure(reactantMolecule.shallowCopyStructure()
        .moveTo(carbonyl.carbon)
            .remove(carbonyl.oxygen)
            .addGroup(LegacyMolecularStructure.alcohol())
            .addAtom(LegacyElement.HYDROGEN)).build();

        return reactionBuilder()
            .addReactant(reactant.getMolecule())
            .addReactant(DestroyMolecules.HYDROGEN, 2)
            .addSimpleItemTagCatalyst(AllTags.forgeItemTag("dusts/nickel"), 1f)
            .addProduct(productMolecule)
            .build();
    };
    
};
