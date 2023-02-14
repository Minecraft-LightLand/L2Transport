package dev.xkmc.lasertransport.content.configurables;

public record NumericConfigurator(String baseUnit, int relative, int scale) {

	public static final NumericConfigurator ITEM = new NumericConfigurator("", 0, 8);
	public static final NumericConfigurator FLUID = new NumericConfigurator("B", -1, 10);

	private static final int PREFIX_START = -3;
	private static final String[] PREFIXES = {"n", "u", "m", "", "k", "M", "G", "T"};

	public String format(long num) {
		if (num == 0) return "*";
		long val = Math.max(0, num);
		if (baseUnit.length() == 0) {
			return val + "";
		}
		int i = relative;
		StringBuilder suf = new StringBuilder();
		while (val > 0) {
			int rem = (int) (val % 1000);
			val /= 1000;
			if (rem > 0) {
				if (suf.length() > 0) suf.insert(0, " ");
				suf.insert(0, rem + " " + PREFIXES[i - PREFIX_START] + baseUnit);
			}
			i++;
		}
		return suf.toString();
	}

	public long getStep(int ind) {
		long ans = 1;
		for (int i = 0; i < ind; i++) {
			ans *= scale;
		}
		return ans;
	}

}
