/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylyn.web.core;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.ProgressMonitorWrapper;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.mylyn.internal.web.core.InfiniteSubProgressMonitor;

/**
 * @since 2.3
 */
public class Policy {

	public static boolean DEBUG_STREAMS = false;

	static {
		if (WebCorePlugin.getDefault() != null && WebCorePlugin.getDefault().isDebugging()) {
			DEBUG_STREAMS = "true".equalsIgnoreCase(Platform.getDebugOption(WebCorePlugin.ID_PLUGIN + "/streams"));//$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	public static void advance(IProgressMonitor monitor, int worked) {
		if (monitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		monitor.worked(worked);
	}

	public static void checkCanceled(IProgressMonitor monitor) {
		if (monitor != null && monitor.isCanceled()) {
			throw new OperationCanceledException();
		}
	}

	/**
	 * @since 3.0
	 */
	public static boolean isBackgroundMonitor(IProgressMonitor monitor) {
		return monitor instanceof BackgroundProgressMonitor;
	}

	/**
	 * @since 3.0
	 */
	public static IProgressMonitor backgroundMonitorFor(IProgressMonitor monitor) {
		if (monitor == null) {
			return new NullProgressMonitor();
		}
		return new BackgroundProgressMonitor(monitor);
	}

	public static IProgressMonitor monitorFor(IProgressMonitor monitor) {
		if (monitor == null) {
			return new NullProgressMonitor();
		}
		return monitor;
	}

	/**
	 * @since 3.0
	 */
	public static IProgressMonitor monitorFor(IProgressMonitor monitor, boolean backgroundOperation) {
		if (monitor == null) {
			return new NullProgressMonitor();
		}
		if (backgroundOperation) {
			return backgroundMonitorFor(monitor);
		}
		return monitor;
	}

	public static IProgressMonitor subMonitorFor(IProgressMonitor monitor, int ticks) {
		if (monitor == null) {
			return new NullProgressMonitor();
		}
		if (monitor instanceof NullProgressMonitor) {
			return monitor;
		}
		if (monitor instanceof BackgroundProgressMonitor) {
			return new BackgroundProgressMonitor(new SubProgressMonitor(monitor, ticks));
		}
		return new SubProgressMonitor(monitor, ticks);
	}

	public static IProgressMonitor infiniteSubMonitorFor(IProgressMonitor monitor, int ticks) {
		if (monitor == null) {
			return new NullProgressMonitor();
		}
		if (monitor instanceof NullProgressMonitor) {
			return monitor;
		}
		if (monitor instanceof BackgroundProgressMonitor) {
			return new BackgroundProgressMonitor(new InfiniteSubProgressMonitor(monitor, ticks));
		}
		return new InfiniteSubProgressMonitor(monitor, ticks);
	}

	/**
	 * Wrapped progress monitor for background operations.
	 */
	private static class BackgroundProgressMonitor extends ProgressMonitorWrapper {

		protected BackgroundProgressMonitor(IProgressMonitor monitor) {
			super(monitor);
		}

	}

}
