/*
 * Author : AdNovum Informatik AG
 */

package ch.thgroup.matrix.business.mail;

public enum MailType {

	PUBLISH_ORDER("publish.order");

	final String templateName;

	MailType(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateName() {
		return templateName;
	}
}

