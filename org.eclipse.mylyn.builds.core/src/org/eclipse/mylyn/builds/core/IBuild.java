/*******************************************************************************
 * Copyright (c) 2010 Markus Knittig and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Markus Knittig - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.builds.core;

import java.util.List;

/**
 * @author Markus Knittig
 */
public interface IBuild {

	String getId();

	int getBuildNumber();

	long getTimestamp();

	long getDuration();

	String getDisplayName();

	BuildState getState();

	BuildStatus getStatus();

	List<IArtifact> getArtifacts();

	IChangeSet getChangeSet();

}
