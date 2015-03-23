package makasprzak.idea.plugins.mappers;

import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import makasprzak.idea.plugins.model.Property;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PsiParameterMapperTest {

   @InjectMocks private PsiParameterMapper psiParameterMapper;
   @Mock(answer = Answers.RETURNS_DEEP_STUBS) private PsiParameter psiParameter;

   @Test
   public void shouldMapParameter() throws Exception {
      when(psiParameter.getName()).thenReturn("someParameterName");
      when(psiParameter.getType().getPresentableText()).thenReturn("String");
      Assertions.assertThat(psiParameterMapper.map(psiParameter)).isEqualTo(
         Property.Builder.property()
            .withName("someParameterName")
            .withType("String")
            .build()
      );

   }

   @Test
   public void shouldMapOtherParameter() throws Exception {
      when(psiParameter.getName()).thenReturn("otherParameterName");
      when(psiParameter.getType()).thenReturn(PsiType.BOOLEAN);
      Assertions.assertThat(psiParameterMapper.map(psiParameter)).isEqualTo(
         Property.Builder.property()
            .withName("otherParameterName")
            .withType("boolean")
            .build()
      );

   }
}