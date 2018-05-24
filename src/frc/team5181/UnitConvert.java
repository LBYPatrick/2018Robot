package frc.team5181;

public class UnitConvert {
	public static double centimeterToInch(double centimeter){
		return (centimeter/2.54);
	}
	
	public static double inchToCentimeter(double inch){
		return (inch*2.54);
	}
	
	public static double millimeterToInch(double millimeter){
		return centimeterToInch(millimeter*10);
	}
	
	public static double inchToMillimeter(double inch){
		return inchToCentimeter(inch)*10;
	}
	
	public static double meterToFoot(double meter){
		return (meter*100/30.48);
	}
	
	public static double footToMeter(double foot){
		return (foot*30.48/100);
	}
}
