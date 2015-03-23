package makasprzak.idea.plugins.generationstrategy;

import com.intellij.psi.PsiClass;
import makasprzak.idea.plugins.dialog.DialogFactory;
import makasprzak.idea.plugins.dialog.GeneratorDialog;
import makasprzak.idea.plugins.model.Property;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GenerationConcreteStrategyTest {
    
    @Mock private PsiClass psiClass;
    @Mock private DialogFactory generatorDialogFactory;
    @Mock private GeneratorDialog generatorDialog;
    @Mock private List<Property> properties;
    @InjectMocks private GenerateFromFieldsCS generateFromFieldsCS;

    @Test
    public void shouldGenerateFromFields() throws Exception {
        given(generatorDialogFactory.create(psiClass)).willReturn(generatorDialog);
        given(generatorDialog.getProperties()).willReturn(properties);
        given(generatorDialog.isOK()).willReturn(true);
        generateFromFieldsCS.start(psiClass);
        verify(generatorDialog).show();
        assertThat(generateFromFieldsCS.isOk()).isTrue();
        assertThat(generateFromFieldsCS.getProperties()).isEqualTo(properties);
    }

    @Test
    public void shouldNotGenerateFromFields() throws Exception {
        given(generatorDialogFactory.create(psiClass)).willReturn(generatorDialog);
        given(generatorDialog.isOK()).willReturn(false);
        generateFromFieldsCS.start(psiClass);
        assertThat(generateFromFieldsCS.isOk()).isFalse();
    }
}