/*
 * Copyright (c) 2012, 2013, Credit Suisse (Anatole Tresch), Werner Keil.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package javax.money.function;

import static java.text.NumberFormat.getPercentInstance;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Locale;

import javax.money.MonetaryAmount;
import javax.money.MonetaryAdjuster;
import javax.money.Money;

import org.javamoney.ext.Displayable;

/**
 * This class allows to extract the percentage of a {@link MonetaryAmount}
 * instance.
 * 
 * @version 0.5
 * @author Werner Keil
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Percent">Wikipedia: Percentage</a>
 */
final class Percent<T extends MonetaryAmount> implements MonetaryAdjuster, Displayable {

	private static final MathContext DEFAULT_MATH_CONTEXT = initDefaultMathContext();
	private static final BigDecimal ONE_HUNDRED = new BigDecimal(100,
			DEFAULT_MATH_CONTEXT);

	private final BigDecimal percentValue;

	/**
	 * Access the shared instance of {@link Percent} for use.
	 * 
	 * @return the shared instance, never {@code null}.
	 */
	Percent(final BigDecimal decimal) {
		percentValue = calcPercent(decimal);
	}

	/**
	 * Get {@link MathContext} for {@link Percent} instances.
	 * 
	 * @return the {@link MathContext} to be used, by default
	 *         {@link MathContext#DECIMAL64}.
	 */
	private static MathContext initDefaultMathContext() {
		// TODO Initialize default, e.g. by system properties, or better:
		// classpath properties!
		return MathContext.DECIMAL64;
	}

	/**
	 * Gets the percentage of the amount.
	 * <p>
	 * This returns the monetary amount in percent. For example, for 10% 'EUR
	 * 2.35' will return 0.235.
	 * <p>
	 * This is returned as a {@code MonetaryAmount}.
	 * 
	 * @return the percent result of the amount, never {@code null}
	 */
	@Override
	public MonetaryAmount adjustInto(MonetaryAmount amount) {
		return Money.from(amount).multiply(
				percentValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getPercentInstance().format(percentValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.money.Displayable#getDisplayName(java.util.Locale)
	 */
	@Override
	public String getDisplayName(Locale locale) {
		return getPercentInstance(locale).format(percentValue);
	}

	/**
	 * Converts to {@link BigDecimal}, if necessary, or casts, if possible.
	 * 
	 * @param number
	 *            The {@link Number}
	 * @param mathContext
	 *            the {@link MathContext}
	 * @return the {@code number} as {@link BigDecimal}
	 */
	private static final BigDecimal getBigDecimal(Number number,
			MathContext mathContext) {
		if (number instanceof BigDecimal) {
			return (BigDecimal) number;
		} else {
			return new BigDecimal(number.doubleValue(), mathContext);
		}
	}

	/**
	 * Calculate a BigDecimal value for a Percent e.g. "3" (3 percent) will
	 * generate .03
	 * 
	 * @return java.math.BigDecimal
	 * @param decimal
	 *            java.math.BigDecimal
	 */
	private static final BigDecimal calcPercent(BigDecimal decimal) {
		return decimal.divide(ONE_HUNDRED, DEFAULT_MATH_CONTEXT); // we now have
																	// .03
	}

}
