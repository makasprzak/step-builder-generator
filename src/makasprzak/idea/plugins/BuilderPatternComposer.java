package makasprzak.idea.plugins;

import makasprzak.idea.plugins.model.Pojo;
import makasprzak.idea.plugins.model.PsiPojo;
import makasprzak.idea.plugins.model.StepBuilderPattern;

public interface BuilderPatternComposer {
    @Deprecated
    StepBuilderPattern build(PsiPojo psiPojo);
    StepBuilderPattern build(Pojo pojo);
}
