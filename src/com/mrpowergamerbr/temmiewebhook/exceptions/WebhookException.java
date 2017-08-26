package com.mrpowergamerbr.temmiewebhook.exceptions;

public class WebhookException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2658633038861163193L;

	public WebhookException(String reason) {
		super(reason);
	}
}
