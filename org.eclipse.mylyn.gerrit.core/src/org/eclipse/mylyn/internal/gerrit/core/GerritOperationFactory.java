/*******************************************************************************
 * Copyright (c) 2011 Tasktop Technologies and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop Technologies - initial API and implementation
 *     Sascha Scholz (SAP) - improvements
 *     Guy Perron 423242: Add ability to edit comment from compare navigator popup
 *******************************************************************************/

package org.eclipse.mylyn.internal.gerrit.core;

import org.eclipse.mylyn.internal.gerrit.core.client.GerritClient;
import org.eclipse.mylyn.internal.gerrit.core.operations.AbstractRequest;
import org.eclipse.mylyn.internal.gerrit.core.operations.GerritOperation;
import org.eclipse.mylyn.tasks.core.IRepositoryManager;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;

/**
 * @author Steffen Pingel
 * @author Sascha Scholz
 * @author Guy Perron
 */
public class GerritOperationFactory {

	private final IRepositoryManager repositoryManager;

	public GerritOperationFactory(IRepositoryManager repositoryManager) {
		this.repositoryManager = repositoryManager;
	}

	public <T> GerritOperation<T> createOperation(ITask review, AbstractRequest<T> request) {
		return new GerritOperation<T>(request.getOperationName(), getClient(review), request);
	}

	public GerritClient getClient(ITask review) {
		TaskRepository repository = repositoryManager.getRepository(review.getConnectorKind(),
				review.getRepositoryUrl());
		GerritConnector connector = (GerritConnector) repositoryManager.getRepositoryConnector(repository.getConnectorKind());
		GerritClient client = connector.getClient(repository);
		return client;
	}

}
