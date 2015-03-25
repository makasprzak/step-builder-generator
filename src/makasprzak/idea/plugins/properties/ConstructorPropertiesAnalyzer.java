package makasprzak.idea.plugins.properties;

import com.intellij.psi.PsiClass;
import makasprzak.idea.plugins.PsiClassAdapter;
import makasprzak.idea.plugins.model.Property;

import java.util.List;

import static com.google.common.collect.Lists.transform;
import static makasprzak.idea.plugins.mappers.PsiParameterMapper.toProperty;

public class ConstructorPropertiesAnalyzer {

    public List<Property> getBiggestConstructorArgs(PsiClass psiClass) {
        return transform(PsiClassAdapter.forA(psiClass).getBiggestConstructorsArgs(), toProperty());
    }
}
