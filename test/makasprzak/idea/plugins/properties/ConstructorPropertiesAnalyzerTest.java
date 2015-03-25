package makasprzak.idea.plugins.properties;

import com.intellij.psi.*;
import makasprzak.idea.plugins.model.Property;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static makasprzak.idea.plugins.model.Property.Builder.property;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorPropertiesAnalyzerTest {
    @Mock
    private PsiClass psiClass;
    @Mock private List<Property> properties;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private PsiMethod constructor;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private PsiMethod secondConstructor;
    @Mock private PsiParameterList parameterList;
    private final PsiParameter[] parameters = new PsiParameter[]{
            psiParameter("String", "someParameter"),
            psiParameter("int", "otherParameter")};

    @InjectMocks private ConstructorPropertiesAnalyzer propertiesAnalyzer;

    @Test
    public void shouldGetPropertiesFromBiggestConstructor() throws Exception {
        given(psiClass.getConstructors()).willReturn(new PsiMethod[]{constructor, secondConstructor});
        given(constructor.getParameterList().getParametersCount()).willReturn(1);
        given(secondConstructor.getParameterList().getParametersCount()).willReturn(2);
        given(secondConstructor.getParameterList().getParameters()).willReturn(parameters);
        assertThat(propertiesAnalyzer.getBiggestConstructorArgs(psiClass))
                .containsExactly(
                        property().withName("someParameter").withType("String").build(),
                        property().withName("otherParameter").withType("int").build()
                );
    }

    private PsiParameter psiParameter(String type, String name) {
        PsiParameter parameter = mock(PsiParameter.class);
        PsiType psiType = mock(PsiType.class);
        given(parameter.getType()).willReturn(psiType);
        given(psiType.getPresentableText()).willReturn(type);
        given(parameter.getName()).willReturn(name);
        return parameter;
    }
}