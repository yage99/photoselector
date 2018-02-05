/**
 * 
 */
package com.yage.research.tools.picselector;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * @author zhangya
 *
 */
public class TestRegularExpression {
	/**
	 * 
	 */
	@Test
	public void testRegularExpression() {
		Pattern pattern = Pattern.compile("TCGA-\\w{2}-\\w{4}-\\w{3}-\\w{2}-\\w{3}");
		String name = "TCGA-08-0352-01A-01-TS1_290.png";
		Matcher matcher = pattern.matcher(name);
		matcher.find();

		assertEquals(matcher.group(), "TCGA-08-0352-01A-01-TS1");
	}
}
