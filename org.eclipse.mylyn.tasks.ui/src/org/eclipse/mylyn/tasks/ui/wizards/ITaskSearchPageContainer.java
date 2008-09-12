/*******************************************************************************
* Copyright (c) 2004, 2008 Tasktop Technologies and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.tasks.ui.wizards;

import org.eclipse.jface.operation.IRunnableContext;

/**
 * @author Steffen Pingel
 * @since 3.0
 */
public interface ITaskSearchPageContainer {

	public abstract IRunnableContext getRunnableContext();

	public abstract void setPerformActionEnabled(boolean enabled);

}
