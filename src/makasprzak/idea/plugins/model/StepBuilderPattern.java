package makasprzak.idea.plugins.model;

import com.intellij.psi.PsiClass;

import java.util.List;

public class StepBuilderPattern {
    private final PsiClass builderClass;
    private final List<PsiClass> stepInterfaces;

    private StepBuilderPattern(PsiClass builderClass, List<PsiClass> stepInterfaces) {
        this.builderClass = builderClass;
        this.stepInterfaces = stepInterfaces;
    }

    public PsiClass getBuilderClass() {
        return builderClass;
    }

    public List<PsiClass> getStepInterfaces() {
        return stepInterfaces;
    }

    @Override
    public String toString() {
        return "StepBuilderPattern{" +
                "builderClass=" + builderClass +
                ", stepInterfaces=" + stepInterfaces +
                '}';
    }

    public static interface BuilderClassStep {
        StepInterfacesStep withBuilderClass(PsiClass builderClass);
    }

    public static interface StepInterfacesStep {
        BuildStep withStepInterfaces(List<PsiClass> stepInterfaces);
    }

    public static interface BuildStep {
        StepBuilderPattern build();
    }


    public static class Builder implements BuilderClassStep, StepInterfacesStep, BuildStep {
        private PsiClass builderClass;
        private List<PsiClass> stepInterfaces;

        private Builder() {
        }

        public static BuilderClassStep stepBuilderPattern() {
            return new Builder();
        }

        @Override
        public StepInterfacesStep withBuilderClass(PsiClass builderClass) {
            this.builderClass = builderClass;
            return this;
        }

        @Override
        public BuildStep withStepInterfaces(List<PsiClass> stepInterfaces) {
            this.stepInterfaces = stepInterfaces;
            return this;
        }

        @Override
        public StepBuilderPattern build() {
            return new StepBuilderPattern(
                    this.builderClass,
                    this.stepInterfaces
            );
        }
    }
}
