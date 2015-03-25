package makasprzak.idea.plugins.dialog;

/**
 * @author mkasprzak
 */
public enum DialogFactories {
   FROM_METHODS(new FromSettersDialogFactory());
   private final DialogFactory dialogFactory;

   private DialogFactories(DialogFactory dialogFactory) {

      this.dialogFactory = dialogFactory;
   }

   public DialogFactory get(){
      return this.dialogFactory;
   }
}
