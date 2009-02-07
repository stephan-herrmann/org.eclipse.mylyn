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

package org.eclipse.mylyn.internal.bugzilla.ui.editor;

import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.mylyn.internal.bugzilla.core.BugzillaAttribute;
import org.eclipse.mylyn.internal.bugzilla.core.BugzillaCorePlugin;
import org.eclipse.mylyn.internal.bugzilla.core.BugzillaCustomField;
import org.eclipse.mylyn.internal.bugzilla.core.IBugzillaConstants;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMetaData;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tasks.ui.editors.AbstractAttributeEditor;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart;
import org.eclipse.mylyn.tasks.ui.editors.AttributeEditorFactory;
import org.eclipse.mylyn.tasks.ui.editors.LayoutHint;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorPartDescriptor;

/**
 * @author Rob Elves
 * @since 3.0
 */
public class BugzillaTaskEditorPage extends AbstractTaskEditorPage {

	public static final String ID_PART_BUGZILLA_PLANNING = "org.eclipse.mylyn.bugzilla.ui.editors.part.planning"; //$NON-NLS-1$

	public static final String ID_PART_BUGZILLA_FLAGS = "org.eclipse.mylyn.bugzilla.ui.editors.part.flags"; //$NON-NLS-1$

	public BugzillaTaskEditorPage(TaskEditor editor) {
		super(editor, BugzillaCorePlugin.CONNECTOR_KIND);
	}

	/**
	 * Call this constructor if extending the Bugzilla connector
	 * 
	 * @param editor
	 * @param connectorKind
	 */
	public BugzillaTaskEditorPage(TaskEditor editor, String connectorKind) {
		super(editor, connectorKind);
	}

	@Override
	protected Set<TaskEditorPartDescriptor> createPartDescriptors() {
		Set<TaskEditorPartDescriptor> descriptors = super.createPartDescriptors();

		// remove unnecessary default editor parts
		for (TaskEditorPartDescriptor taskEditorPartDescriptor : descriptors) {
			if (taskEditorPartDescriptor.getId().equals(ID_PART_PEOPLE)) {
				descriptors.remove(taskEditorPartDescriptor);
				break;
			}
		}

		// Add Bugzilla Planning part
		try {
			TaskData data = TasksUi.getTaskDataManager().getTaskData(getTask());
			if (data != null) {
				TaskAttribute attrEstimatedTime = data.getRoot().getMappedAttribute(
						BugzillaAttribute.ESTIMATED_TIME.getKey());
				if (attrEstimatedTime != null) {
					descriptors.add(new TaskEditorPartDescriptor(ID_PART_BUGZILLA_PLANNING) {
						@Override
						public AbstractTaskEditorPart createPart() {
							return new BugzillaPlanningEditorPart();
						}
					}.setPath(PATH_ATTRIBUTES));
				}
			}
		} catch (CoreException e) {
			// ignore
		}

		// Add the updated Bugzilla people part
		descriptors.add(new TaskEditorPartDescriptor(ID_PART_PEOPLE) {
			@Override
			public AbstractTaskEditorPart createPart() {
				return new BugzillaPeoplePart();
			}
		}.setPath(PATH_PEOPLE));

		return descriptors;
	}

	@Override
	protected AttributeEditorFactory createAttributeEditorFactory() {
		AttributeEditorFactory factory = new AttributeEditorFactory(getModel(), getTaskRepository(), getEditorSite()) {
			@Override
			public AbstractAttributeEditor createEditor(String type, final TaskAttribute taskAttribute) {
				AbstractAttributeEditor editor;
				if (IBugzillaConstants.EDITOR_TYPE_KEYWORDS.equals(type)) {
					editor = new BugzillaKeywordAttributeEditor(getModel(), taskAttribute);
				} else if (IBugzillaConstants.EDITOR_TYPE_REMOVECC.equals(type)) {
					editor = new BugzillaCcAttributeEditor(getModel(), taskAttribute);
				} else if (IBugzillaConstants.EDITOR_TYPE_VOTES.equals(type)) {
					editor = new BugzillaVotesEditor(getModel(), taskAttribute);
				} else if (IBugzillaConstants.EDITOR_TYPE_FLAG.equals(type)) {
					editor = new FlagAttributeEditor(getModel(), taskAttribute);
				} else {
					editor = super.createEditor(type, taskAttribute);
					if (TaskAttribute.TYPE_BOOLEAN.equals(type)) {
						editor.setDecorationEnabled(false);
					}
				}

				if (editor != null && taskAttribute.getId().startsWith(BugzillaCustomField.CUSTOM_FIELD_PREFIX)) {
					editor.setLayoutHint(new LayoutHint(editor.getLayoutHint()) {

						@Override
						public int getPriority() {
							return super.getPriority() * 10;
						}
					});
				}

				TaskAttributeMetaData properties = taskAttribute.getMetaData();
				if (editor != null && IBugzillaConstants.EDITOR_TYPE_FLAG.equals(properties.getType())) {
					editor.setLayoutHint(new LayoutHint(editor.getLayoutHint()) {

						@Override
						public int getPriority() {
							return super.getPriority() * 5;
						}
					});
				}

				return editor;
			}
		};
		return factory;
	}

	@Override
	public void doSubmit() {

		TaskAttribute summaryAttribute = getModel().getTaskData().getRoot().getMappedAttribute(TaskAttribute.SUMMARY);
		if (summaryAttribute != null && summaryAttribute.getValue().length() == 0) {
			getTaskEditor().setMessage(Messages.BugzillaTaskEditorPage_Please_enter_a_short_summary_before_submitting,
					IMessageProvider.ERROR);
			AbstractTaskEditorPart part = getPart(ID_PART_SUMMARY);
			if (part != null) {
				part.setFocus();
			}
			return;
		}

		TaskAttribute componentAttribute = getModel().getTaskData().getRoot().getMappedAttribute(
				BugzillaAttribute.COMPONENT.getKey());
		if (componentAttribute != null && componentAttribute.getValue().length() == 0) {
			getTaskEditor().setMessage(Messages.BugzillaTaskEditorPage_Please_select_a_component_before_submitting,
					IMessageProvider.ERROR);
			AbstractTaskEditorPart part = getPart(ID_PART_ATTRIBUTES);
			if (part != null) {
				part.setFocus();
			}
			return;
		}

		TaskAttribute descriptionAttribute = getModel().getTaskData().getRoot().getMappedAttribute(
				TaskAttribute.DESCRIPTION);
		if (descriptionAttribute != null && descriptionAttribute.getValue().length() == 0) {
			getTaskEditor().setMessage(Messages.BugzillaTaskEditorPage_Please_enter_a_description_before_submitting,
					IMessageProvider.ERROR);
			AbstractTaskEditorPart descriptionPart = getPart(ID_PART_DESCRIPTION);
			if (descriptionPart != null) {
				descriptionPart.setFocus();
			}
			return;
		}

		// Force the most recent known good token onto the outgoing task data to ensure submit
		// bug#263318
		TaskAttribute attrToken = getModel().getTaskData().getRoot().getAttribute(BugzillaAttribute.TOKEN.getKey());
		if (attrToken != null) {
			attrToken.setValue(getModel().getTask().getAttribute(BugzillaAttribute.TOKEN.getKey()));
		}

		super.doSubmit();
	}

}
