package makasprzak.idea.plugins.model;

import com.google.common.base.Objects;
import com.intellij.psi.PsiElement;

public class PsiPropertyContainer {
   private final PsiElement psiElement;
   private final Property property;

   private PsiPropertyContainer(PsiElement psiElement, Property property) {
      this.psiElement = psiElement;
      this.property = property;
   }

   public PsiElement getPsiElement() {
      return psiElement;
   }

   public Property getProperty() {
      return property;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      PsiPropertyContainer that = (PsiPropertyContainer) o;

      return Objects.equal(this.psiElement, that.psiElement) &&
         Objects.equal(this.property, that.property);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(psiElement, property);
   }

   public static interface PsiElementStep {
      PropertyStep withPsiElement(PsiElement psiElement);
   }

   public static interface PropertyStep {
      BuildStep withProperty(Property property);
   }

   public static interface BuildStep {
      PsiPropertyContainer build();
   }

   public static class Builder implements PsiElementStep, PropertyStep, BuildStep {
      private PsiElement psiElement;
      private Property property;

      private Builder() {
      }

      public static PsiElementStep psiPropertyContainer() {
         return new Builder();
      }

      @Override
      public PropertyStep withPsiElement(PsiElement psiElement) {
         this.psiElement = psiElement;
         return this;
      }

      @Override
      public BuildStep withProperty(Property property) {
         this.property = property;
         return this;
      }

      @Override
      public PsiPropertyContainer build() {
         return new PsiPropertyContainer(
            this.psiElement,
            this.property
         );
      }
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this)
         .add("psiElement", psiElement)
         .add("property", property)
         .toString();
   }


}
