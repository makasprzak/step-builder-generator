package makasprzak.idea.plugins.model;

import com.google.common.base.Objects;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;

import java.util.List;

public class PsiPojo {
    private final PsiClass psiClass;
    private final List<PsiField> fields;

    private PsiPojo(PsiClass psiClass, List<PsiField> fields) {
        this.psiClass = psiClass;
        this.fields = fields;
    }

    public PsiClass getPsiClass() {
        return psiClass;
    }

    public List<PsiField> getFields() {
        return fields;
    }



    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("psiClass", psiClass)
                .add("fields", fields)
                .toString();
    }

    public static interface PsiClassStep {
        FieldsStep withPsiClass(PsiClass psiClass);
    }

    public static interface FieldsStep {
        BuildStep withFields(List<PsiField> fields);
    }

    public static interface BuildStep {
        PsiPojo build();
    }


    public static class Builder implements PsiClassStep, FieldsStep, BuildStep {
        private PsiClass psiClass;
        private List<PsiField> fields;

        private Builder() {
        }

        public static PsiClassStep psiPojo() {
            return new Builder();
        }

        @Override
        public FieldsStep withPsiClass(PsiClass psiClass) {
            this.psiClass = psiClass;
            return this;
        }

        @Override
        public BuildStep withFields(List<PsiField> fields) {
            this.fields = fields;
            return this;
        }

        @Override
        public PsiPojo build() {
            return new PsiPojo(
                    this.psiClass,
                    this.fields
            );
        }
    }
}
