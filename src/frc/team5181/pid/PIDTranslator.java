package frc.team5181.pid;

@FunctionalInterface
public interface PIDTranslator {
	double translate(double data);
}
