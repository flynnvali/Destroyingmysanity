package com.petrolpark.destroy.chemistry.legacy.index.group;

import com.petrolpark.destroy.chemistry.legacy.LegacyAtom;
import com.petrolpark.destroy.chemistry.legacy.LegacyFunctionalGroup;
import com.petrolpark.destroy.chemistry.legacy.LegacyFunctionalGroupType;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyGroupTypes;

public class GeminalDihalideGroup extends LegacyFunctionalGroup<GeminalDihalideGroup> {

    public final LegacyAtom carbon;
    public final  LegacyAtom firstHalogen;
    public final  LegacyAtom secondHalogen;
    public int degree;

    public GeminalDihalideGroup(LegacyAtom carbon, LegacyAtom firstHalogen, LegacyAtom secondHalogen,  int degree) {
        this.carbon = carbon;
        this.firstHalogen = firstHalogen;
        this.secondHalogen = secondHalogen;
        this.degree = degree;
    };


    @Override
    public LegacyFunctionalGroupType<GeminalDihalideGroup> getType() {
        return DestroyGroupTypes.GEMINAL_DIHALIDE;
    };
    
};
