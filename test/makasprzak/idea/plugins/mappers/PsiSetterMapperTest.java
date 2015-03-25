package makasprzak.idea.plugins.mappers;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static makasprzak.idea.plugins.model.Property.Builder.property;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PsiSetterMapperTest {
    
    @InjectMocks private PsiSetterMapper mapper;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private PsiMethod setter;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private PsiParameter setterArg;
    
    @Test
    public void shouldMapStringFieldToProperty() throws Exception {
        when(setter.getName()).thenReturn("setSomeStringProperty");
        when(setter.getParameterList().getParameters()).thenReturn(new PsiParameter[]{setterArg});
        when(setterArg.getType().getPresentableText()).thenReturn("String");
        assertThat(mapper.map(setter)).isEqualTo(property()
                .withName("someStringProperty")
                .withType("String")
                .build());

    }
}