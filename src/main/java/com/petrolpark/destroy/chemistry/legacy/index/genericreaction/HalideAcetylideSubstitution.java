package com.petrolpark.destroy.chemistry.legacy.index.genericreaction;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.chemistry.legacy.LegacyBond.BondType;
import com.petrolpark.destroy.chemistry.legacy.LegacyReaction;
import com.petrolpark.destroy.chemistry.legacy.LegacyMolecularStructure;
import com.petrolpark.destroy.chemistry.legacy.ReadOnlyMixture;
import com.petrolpark.destroy.chemistry.legacy.LegacyElement;
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


   // @Override
   // public LegacyReaction generateReaction(GenericReactant<HalideGroup> firstReactant, GenericReactant<AcetylideGroup> secondReactant) {
    //    LegacyMolecularStructure halideStructureCopy = firstReactant.getMolecule().shallowCopyStructure();
    //    HalideGroup halideGroup = firstReactant.getGroup();
    //    LegacyMolecularStructure acetylideStructureCopy = secondReactant.getMolecule().shallowCopyStructure();
      //  AcetylideGroup acetylideGroup = secondReactant.getGroup();

      //  acetylideStructureCopy.moveTo(acetylideGroup.neutralCarbon);
       // acetylideStructureCopy.remove(acetylideGroup.carbonWithCharge);
      //  acetylideStructureCopy.addAtom(new LegacyAtom(LegacyElement.CARBON), BondType.TRIPLE);

      //  halideStructureCopy.moveTo(HalideGroup.getCarbon);
      //  halideStructureCopy.remove(HalideGroup.getHalogen);

      //  LegacySpecies ccBond = moleculeBuilder().structure(LegacyMolecularStructure.joinFormulae(halideStructureCopy, acetylideStructureCopy, BondType.SINGLE)).build();



      //  ReactionBuilder builder = reactionBuilder()

         //       .addReactant(firstReactant.getMolecule())
         //       .addReactant(secondReactant.getMolecule())
        //        .addProduct(ccBond)
          //      .addProduct(getIon(halideGroup.halogen));
           //     return builder.build();
    @Override
    public LegacyReaction generateReaction(GenericReactant<HalideGroup> firstReactant, GenericReactant<AcetylideGroup> secondReactant) {
        LegacyMolecularStructure halideGroup = firstReactant.molecule.shallowCopyStructure();
        // HalideGroup halide = firstReactant.getGroup();
        LegacyMolecularStructure acetylideGroup = secondReactant.molecule.shallowCopyStructure();
        halideGroup.moveTo(firstReactant.group.carbon)
                .remove(firstReactant.group.halogen);
       acetylideGroup.moveTo(secondReactant.group.neutralCarbon)
                .remove(secondReactant.group.carbonWithCharge)
                .addAtom(LegacyElement.CARBON, BondType.TRIPLE);
       // acetylideGroup.moveTo(secondReactant.group.carbonWithCharge);
        LegacyMolecularStructure product = LegacyMolecularStructure.joinFormulae(halideGroup, acetylideGroup, BondType.SINGLE);
        return reactionBuilder()
                .addReactant(firstReactant.molecule)
                .addReactant(secondReactant.molecule)
                .addProduct(moleculeBuilder().structure(product).build())
                .addProduct(DestroyMolecules.CHLORIDE)
                .build();
    };


   // };

  //  public void transform(LegacyReaction.ReactionBuilder builder, HalideGroup group) {};

   // public LegacySpecies getIon(LegacyAtom atom) {
   //     switch (atom.getElement()) {
   //         case FLUORINE:
   //             return DestroyMolecules.FLUORIDE;
    //        case CHLORINE:
  //              return DestroyMolecules.CHLORIDE;
   //         case IODINE:
    //            return DestroyMolecules.IODIDE;
   //         default:
      //          throw new GenericReactionGenerationException(atom.getElement().toString()+" is not a halogen.");






};
