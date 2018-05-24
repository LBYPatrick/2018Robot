package frc.team5181.pid;

import java.util.List;

@FunctionalInterface
public interface PIDCombiner {
	double combine(List<Double> data);
}
