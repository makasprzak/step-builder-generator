package makasprzak.idea.plugins.mappers;

import com.intellij.psi.PsiField;
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
public class PsiFieldMapperTest {
    
    @InjectMocks private PsiFieldMapper mapper;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private PsiField field;
    
    @Test
    public void shouldMapStringFieldToProperty() throws Exception {
        when(field.getName()).thenReturn("someStringProperty");
        when(field.getType().getPresentableText()).thenReturn("String");
        assertThat(mapper.map(field)).isEqualTo(property()
                .withName("someStringProperty")
                .withType("String")
                .build());

    }
}