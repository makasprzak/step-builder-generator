package makasprzak.idea.plugins.properties;

import com.intellij.psi.PsiClass;
import makasprzak.idea.plugins.dialog.DialogFactory;
import makasprzak.idea.plugins.dialog.GeneratorDialog;
import makasprzak.idea.plugins.model.Property;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AskUserPropertiesProviderTest {

    @Mock private PropertiesConsumer propertiesConsumer;
    @Mock private DialogFactory dialogFactory;
    @InjectMocks private AskUserPropertiesProvider propertiesProvider;

    @Mock private GeneratorDialog dialog;
    @Mock private PsiClass psiClass;
    @Mock private List<Property> properties;

    @Test
    public void shouldGetPropertiesUsingDialog() throws Exception {
        given(dialogFactory.create(psiClass)).willReturn(dialog);
        given(dialog.isOK()).willReturn(true);
        given(dialog.getProperties()).willReturn(properties);

        propertiesProvider.getProperties(psiClass, propertiesConsumer);

        verify(propertiesConsumer).consume(properties);
    }

    @Test
    public void shouldNotGetPropertiesWhenDialogNotOk() throws Exception {
        given(dialogFactory.create(psiClass)).willReturn(dialog);
        given(dialog.isOK()).willReturn(false);
        given(dialog.getProperties()).willReturn(properties);

        propertiesProvider.getProperties(psiClass, propertiesConsumer);

        verify(propertiesConsumer, never()).consume(properties);
    }

    @Test
    public void shouldShowDialogBeforeCheckingResults() throws Exception {
        given(dialogFactory.create(psiClass)).willReturn(dialog);
        given(dialog.isOK()).willReturn(false);
        given(dialog.getProperties()).willReturn(properties);

        propertiesProvider.getProperties(psiClass, propertiesConsumer);

        InOrder inOrder = inOrder(dialog);
        inOrder.verify(dialog).show();
        inOrder.verify(dialog).isOK();
    }
}