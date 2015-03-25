package makasprzak.idea.plugins.properties;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import makasprzak.idea.plugins.model.Property;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class UseConstructorArgsPropertiesStrategyTest {
    @InjectMocks private UseConstructorArgsPropertiesStrategy strategy;

    @Mock private PsiClass psiClass;
    @Mock private List<Property> properties;
    @Mock private PsiMethod constructor;
    @Mock private PsiParameterList parameterList;
    private final PsiParameter[] parameters = new PsiParameter[]{
            mock(PsiParameter.class,"firstParameter"),
            mock(PsiParameter.class, "secondParameter")};

    @Test
    public void shouldAlwaysReturnOK() throws Exception {
        assertThat(strategy.isOk()).isTrue();
    }

    @Ignore("todo - introduce google juice and clean up DI mess")
    @Test
    public void shouldUseConstructorArgs() throws Exception {
        given(parameterList.getParameters()).willReturn(parameters);
        given(parameterList.getParametersCount()).willReturn(parameters.length);
        given(constructor.getParameterList()).willReturn(parameterList);
        given(psiClass.getConstructors()).willReturn(new PsiMethod[]{});
        strategy.start(psiClass);

    }
}