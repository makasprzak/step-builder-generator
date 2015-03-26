package makasprzak.idea.plugins.dialog;

import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.psi.PsiElement;
import makasprzak.idea.plugins.model.PsiPropertyContainer;

import javax.swing.*;
import java.awt.*;

public class PropertyCellRenderer implements ListCellRenderer {
    private final DefaultPsiElementCellRenderer renderer = new DefaultPsiElementCellRenderer();

    @Override
    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
        return renderer.getListCellRendererComponent(list, getValue(value),index,isSelected,cellHasFocus);
    }

    private PsiElement getValue(Object value) {
        return ((PsiPropertyContainer)value).getPsiElement();
    }
}
