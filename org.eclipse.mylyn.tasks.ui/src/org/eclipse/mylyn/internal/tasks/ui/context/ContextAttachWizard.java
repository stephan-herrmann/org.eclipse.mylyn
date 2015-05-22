/*******************************************************************************
 * Copyright (c) 2004, 2012 Tasktop Technologies and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.internal.tasks.ui.context;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.mylyn.internal.tasks.ui.util.AttachmentUtil;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tasks.ui.TasksUiImages;

/**
 * @author Rob Elves
 * @author Steffen Pingel
 */
public class ContextAttachWizard extends Wizard {

	private final TaskRepository repository;

	private final ITask task;

	private ContextAttachWizardPage wizardPage;

	public ContextAttachWizard(ITask task) {
		this.repository = TasksUi.getRepositoryManager()
				.getRepository(task.getConnectorKind(), task.getRepositoryUrl());
		this.task = task;
		setWindowTitle(Messages.ContextAttachWizard_Attach_Context);
		setDefaultPageImageDescriptor(TasksUiImages.BANNER_REPOSITORY_CONTEXT);
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		wizardPage = new ContextAttachWizardPage(repository, task);
		addPage(wizardPage);
		super.addPages();
	}

	@Override
	public final boolean performFinish() {
		return AttachmentUtil.uploadContext(repository, task, wizardPage.getComment(), getContainer());
	}

}
