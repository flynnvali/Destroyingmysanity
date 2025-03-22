package com.petrolpark.destroy.chemistry.legacy.index.genericreaction;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.chemistry.legacy.*;
import com.petrolpark.destroy.chemistry.legacy.LegacyBond.BondType;
import com.petrolpark.destroy.chemistry.legacy.LegacyReaction.ReactionBuilder;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.GenericReactant;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.SingleGroupGenericReaction;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyGroupTypes;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.legacy.index.group.AlcoholGroup;
import com.petrolpark.destroy.chemistry.legacy.index.group.HalideGroup;

import java.util.List;

public class DoubleElimination extends SingleGroupGenericReaction<HalideGroup> {

    public DoubleElimination() {
        super(Destroy.asResource("double_elimination"), DestroyGroupTypes.HALIDE);
    };

    @Override
    public boolean isPossibleIn(ReadOnlyMixture mixture) {
        return mixture.getConcentrationOf(DestroyMolecules.AZANIDE) > 0f;
    };

    @Override
    public LegacyReaction generateReaction(GenericReactant<HalideGroup> reactant) {
        LegacyMolecularStructure structure = reactant.getMolecule().shallowCopyStructure();
        HalideGroup halide = reactant.getGroup();

        ReactionBuilder builder = reactionBuilder();

        int products = 0;
        for (LegacyAtom carbon : structure.moveTo(halide.carbon).getBondedAtomsOfElement(LegacyElement.CARBON)) {
            List<LegacyAtom> hydrogens = structure.moveTo(carbon).getBondedAtomsOfElement(LegacyElement.HYDROGEN);
            List<LegacyAtom> additionalChlorine = structure.moveTo(carbon).getBondedAtomsOfElement(LegacyElement.CHLORINE);
            List<LegacyAtom> additionalIodine = structure.moveTo(carbon).getBondedAtomsOfElement(LegacyElement.IODINE);
            List<LegacyAtom> carbons = structure.getBondedAtomsOfElement(LegacyElement.CARBON);
            List<LegacyAtom> rGroups = structure.getBondedAtomsOfElement(LegacyElement.R_GROUP);
            if (carbons.size() > 2  || hydrogens.size() == 0 || additionalChlorine.size() + additionalIodine.size() == 0) continue; // Don't form from non-sp3 alkyl carbons
            else if (additionalChlorine.size() != 0 && halide.halogen.getElement() == LegacyElement.CHLORINE) {
                LegacyMolecularStructure productStructure = structure.shallowCopy();
                productStructure
                        .remove(hydrogens.get(0))
                        .remove(halide.halogen)
                        .moveTo(carbon)
                        .remove(additionalChlorine.get(0))
                        .replaceBondTo(halide.carbon, BondType.TRIPLE);
                builder.addProduct(moleculeBuilder().structure(productStructure).build());
                products++;
            } else if (additionalIodine.size() != 0 && halide.halogen.getElement() == LegacyElement.IODINE){
                LegacyMolecularStructure productStructure = structure.shallowCopy();
                productStructure
                        .remove(hydrogens.get(0))
                        .remove(halide.halogen)
                        .moveTo(carbon)
                        .remove(additionalIodine.get(0))
                        .replaceBondTo(halide.carbon, BondType.TRIPLE);
                builder.addProduct(moleculeBuilder().structure(productStructure).build());
                products++;
            }
        };

        if (products == 0) return null;

        builder
            .addReactant(reactant.getMolecule(), products)
            .addReactant(DestroyMolecules.AZANIDE, products * 2)
            .addProduct(getIon(halide.halogen))
            .addProduct(DestroyMolecules.AMMONIA, products * 2);

        return builder.build();
    };

    @Override
    public LegacyReaction generateExampleReaction() {
        LegacyAtom carbon = new LegacyAtom(LegacyElement.CARBON);
        LegacyAtom chlorine = new LegacyAtom(LegacyElement.CHLORINE);
        LegacyAtom hydrogen = new LegacyAtom(LegacyElement.HYDROGEN);
        LegacyAtom r1 = new LegacyAtom(LegacyElement.R_GROUP);
        r1.rGroupNumber = 1;
        LegacyAtom r2 = new LegacyAtom(LegacyElement.R_GROUP);
        r2.rGroupNumber = 2;

        return generateReaction(
            new GenericReactant<HalideGroup>(
                moleculeBuilder().structure(
                    LegacyMolecularStructure.atom(LegacyElement.CARBON)
                        .addAtom(LegacyElement.HYDROGEN)
                        .addAtom(LegacyElement.CHLORINE)
                        .addAtom(r1)
                        .addAtom(carbon)
                        .moveTo(carbon)
                        .addAtom(r2)
                        .addAtom(hydrogen)
                        .addAtom(chlorine)
                ).build(),
                new HalideGroup(carbon, chlorine, 2)
            )
        );
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
