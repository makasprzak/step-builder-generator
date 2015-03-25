package makasprzak.idea.plugins.dialog;

import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import makasprzak.idea.plugins.model.PsiPropertyContainer;

import javax.swing.*;
import java.awt.*;

public class PropertyCellRenderer implements ListCellRenderer<PsiPropertyContainer> {
    private final DefaultPsiElementCellRenderer renderer = new DefaultPsiElementCellRenderer();

    @Override
    public Component getListCellRendererComponent(JList<? extends PsiPropertyContainer> list,
                                                  PsiPropertyContainer value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
        return renderer.getListCellRendererComponent(list,value.getPsiElement(),index,isSelected,cellHasFocus);
    }
}
