package frc.team5181.pid;

/*package*/ class PIDWrappedTarget implements PIDTarget {

  private PIDTarget original;
  private PIDTranslator translator;

  /*package*/ PIDWrappedTarget(PIDTarget original, PIDTranslator translator) {
    this.original = original;
    this.translator = translator;
  }

  @Override
  public void pidAdjust(double amount) {
    original.pidAdjust(translator.translate(amount));
  }

}
