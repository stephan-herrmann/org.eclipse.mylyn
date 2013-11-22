/*******************************************************************************
 * Copyright (c) 2012 Tasktop Technologies and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop Technologies - initial API and implementation
 *     Francois Chouinard - Added support for the review labels
 *******************************************************************************/

package org.eclipse.mylyn.internal.gerrit.core.client.data;

import java.sql.Timestamp;

import org.eclipse.mylyn.internal.gerrit.core.client.rest.GerritReviewLabel;
import org.eclipse.mylyn.internal.gerrit.core.remote.GerritReviewRemoteFactory;
import org.eclipse.mylyn.reviews.core.model.ReviewStatus;

import com.google.gerrit.common.data.ChangeInfo;
import com.google.gerrit.reviewdb.Change.Status;

/**
 * @author Steffen Pingel
 */
public class GerritQueryResult {

	private int _number;

	private String branch;

	private Timestamp created;

	private String id;

	private GerritPerson owner;

	private String project;

	private String status;

	private String subject;

	private Timestamp updated;

	// 'Starred' status of the review
	private boolean starred;

	// Labels
	private GerritReviewLabel labels;

	public GerritQueryResult(ChangeInfo changeInfo) {
		setNumber(changeInfo.getId().get());
		setId(changeInfo.getKey().get());
		setProject(changeInfo.getProject().getName());
		setSubject(changeInfo.getSubject());
		Status status = changeInfo.getStatus();
		if (GerritReviewRemoteFactory.getReviewStatus(status) == ReviewStatus.DRAFT) {
			setStatus("DRAFT"); //$NON-NLS-1$
		} else {
			setStatus(status.toString());
		}
		setUpdated(changeInfo.getLastUpdatedOn());
		setStarred(changeInfo.isStarred());
	}

	public GerritQueryResult() {
	}

	public String getBranch() {
		return branch;
	}

	public Timestamp getCreated() {
		return created;
	}

	public String getId() {
		return id;
	}

	public int getNumber() {
		return _number;
	}

	public GerritPerson getOwner() {
		return owner;
	}

	public String getProject() {
		return project;
	}

	public String getStatus() {
		return status;
	}

	public String getSubject() {
		return subject;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public boolean isStarred() {
		return starred;
	}

	public GerritReviewLabel getReviewLabel() {
		return labels;
	}

	private void setId(String id) {
		this.id = id;
	}

	private void setNumber(int number) {
		this._number = number;
	}

	private void setProject(String project) {
		this.project = project;
	}

	private void setStatus(String status) {
		this.status = status;
	}

	private void setSubject(String subject) {
		this.subject = subject;
	}

	private void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	private void setStarred(boolean starred) {
		this.starred = starred;
	}

}
