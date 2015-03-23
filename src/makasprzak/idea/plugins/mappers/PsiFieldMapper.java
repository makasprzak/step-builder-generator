package makasprzak.idea.plugins.mappers;

import com.google.common.base.Function;
import com.intellij.psi.PsiField;
import makasprzak.idea.plugins.model.Property;

import static makasprzak.idea.plugins.model.Property.Builder.property;

public class PsiFieldMapper implements PsiMapper<PsiField>{
    @Override
    public Property map(PsiField psiField) {
        return property()
                .withName(psiField.getName())
                .withType(psiField.getType().getPresentableText())
                .build();
    }

    public static Function<PsiField, Property> toProperty() {
        return new Function<PsiField, Property>() {
            private final PsiFieldMapper mapper = new PsiFieldMapper();
            @Override
            public Property apply(PsiField psiField) {
                return mapper.map(psiField);
            }
        };
    }

}
