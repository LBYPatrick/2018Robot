package frc.team5181.pid;

@FunctionalInterface
public interface PIDReducer {
	double reduce(double accumulator, double currentValue, double currentIndex);
}
