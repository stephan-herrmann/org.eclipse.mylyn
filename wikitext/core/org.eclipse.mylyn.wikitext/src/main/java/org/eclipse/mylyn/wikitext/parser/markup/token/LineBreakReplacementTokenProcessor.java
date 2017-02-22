/*******************************************************************************
 * Copyright (c) 2007, 2011 David Green and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     David Green - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylyn.wikitext.parser.markup.token;

import org.eclipse.mylyn.wikitext.parser.DocumentBuilder;
import org.eclipse.mylyn.wikitext.parser.markup.PatternBasedElementProcessor;

/**
 * A processor that emits a line break.
 *
 * @see DocumentBuilder#lineBreak()
 * @author David Green
 * @since 3.0
 */
public class LineBreakReplacementTokenProcessor extends PatternBasedElementProcessor {

	@Override
	public void emit() {
		getBuilder().lineBreak();
	}

}
