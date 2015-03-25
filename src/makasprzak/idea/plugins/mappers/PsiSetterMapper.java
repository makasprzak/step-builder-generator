package makasprzak.idea.plugins.mappers;

import com.google.common.base.Function;
import com.intellij.psi.PsiMethod;
import makasprzak.idea.plugins.model.Property;
import org.apache.commons.lang.StringUtils;

import static makasprzak.idea.plugins.model.Property.Builder.property;

public class PsiSetterMapper implements PsiMapper<PsiMethod>{

    @Override
    public Property map(PsiMethod psiMethod) {
        return property()
                .withName(getPropertyName(psiMethod))
                .withType(getPropertyType(psiMethod))
                .build();
    }

    private String getPropertyType(PsiMethod psiMethod) {
        return psiMethod.getParameterList().getParameters()[0].getType().getPresentableText();
    }

    private String getPropertyName(PsiMethod psiMethod) {
        return StringUtils.uncapitalize(psiMethod.getName().substring(3));
    }

    public static Function<PsiMethod, Property> toProperty() {
        return new Function<PsiMethod, Property>() {
            private final PsiSetterMapper mapper = new PsiSetterMapper();
            @Override
            public Property apply(PsiMethod psiSetter) {
                return mapper.map(psiSetter);
            }
        };
    }

}
