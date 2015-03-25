package makasprzak.idea.plugins.propertiesstrategy;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiClass;
import makasprzak.idea.plugins.PsiClassAdapter;
import makasprzak.idea.plugins.mappers.PsiParameterMapper;
import makasprzak.idea.plugins.model.Property;

import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Lists.transform;
import static makasprzak.idea.plugins.PsiClassAdapter.forA;
import static makasprzak.idea.plugins.mappers.PsiParameterMapper.toProperty;

/**
 * Created by Maciej on 2015-03-24.
 */
public class UseConstructorArgsPropertiesStrategy implements PropertiesStrategy {
    private PsiClass psiClass;

    @Override
    public void start(PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    @Override
    public boolean isOk() {
        return true;
    }

    @Override
    public List<Property> getProperties() {
        return transform(forA(psiClass).getBiggestConstructorsArgs(), toProperty());
    }

}
